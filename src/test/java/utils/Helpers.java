package utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import model.auth.AuthToken;
import model.bookingdetails.BookingDetails;
import model.bookingdetails.Bookingdates;

public class Helpers {

    public String getBookingRequestBody(String firstName, String lastName) {

        BookingDetails bookingDetails = new BookingDetails();
        bookingDetails.setFirstname(firstName);
        bookingDetails.setLastname(lastName);
        bookingDetails.setTotalprice(124);
        bookingDetails.setDepositpaid(true);

        Bookingdates bookingdates = new Bookingdates();
        bookingdates.setCheckin("2024-01-01");
        bookingdates.setCheckout("2024-01-04");
        bookingDetails.setAdditionalneeds("Breakfast");
        bookingDetails.setBookingdates(bookingdates);

        ObjectMapper objectMapper = new ObjectMapper();

        try {
            return objectMapper.writeValueAsString(bookingDetails);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e.getMessage());
        }

    }

    public String authRequestBody(String userName, String password) {
        AuthToken authToken = new AuthToken();
        ObjectMapper objectMapper = new ObjectMapper();

        authToken.setUsername(userName);
        authToken.setPassword(password);

        try {
            return objectMapper.writeValueAsString(authToken);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e.getMessage());
        }
    }
}
