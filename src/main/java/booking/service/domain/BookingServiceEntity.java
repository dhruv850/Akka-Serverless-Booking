package booking.service.domain;

import booking.service.api.BookingServiceApi;
import com.akkaserverless.javasdk.eventsourcedentity.EventSourcedEntity;
import com.akkaserverless.javasdk.eventsourcedentity.EventSourcedEntity.Effect;
import com.akkaserverless.javasdk.eventsourcedentity.EventSourcedEntityContext;
import com.google.protobuf.Empty;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Predicate;
import java.util.stream.Collectors;

// This class was initially generated based on the .proto definition by Akka Serverless tooling.
// This is the implementation for the Event Sourced Entity Service described in your booking/service/api/booking_service_api.proto file.
//
// As long as this file exists it will not be overwritten: you can maintain it yourself,
// or delete it so it is regenerated as needed.

public class BookingServiceEntity extends AbstractBookingServiceEntity {

        @SuppressWarnings("unused")
        private final String entityId;

        public BookingServiceEntity(EventSourcedEntityContext context) {
                this.entityId = context.entityId();
        }

        /*Method Handlers Starts */

        @Override
        public BookingServiceDomain.PatientState emptyState() {
                return BookingServiceDomain.PatientState.getDefaultInstance();
        }

        @Override
        public Effect<Empty> createPatient(BookingServiceDomain.PatientState currentState,
                        BookingServiceApi.PatientDetails patientDetails) {
                BookingServiceDomain.PatientAdded patientAddedEvent = BookingServiceDomain.PatientAdded.newBuilder()
                                .setPatientDetails(BookingServiceDomain.PatientDetails.newBuilder()
                                                .setPatientId(patientDetails.getPatientId())
                                                .setPatientName(patientDetails.getPatientName())
                                                .build())
                                .build();
                return effects().emitEvent(patientAddedEvent).thenReply(__ -> Empty.getDefaultInstance());
        }

        @Override
        public Effect<Empty> createBooking(BookingServiceDomain.PatientState currentState,
                        BookingServiceApi.AddBooking addBooking) {
                BookingServiceDomain.BookingAdded bookingAddedEvent = BookingServiceDomain.BookingAdded.newBuilder()
                                .setBooking(
                                                BookingServiceDomain.Bookings.newBuilder()
                                                                .setBookingId(addBooking.getBookingId())
                                                                .setType(addBooking.getType())
                                                                .setDate(addBooking.getDate())
                                                                .build())
                                .build();
                return effects().emitEvent(bookingAddedEvent).thenReply(__ -> Empty.getDefaultInstance());
        }

        @Override
        public Effect<Empty> removeBooking(
                        BookingServiceDomain.PatientState currentState,
                        BookingServiceApi.DeleteBooking command) {
                if (findItemByBookingId(currentState, command.getBookingId()).isEmpty()) {
                        return effects()
                                        .error(
                                                        "Cannot remove booking " + command.getBookingId()
                                                                        + " because it is not associated with this patient.");
                }

                BookingServiceDomain.BookingRemoved event = BookingServiceDomain.BookingRemoved.newBuilder()
                                .setBookingId(command.getBookingId()).build();

                return effects()
                                .emitEvent(event)
                                .thenReply(newState -> Empty.getDefaultInstance());
        }

        @Override
        public Effect<BookingServiceApi.Patient> retrievePatient(
                        BookingServiceDomain.PatientState currentState, // <1>
                        BookingServiceApi.GetPatient command) {
                List<BookingServiceApi.Bookings> apiItems = currentState.getBookingsList().stream()
                                .map(this::convertBooking)
                                .sorted(Comparator.comparing(BookingServiceApi.Bookings::getBookingId))
                                .collect(Collectors.toList());

                BookingServiceApi.PatientDetails patientDetails = convertPatient(currentState.getPatientDetails());

                BookingServiceApi.Patient apiPatient = BookingServiceApi.Patient.newBuilder()
                                .addAllBookings(apiItems)
                                .setPatientDetails(patientDetails)
                                .build(); // <2>
                return effects().reply(apiPatient);
        }
        
        /*Method Handlers Ends */

        /*Event Handlers Starts */

        @Override
        public BookingServiceDomain.PatientState bookingAdded(BookingServiceDomain.PatientState currentState,
                        BookingServiceDomain.BookingAdded bookingAdded) {
                Map<String, BookingServiceApi.Bookings> patient = domainPatientToMap(currentState);
                BookingServiceApi.PatientDetails currentPatientDetails = domainPatientDetailsToMap(currentState);
                BookingServiceApi.Bookings item = patient.get(bookingAdded.getBooking().getBookingId());
                if (item == null) {
                        item = domainBookingToApi(bookingAdded.getBooking());
                } else {
                        item = item.toBuilder()
                                        .setDate(bookingAdded.getBooking().getDate())
                                        .build();
                }
                patient.put(item.getBookingId(), item);
                return mapToDomainPatient(patient, currentPatientDetails);
        }

        @Override
        public BookingServiceDomain.PatientState bookingRemoved(
                        BookingServiceDomain.PatientState currentState,
                        BookingServiceDomain.BookingRemoved itemRemoved) {
                List<BookingServiceDomain.Bookings> items = removeItemByProductId(currentState,
                                itemRemoved.getBookingId());
                items.sort(Comparator.comparing(BookingServiceDomain.Bookings::getBookingId));
                return BookingServiceDomain.PatientState.newBuilder().addAllBookings(items)
                                .setPatientDetails(currentState.getPatientDetails()).build();
        }

