package steps;

import io.qameta.allure.Step;
import io.qameta.allure.restassured.AllureRestAssured;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import model.bookingIdsList.BookingIdsList;
import model.bookingdetails.BookingDetails;
import model.createBooking.CreateBooking;
import org.assertj.core.api.Assertions;
import utils.Helpers;

public class ApiSteps {

    String BASE_URI = "https://restful-booker.herokuapp.com";


    @Step("Get all booking ids")
    public Response getAllBookingIds(String basePath) {

        return RestAssured.given().filter(new AllureRestAssured())
                .baseUri(BASE_URI)
                .basePath(basePath)
                .when()
                .get()
                .thenReturn();
    }

    @Step("Get booking details by id")
    public Response getBookingDetailsById(String basePath) {

        return RestAssured.given().filter(new AllureRestAssured())
                .baseUri(BASE_URI)
                .basePath(basePath)
                .accept(ContentType.ANY)
                .when()
                .get()
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


    public void verifyStatusCode(Response response, int statusCode) {
        Assertions.assertThat(response.statusCode()).isEqualTo(statusCode);
    }

    public void verifyBookingListIsReturned(Response response) {
        BookingIdsList[] bookingIdsList = response.getBody().as(BookingIdsList[].class);

        Assertions.assertThat(bookingIdsList).isNotNull();
    }

    public void verifyBookingDetailsAreReturned(Response response, String firstName, String lastName) {
        response.prettyPeek();
        BookingDetails bookingDetails = response.getBody().as(BookingDetails.class);

        Assertions.assertThat(bookingDetails.getFirstname()).isEqualTo(firstName);
        Assertions.assertThat(bookingDetails.getLastname()).isEqualTo(lastName);
    }

    public void verifyBookingIsCreated(Response response, String firstName, String lastName) {
        CreateBooking createBooking=response.getBody().as(CreateBooking.class);

        Assertions.assertThat(createBooking.getBookingid()).isNotNull();
        Assertions.assertThat(createBooking.getBooking().getFirstname()).isEqualTo(firstName);
        Assertions.assertThat(createBooking.getBooking().getLastname()).isEqualTo(lastName);
    }

    public Integer getBookingId(Response response){
        CreateBooking createBooking=response.getBody().as(CreateBooking.class);
        return createBooking.bookingid;
    }

}
