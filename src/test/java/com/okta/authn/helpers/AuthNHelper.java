package com.okta.authn.helpers;

import com.funscope.resources.TestEnvironment;
import com.funscope.resources.TestUser;
import com.google.common.collect.ImmutableMap;
import io.restassured.http.Method;
import io.restassured.response.ValidatableResponse;

import static org.hamcrest.Matchers.is;

public class AuthNHelper extends RequestsHelper {

    public AuthNHelper(TestEnvironment testEnvironment) {
        super(testEnvironment);
    }

    public ValidatableResponse authenticateUserWithMFA(TestUser user) {
        return with().body(ImmutableMap.of("username", user.getLogin(), "password", user.getPassword()))
                .when().request(Method.POST, "/authn")
                .then()
                .log().body()
                .assertThat()
                .statusCode(200)
                .body("status", is("MFA_REQUIRED"));
    }

    public ValidatableResponse authenticateUserWithoutMFA(TestUser user) {
        return with().body(ImmutableMap.of("username", user.getLogin(), "password", user.getPassword()))
                .when().request(Method.POST, "/authn")
                .then()
                .log().body()
                .assertThat()
                .statusCode(200)
                .body("status", is("SUCCESS"));
    }

    public ValidatableResponse answerQuestionChallenge(String stateToken, String factorId, String answer) {
        return with().pathParam("factorId", factorId)
                .body(ImmutableMap.of("stateToken", stateToken, "answer", answer))
                .request(Method.POST, "/authn/factors/{factorId}/verify")
                .then()
                .log().body()
                .assertThat()
                .statusCode(200)
                .body("status", is("SUCCESS"));
    }
}
