package steps;

import hooks.Hooks;
import io.qameta.allure.Step;
import io.qameta.allure.restassured.AllureRestAssured;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import model.auth.Token;
import model.bookingIdsList.BookingIdsList;
import model.bookingdetails.BookingDetails;
import model.createBooking.CreateBooking;
import org.assertj.core.api.Assertions;
import utils.Helpers;

import java.util.HashMap;
import java.util.Map;

public class RestfulBookerApiSteps extends Hooks {

    String BASE_URI = "https://restful-booker.herokuapp.com";

    @Step("Get response")
    public Response getResponse(String basePath) {

        return RestAssured.given().filter(new AllureRestAssured())
                .baseUri(BASE_URI)
                .basePath(basePath)
                .accept(ContentType.ANY)
                .when()
                .get()
                .thenReturn();

    }

    @Step("Get bearer token")
    public Response getBearerToken(String basePath, String userName, String password) {

        String authRequestBody = new Helpers().authRequestBody(userName, password);

        return RestAssured.given().filter(new AllureRestAssured())
                .baseUri(BASE_URI)
                .basePath(basePath)
                .contentType(ContentType.JSON)
                .when()
                .body(authRequestBody)
                .post()
                .thenReturn();
    }

    @Step("Create booking")
    public Response createBooking(String basePath, String firstName, String lastName) {

        String bookingRequestBody = new Helpers().getBookingRequestBody(firstName, lastName);

        return RestAssured.given().filter(new AllureRestAssured())
                .baseUri(BASE_URI)
                .basePath(basePath)
                .contentType(ContentType.JSON)
                .when()
                .body(bookingRequestBody)
                .post()
                .thenReturn();
    }

    @Step("Update booking")
    public Response updateBooking(String basePath, String firstName, String lastName) {

        String bookingRequestBody = new Helpers().getBookingRequestBody(firstName, lastName);

        return RestAssured.given().filter(new AllureRestAssured())
                .baseUri(BASE_URI)
                .basePath(basePath)
                .headers(getAuthHeader())
                .contentType(ContentType.JSON)
                .accept(ContentType.ANY)
                .when()
                .body(bookingRequestBody)
                .put()
                .thenReturn();
    }

    @Step("Update partial booking")
    public Response updatePartialBooking(String basePath, String firstName, String lastName) {

        String bookingRequestBody = new Helpers().getBookingRequestBody(firstName, lastName);

        return RestAssured.given().filter(new AllureRestAssured())
                .baseUri(BASE_URI)
                .basePath(basePath)
                .headers(getAuthHeader())
                .contentType(ContentType.JSON)
                .accept(ContentType.ANY)
                .when()
                .body(bookingRequestBody)
                .patch()
                .thenReturn();
    }

    @Step("delete booking")
    public Response deleteBooking(String basePath) {

        return RestAssured.given().filter(new AllureRestAssured())
                .baseUri(BASE_URI)
                .basePath(basePath)
                .headers(getAuthHeader())
                .contentType(ContentType.JSON)
                .delete()
                .thenReturn();
    }

    @Step("Get all booking ids")
    public Response getAllBookingIds(String basePath) {
        return getResponse(basePath);
    }

    @Step("Get booking details by id")
    public Response getBookingDetailsById(String basePath) {
        return getResponse(basePath);
    }

    @Step("Verify status code '{1}'")
    public void verifyStatusCode(Response response, int statusCode) {
        Assertions.assertThat(response.statusCode()).isEqualTo(statusCode);
    }

    @Step("Verify booking list is returned")
    public void verifyBookingListIsReturned(Response response) {
        BookingIdsList[] bookingIdsList = response.getBody().as(BookingIdsList[].class);

        Assertions.assertThat(bookingIdsList).isNotNull();
    }

    @Step("Verify booking details are returned")
    public void verifyBookingDetailsAreReturned(Response response, String firstName, String lastName) {
        BookingDetails bookingDetails = response.getBody().as(BookingDetails.class);

        Assertions.assertThat(bookingDetails.getFirstname()).isEqualTo(firstName);
        Assertions.assertThat(bookingDetails.getLastname()).isEqualTo(lastName);
    }

    @Step("Verify booking is created")
    public void verifyBookingIsCreated(Response response, String firstName, String lastName) {
        CreateBooking createBooking = response.getBody().as(CreateBooking.class);

        Assertions.assertThat(createBooking.getBookingid()).isNotNull();
        Assertions.assertThat(createBooking.getBooking().getFirstname()).isEqualTo(firstName);
        Assertions.assertThat(createBooking.getBooking().getLastname()).isEqualTo(lastName);
    }

    @Step("Verify booking is updated")
    public void verifyBookingIsUpdated(Response response, String firstName, String lastName) {
        BookingDetails bookingDetails = response.getBody().as(BookingDetails.class);

        Assertions.assertThat(bookingDetails.getFirstname()).isEqualTo(firstName);
        Assertions.assertThat(bookingDetails.getLastname()).isEqualTo(lastName);
    }

    @Step("Get token")
    public String getToken(Response response) {
        Token token = response.getBody().as(Token.class);
        return token.getToken();
    }

    @Step("Get booking id")
    public Integer getBookingId(Response response) {
        CreateBooking createBooking = response.getBody().as(CreateBooking.class);
        return createBooking.bookingid;
    }

    @Step("Get headers")
    public Map<String, String> getAuthHeader() {
        Map<String, String> header = new HashMap<>();
        header.put("Cookie", "token=" + AUTH_TOKEN);
        return header;
    }

}
