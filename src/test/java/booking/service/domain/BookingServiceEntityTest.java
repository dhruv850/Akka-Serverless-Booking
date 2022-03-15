package booking.service.domain;

import booking.service.api.BookingServiceApi;
import com.akkaserverless.javasdk.eventsourcedentity.EventSourcedEntity;
import com.akkaserverless.javasdk.eventsourcedentity.EventSourcedEntityContext;
import com.akkaserverless.javasdk.testkit.EventSourcedResult;
import com.google.protobuf.Empty;
import org.junit.Test;

import static org.junit.Assert.*;

// This class was initially generated based on the .proto definition by Akka Serverless tooling.
//
// As long as this file exists it will not be overwritten: you can maintain it yourself,
// or delete it so it is regenerated as needed.

public class BookingServiceEntityTest {

  @Test
  public void exampleTest() {
    BookingServiceEntityTestKit testKit = BookingServiceEntityTestKit.of(BookingServiceEntity::new);
    // use the testkit to execute a command
    // of events emitted, or a final updated state:
    // EventSourcedResult<SomeResponse> result = testKit.someOperation(SomeRequest);
    // verify the emitted events
    // ExpectedEvent actualEvent = result.getNextEventOfType(ExpectedEvent.class);
    // assertEquals(expectedEvent, actualEvent)
    // verify the final state after applying the events
    // assertEquals(expectedState, testKit.getState());
    // verify the response
    // SomeResponse actualResponse = result.getReply();
    // assertEquals(expectedResponse, actualResponse);
  }

  @Test
  public void createBookingTest() {
    BookingServiceEntityTestKit testKit = BookingServiceEntityTestKit.of(BookingServiceEntity::new);
    // EventSourcedResult<Empty> result = testKit.createBooking(AddBooking.newBuilder()...build());
  }


  @Test
  public void createPatientTest() {
    BookingServiceEntityTestKit testKit = BookingServiceEntityTestKit.of(BookingServiceEntity::new);
    // EventSourcedResult<Empty> result = testKit.createPatient(PatientDetails.newBuilder()...build());
  }


  @Test
  public void removeBookingTest() {
    BookingServiceEntityTestKit testKit = BookingServiceEntityTestKit.of(BookingServiceEntity::new);
    // EventSourcedResult<Empty> result = testKit.removeBooking(DeleteBooking.newBuilder()...build());
  }


  @Test
  public void retrievePatientTest() {
    BookingServiceEntityTestKit testKit = BookingServiceEntityTestKit.of(BookingServiceEntity::new);
    // EventSourcedResult<Patient> result = testKit.retrievePatient(GetPatient.newBuilder()...build());
  }

}
