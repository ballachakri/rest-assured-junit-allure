package hooks;

import io.qameta.allure.Step;
import io.restassured.RestAssured;
import io.restassured.filter.log.LogDetail;
import io.restassured.filter.log.RequestLoggingFilter;
import io.restassured.filter.log.ResponseLoggingFilter;
import io.restassured.response.Response;
import org.junit.jupiter.api.BeforeAll;
import steps.RestfulBookerApiSteps;
import utils.PropertiesReader;

import java.io.IOException;

public class Hooks {

    private static final RestfulBookerApiSteps apiSteps = new RestfulBookerApiSteps();
    public static String BASE_URI;
    public static String AUTH_TOKEN;
    public static String USER_NAME;
    public static String PASSWORD;

    @BeforeAll
    @Step("Get auth token")
    public static void getAuthToken() throws IOException {

        RestAssured.filters(
                new RequestLoggingFilter(LogDetail.ALL),
                new ResponseLoggingFilter(LogDetail.ALL)
        );

        BASE_URI = PropertiesReader.readProperties().getProperty("base.uri");
        USER_NAME = PropertiesReader.readProperties().getProperty("user.name");
        PASSWORD = PropertiesReader.readProperties().getProperty("password");

        Response response = apiSteps.getBearerToken("/auth", USER_NAME, PASSWORD);
        apiSteps.verifyStatusCode(response, 200);
        AUTH_TOKEN = apiSteps.getToken(response);
    }
}

