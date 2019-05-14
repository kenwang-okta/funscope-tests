package com.okta.authn.helpers;

import com.funscope.resources.TestEnvironment;
import com.google.common.collect.ImmutableMap;
import io.restassured.http.Method;
import io.restassured.response.ValidatableResponse;

import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;
import static org.hamcrest.Matchers.nullValue;

public class SessionsHelper extends RequestsHelper {

    public SessionsHelper(TestEnvironment testEnvironment) {
        super(testEnvironment);
    }

    public ValidatableResponse createSession(String sessionToken) {
        return with().body(ImmutableMap.of("sessionToken", sessionToken))
                .when()
                .request(Method.POST, "/sessions")
                .then()
                .log().body()
                .assertThat()
                .statusCode(200)
                .body("id", is(not(nullValue())));
    }

    public ValidatableResponse createSessionWithApiToken(String sessionToken) {
        return withApiToken()
                .with().body(ImmutableMap.of("sessionToken", sessionToken))
                .when().request(Method.POST, "/sessions")
                .then()
                .log().body()
                .assertThat()
                .statusCode(200)
                .body("id", is(not(nullValue())));
    }

    public ValidatableResponse getSessionWithApiToken(String sessionId) {
        return withApiToken()
                .with().pathParam("sessionId", sessionId)
                .when().request(Method.GET, "/sessions/{sessionId}")
                .then()
                .log().body()
                .assertThat()
                .statusCode(200)
                .body("id", is(sessionId));

    }

    public ValidatableResponse deleteSessionWithApiToken(String sessionId) {
        return withApiToken()
                .with().pathParam("sessionId", sessionId)
                .when().request(Method.DELETE, "/sessions/{sessionId}")
                .then()
                .log().body()
                .assertThat()
                .statusCode(204);
    }

    public ValidatableResponse createSession(String username, String password) {
        return with().body(ImmutableMap.of("username", username, "password", password))
                .when().request(Method.POST, "/sessions")
                .then()
                .log().body()
                .assertThat()
                .statusCode(200)
                .body("id", is(not(nullValue())));
    }

    public ValidatableResponse createSessionWithApiToken(String username, String password) {
        return withApiToken().body(ImmutableMap.of("username", username, "password", password))
                .when().request(Method.POST, "/sessions")
                .then()
                .log().body()
                .assertThat()
                .statusCode(200)
                .body("id", is(not(nullValue())));
    }
}
