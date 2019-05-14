package com.okta.authn;

import com.funscope.BaseOktaTest;
import com.funscope.TestResource;
import com.funscope.resources.TestUser;
import io.restassured.response.ValidatableResponse;
import org.junit.Test;

import static org.hamcrest.Matchers.is;

public class LoginTest extends BaseOktaTest {

    @Test
    public void testSecurityQuestionMFAAuthenticationFlow(@TestResource("user") TestUser testUser) {
        ValidatableResponse response = authNHelper.authenticateUserWithMFA(testUser).assertThat().body("_embedded.factors[0].factorType", is("question"));
        String stateToken = response.extract().path("stateToken");
        String questionFactorId = response.extract().path("_embedded.factors[0].id");
        authNHelper.answerQuestionChallenge(stateToken, questionFactorId, testUser.getFactors().getQuestion().getAnswer());
    }

}