        @Override
        public BookingServiceDomain.PatientState patientAdded(BookingServiceDomain.PatientState currentState,
                        BookingServiceDomain.PatientAdded patientAdded) {
                BookingServiceApi.PatientDetails currentPatientDetails = domainPatientDetailsToMap(currentState);
                return mapToDomainPatientDetails(patientAdded.getPatientDetails(), currentPatientDetails);
        }

        /*Event Handlers Ends */

        /*Utility Functions Starts */

        private List<BookingServiceDomain.Bookings> removeItemByProductId(
                        BookingServiceDomain.PatientState patient, String bookingId) {
                return patient.getBookingsList().stream()
                                .filter(lineItem -> !lineItem.getBookingId().equals(bookingId))
                                .collect(Collectors.toList());
        }

        private BookingServiceApi.Bookings convertBooking(BookingServiceDomain.Bookings item) {
                return BookingServiceApi.Bookings.newBuilder()
                                .setBookingId(item.getBookingId())
                                .setType(item.getType())
                                .setDate(item.getDate())
                                .build();
        }

        private BookingServiceApi.PatientDetails convertPatient(BookingServiceDomain.PatientDetails item) {
                return BookingServiceApi.PatientDetails.newBuilder()
                                .setPatientName(item.getPatientName())
                                .setPatientId(item.getPatientId())
                                .build();
        }

        private Optional<BookingServiceDomain.Bookings> findItemByBookingId(
                        BookingServiceDomain.PatientState patient, String bookingId) {
                Predicate<BookingServiceDomain.Bookings> lineItemExists = lineItem -> lineItem.getBookingId()
                                .equals(bookingId);
                return patient.getBookingsList().stream().filter(lineItemExists).findFirst();
        }

        private BookingServiceApi.Bookings domainBookingToApi(BookingServiceDomain.Bookings item) {
                return BookingServiceApi.Bookings.newBuilder()
                                .setBookingId(item.getBookingId())
                                .setType(item.getType())
                                .setDate(item.getDate())
                                .build();
        }

        private BookingServiceApi.PatientDetails domainPatientDetailsToApi(BookingServiceDomain.PatientDetails item) {
                return BookingServiceApi.PatientDetails.newBuilder()
                                .setPatientName(item.getPatientName())
                                .setPatientId(item.getPatientId())
                                .build();
        }

        private BookingServiceApi.PatientDetails domainPatientDetailsToMap(BookingServiceDomain.PatientState state) {
                return domainPatientDetailsToApi(state.getPatientDetails());
        }

        private Map<String, BookingServiceApi.Bookings> domainPatientToMap(BookingServiceDomain.PatientState state) {
                return state.getBookingsList().stream().collect(
                                Collectors.toMap(BookingServiceDomain.Bookings::getBookingId, this::domainBookingToApi));
        }

        private BookingServiceDomain.PatientState mapToDomainPatient(Map<String, BookingServiceApi.Bookings> patient,
                        BookingServiceApi.PatientDetails currentPatientDetails) {
                return BookingServiceDomain.PatientState.newBuilder()
                                .addAllBookings(patient.values().stream().map(this::apiBookingToDomain)
                                                .collect(Collectors.toList()))
                                .setPatientDetails(apiCurrentPatientDetailsToDomain(currentPatientDetails))
                                .build();
        }

        private BookingServiceDomain.PatientDetails apiCurrentPatientDetailsToDomain(
                        BookingServiceApi.PatientDetails currentPatientDetails) {
                return BookingServiceDomain.PatientDetails.newBuilder()
                                .setPatientName(currentPatientDetails.getPatientName())
                                .setPatientId(currentPatientDetails.getPatientId())
                                .build();
        }

        private BookingServiceDomain.PatientDetails apiPatientDetailsToDomain(BookingServiceDomain.PatientDetails item,
                        BookingServiceApi.PatientDetails currentPatientDetails) {

                if (item.getPatientId() == "") {
                        return BookingServiceDomain.PatientDetails.newBuilder()
                                        .setPatientName(currentPatientDetails.getPatientName())
                                        .setPatientId(currentPatientDetails.getPatientId())
                                        .build();
                } else {
                        return BookingServiceDomain.PatientDetails.newBuilder()
                                        .setPatientName(item.getPatientName())
                                        .setPatientId(item.getPatientId())
                                        .build();
                }
        }

        private BookingServiceDomain.Bookings apiBookingToDomain(BookingServiceApi.Bookings item) {
                return BookingServiceDomain.Bookings.newBuilder()
                                .setBookingId(item.getBookingId())
                                .setType(item.getType())
                                .setDate(item.getDate())
                                .build();
        }

        private BookingServiceDomain.PatientState mapToDomainPatientDetails(
                        BookingServiceDomain.PatientDetails patientDetails,
                        BookingServiceApi.PatientDetails currentPatientDetails) {
                return BookingServiceDomain.PatientState.newBuilder()
                                .setPatientDetails(apiPatientDetailsToDomain(patientDetails, currentPatientDetails))
                                .build();
        }

        /*Utility Functions Ends */
}
