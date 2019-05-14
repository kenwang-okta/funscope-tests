package com.okta.authn;

import com.funscope.BaseOktaTest;
import com.funscope.TestResource;
import com.funscope.resources.TestUser;
import com.google.common.collect.ImmutableMap;
import io.restassured.http.Method;
import io.restassured.response.ValidatableResponse;
import org.junit.Test;

import static org.hamcrest.Matchers.is;

public class LoginTest extends BaseOktaTest {

    @Test
    public void testSecurityQuestionMFAAuthenticationFlow(@TestResource("user") TestUser testUser) {
        ValidatableResponse response = with()
                .body(ImmutableMap.of("username", testUser.getLogin(), "password", testUser.getPassword()))
                .when()
                .request(Method.POST, "/authn")
                .then()
                .log()
                .body()
                .assertThat()
                .statusCode(200)
                .body("status", is("MFA_REQUIRED"))
                .body("_embedded.factors[0].factorType", is("question"));

        String stateToken = response.extract().path("stateToken");
        String questionFactorId = response.extract().path("_embedded.factors[0].id");

        with().pathParam("factorId", questionFactorId)
                .body(ImmutableMap.of("stateToken", stateToken, "answer", testUser.getFactors().getQuestion().getAnswer()))
                .request(Method.POST, "/authn/factors/{factorId}/verify")
                .then()
                .log()
                .body()
                .assertThat()
                .statusCode(200)
                .body("status", is("SUCCESS"));
    }



}
