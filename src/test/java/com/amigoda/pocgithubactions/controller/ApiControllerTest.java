package com.amigoda.pocgithubactions.controller;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class ApiControllerTest extends RestControllerTest {

    @LocalServerPort
    private int port;

    final static String timeRegex = "^\\d{4}-\\d{2}-\\d{2} \\d{2}:\\d{2}:\\d{2}$";

    protected boolean isValidTime(String time) {
        return time.matches(timeRegex);
    }

    @BeforeEach
    public void setUp() {
        restTemplate = createRestTemplate();
    }

    @Test
    public void testApiTimeWithoutTimeZoneReturnsValidTime() {

        final String testUrl = "http://localhost:" + port + "/api/time";

        final ResponseEntity<String> response = getForString(testUrl);

        final HttpStatusCode httpStatusCode = response.getStatusCode();
        Assertions.assertTrue(httpStatusCode.is2xxSuccessful(), wrongStatusCodeMsg(testUrl, "2xx", httpStatusCode));

        final String body = response.getBody();
        Assertions.assertNotNull(body, wrongResultMsg(testUrl, "time", null));
        Assertions.assertTrue(isValidTime(body), wrongResultMsg(testUrl, "time", body));
    }

    @Test
    public void testApiTimeWithTimeZoneGMTReturnsValidTime() {

        final String testUrl = "http://localhost:" + port + "/api/time?timeZone=GMT";

        final ResponseEntity<String> response = getForString(testUrl);

        final HttpStatusCode httpStatusCode = response.getStatusCode();
        Assertions.assertTrue(httpStatusCode.is2xxSuccessful(), wrongStatusCodeMsg(testUrl, "2xx", httpStatusCode));

        final String body = response.getBody();
        Assertions.assertNotNull(body, wrongResultMsg(testUrl, "time", null));
        Assertions.assertTrue(isValidTime(body), wrongResultMsg(testUrl, "time", body));
    }

    @Test
    public void testApiTimeWithTimeZoneUTCMinus5ReturnsValidTime() {

        final String testUrl = "http://localhost:" + port + "/api/time?timeZone=UTC-5";

        final ResponseEntity<String> response = getForString(testUrl);

        final HttpStatusCode httpStatusCode = response.getStatusCode();
        Assertions.assertTrue(httpStatusCode.is2xxSuccessful(), wrongStatusCodeMsg(testUrl, "2xx", httpStatusCode));

        final String body = response.getBody();
        Assertions.assertNotNull(body, wrongResultMsg(testUrl, "time", null));
        Assertions.assertTrue(isValidTime(body), wrongResultMsg(testUrl, "time", body));
    }

    @Test
    public void testApiTimeWithInvalidTimeZoneReturnsErrorMessage() {

        final String testUrl = "http://localhost:" + port + "/api/time?timeZone=XXX";

        final ResponseEntity<String> response = getForString(testUrl);

        final HttpStatusCode httpStatusCode = response.getStatusCode();
        Assertions.assertTrue(httpStatusCode.is2xxSuccessful(), wrongStatusCodeMsg(testUrl, "2xx", httpStatusCode));

        final String body = response.getBody();
        Assertions.assertNotNull(body, wrongResultMsg(testUrl, "error message", null));
        Assertions.assertTrue(body.contains("Unknown time-zone"), wrongResultMsg(testUrl, "error message", body));
    }
}
