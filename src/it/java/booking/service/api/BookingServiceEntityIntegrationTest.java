package booking.service.api;

import booking.service.Main;
import booking.service.domain.BookingServiceDomain;
import com.akkaserverless.javasdk.testkit.junit.AkkaServerlessTestKitResource;
import com.google.protobuf.Empty;
import org.junit.ClassRule;
import org.junit.Test;

import static java.util.concurrent.TimeUnit.*;

// This class was initially generated based on the .proto definition by Akka Serverless tooling.
//
// As long as this file exists it will not be overwritten: you can maintain it yourself,
// or delete it so it is regenerated as needed.

// Example of an integration test calling our service via the Akka Serverless proxy
// Run all test classes ending with "IntegrationTest" using `mvn verify -Pit`
public class BookingServiceEntityIntegrationTest {

  /**
   * The test kit starts both the service container and the Akka Serverless proxy.
   */
  @ClassRule
  public static final AkkaServerlessTestKitResource testKit =
    new AkkaServerlessTestKitResource(Main.createAkkaServerless());

  /**
   * Use the generated gRPC client to call the service through the Akka Serverless proxy.
   */
  private final BookingService client;

  public BookingServiceEntityIntegrationTest() {
    client = testKit.getGrpcClient(BookingService.class);
  }

  @Test
  public void createBookingOnNonExistingEntity() throws Exception {
    // TODO: set fields in command, and provide assertions to match replies
    // client.createBooking(BookingServiceApi.AddBooking.newBuilder().build())
    //         .toCompletableFuture().get(5, SECONDS);
  }

  @Test
  public void createPatientOnNonExistingEntity() throws Exception {
    // TODO: set fields in command, and provide assertions to match replies
    // client.createPatient(BookingServiceApi.PatientDetails.newBuilder().build())
    //         .toCompletableFuture().get(5, SECONDS);
  }

  @Test
  public void removeBookingOnNonExistingEntity() throws Exception {
    // TODO: set fields in command, and provide assertions to match replies
    // client.removeBooking(BookingServiceApi.DeleteBooking.newBuilder().build())
    //         .toCompletableFuture().get(5, SECONDS);
  }

  @Test
  public void retrievePatientOnNonExistingEntity() throws Exception {
    // TODO: set fields in command, and provide assertions to match replies
    // client.retrievePatient(BookingServiceApi.GetPatient.newBuilder().build())
    //         .toCompletableFuture().get(5, SECONDS);
  }
}
