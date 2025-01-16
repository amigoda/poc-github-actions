package com.amigoda.pocgithubactions.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.web.client.DefaultResponseErrorHandler;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.net.HttpURLConnection;

public class RestControllerTest {

    protected RestTemplate restTemplate;

    protected RestTemplate createRestTemplate() {

        final RestTemplate restTemplate = new RestTemplate(new SimpleClientHttpRequestFactory() {
            @Override
            protected void prepareConnection(HttpURLConnection connection, String httpMethod) throws IOException {
                super.prepareConnection(connection, httpMethod);
                connection.setInstanceFollowRedirects(false);
            }
        });

        restTemplate.setErrorHandler(new DefaultResponseErrorHandler() {
            @Override
            public void handleError(ClientHttpResponse response) {
            }
        });

        return restTemplate;
    }

    protected ResponseEntity<String> getForString(String url) {

        ResponseEntity<String> response;
        try {
            response = restTemplate.getForEntity(url, String.class);
        }
        catch (HttpStatusCodeException exc) {
            response = new ResponseEntity<>(exc.getStatusCode());
        }
        catch (RestClientException exc) {
            response = new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return response;
    }

    protected String wrongStatusCodeMsg(String url, String expected, HttpStatusCode actual) {
        return wrongStatusCodeMsg(url, expected, actual.toString());
    }

    protected String wrongStatusCodeMsg(String url, String expected, String actual) {
        return wrongMsg("Wrong status code", url, expected, actual);
    }

    protected String wrongResultMsg(String url, String expected, String actual) {
        return wrongMsg("Wrong result", url, expected, actual);
    }

    protected String wrongMsg(String subject, String url, String expected, String actual) {
        return subject + " for URL [" + url + "]. Expected [" + expected + "] but got [" + actual + "]";
    }
}
