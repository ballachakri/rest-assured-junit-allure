package tests;


import io.qameta.allure.Description;
import io.qameta.allure.Epic;
import io.restassured.response.Response;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import steps.ApiSteps;

@Epic("booking-api end points")
@Tag("booking-api")
public class ApiTest {

    private final String FIRST_NAME = RandomStringUtils.randomAlphanumeric(5);
    private final String LAST_NAME = RandomStringUtils.randomAlphanumeric(7);

    ApiSteps apiSteps = new ApiSteps();

    @Test
    @Tag("get-all-ids")
    @Description("booking-api - Get all booking ids")
    public void getAllBookingIds() {

        Response response = apiSteps.getAllBookingIds("/booking");
        apiSteps.verifyStatusCode(response, 200);
        apiSteps.verifyBookingListIsReturned(response);
    }

    @Test
    @Tag("get-by-id")
    @Description("booking-api - Get Booking by booking id")
    public void getBookingDetailsById() {

        Response response = apiSteps.createBooking("/booking", FIRST_NAME, LAST_NAME);
        apiSteps.verifyStatusCode(response, 200);
        apiSteps.verifyBookingIsCreated(response, FIRST_NAME, LAST_NAME);
        Integer bookingId = apiSteps.getBookingId(response);

        Response response1 = apiSteps.getBookingDetailsById("/booking/" + bookingId);
        apiSteps.verifyStatusCode(response1, 200);
        apiSteps.verifyBookingDetailsAreReturned(response1, FIRST_NAME, LAST_NAME);
    }

    @Test
    @Tag("create")
    @Description("booking-api - Create booking")
    public void createBooking() {

        Response response = apiSteps.createBooking("/booking", FIRST_NAME, LAST_NAME);
        System.out.println(response.prettyPrint());
        apiSteps.verifyStatusCode(response, 200);
        apiSteps.verifyBookingIsCreated(response, FIRST_NAME, LAST_NAME);
    }

}
