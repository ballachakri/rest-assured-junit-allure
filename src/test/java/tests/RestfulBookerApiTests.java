package tests;

import hooks.Hooks;
import io.qameta.allure.Feature;
import io.qameta.allure.Link;
import io.restassured.response.Response;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.jupiter.api.*;
import steps.RestfulBookerApiSteps;

@Feature("booking-api end points")
@Tag("booking-api")
public class RestfulBookerApiTests extends Hooks {

    private final String FIRST_NAME = RandomStringUtils.randomAlphanumeric(5);

    private final String LAST_NAME = RandomStringUtils.randomAlphanumeric(7);

    private static final RestfulBookerApiSteps apiSteps = new RestfulBookerApiSteps();

    @Test
    @Tag("get-all-ids")
    @Link("QA-101")
    @DisplayName("booking-api - Get all booking ids")
    public void getAllBookingIds() {

        Response response = apiSteps.getAllBookingIds("/booking");
        apiSteps.verifyStatusCode(response, 200);
        apiSteps.verifyBookingListIsReturned(response);
    }

    @Test
    @Tag("get-by-id")
    @Link("QA-102")
    @DisplayName("booking-api - Get Booking by booking id")
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
    @Link("QA-103")
    @DisplayName("booking-api - Create booking")
    public void createBooking() {

        Response response = apiSteps.createBooking("/booking", FIRST_NAME, LAST_NAME);
        apiSteps.verifyStatusCode(response, 200);
        apiSteps.verifyBookingIsCreated(response, FIRST_NAME, LAST_NAME);
    }

    @Test
    @Tag("update")
    @Link("QA-104")
    @DisplayName("booking-api - Update booking")
    public void updateBooking() {

        Response response = apiSteps.createBooking("/booking", FIRST_NAME, LAST_NAME);
        apiSteps.verifyStatusCode(response, 200);
        apiSteps.verifyBookingIsCreated(response, FIRST_NAME, LAST_NAME);
        Integer bookingId = apiSteps.getBookingId(response);

        Response response1 = apiSteps.updateBooking("/booking/" + bookingId, FIRST_NAME + 1, LAST_NAME + 1);
        System.out.println(response1.prettyPrint());
        apiSteps.verifyStatusCode(response1, 200);
        apiSteps.verifyBookingIsUpdated(response1, FIRST_NAME + 1, LAST_NAME + 1);
    }


    @Test
    @Tag("partial-update")
    @Link("QA-105")
    @DisplayName("booking-api - Update partial booking")
    public void updatePartialBooking() {

        Response response = apiSteps.createBooking("/booking", FIRST_NAME, LAST_NAME);
        apiSteps.verifyStatusCode(response, 200);
        apiSteps.verifyBookingIsCreated(response, FIRST_NAME, LAST_NAME);
        Integer bookingId = apiSteps.getBookingId(response);

        Response response1 = apiSteps.updatePartialBooking("/booking/" + bookingId, FIRST_NAME + 1, LAST_NAME + 1);
        apiSteps.verifyStatusCode(response1, 200);
        apiSteps.verifyBookingIsUpdated(response1, FIRST_NAME + 1, LAST_NAME + 1);
    }

    @Test
    @Tag("delete")
    @Link("QA-106")
    @DisplayName("booking-api - Delete booking")
    public void deleteBooking() {

        Response response = apiSteps.createBooking("/booking", FIRST_NAME, LAST_NAME);
        apiSteps.verifyStatusCode(response, 200);
        apiSteps.verifyBookingIsCreated(response, FIRST_NAME, LAST_NAME);
        Integer bookingId = apiSteps.getBookingId(response);

        Response response1 = apiSteps.deleteBooking("/booking/" + bookingId);
        apiSteps.verifyStatusCode(response1, 201);

    }
}
