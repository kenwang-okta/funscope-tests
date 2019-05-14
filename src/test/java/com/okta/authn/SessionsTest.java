package com.okta.authn;

import com.funscope.BaseOktaTest;
import com.funscope.TestResource;
import com.funscope.resources.TestUser;
import org.junit.Test;

import static org.hamcrest.Matchers.is;

public class SessionsTest extends BaseOktaTest {

    @Test
    public void testCreateSessionFromSessionToken(@TestResource("no-mfa-user") TestUser user) {
        String sessionToken = authNHelper.authenticateUserWithoutMFA(user).extract().path("sessionToken");
        String sessionId = sessionsHelper.createSession(sessionToken).assertThat().body("login", is(user.getLogin())).extract().path("id");
        sessionsHelper.deleteSessionWithApiToken(sessionId);
    }

    @Test
    public void testCreateSessionFromSessionTokenWithApiToken(@TestResource("no-mfa-user") TestUser user) {
        String sessionToken = authNHelper.authenticateUserWithoutMFA(user).extract().path("sessionToken");
        String sessionId = sessionsHelper.createSessionWithApiToken(sessionToken).assertThat().body("login", is(user.getLogin())).extract().path("id");
        sessionId = sessionsHelper.getSessionWithApiToken(sessionId).extract().path("id");
        sessionsHelper.deleteSessionWithApiToken(sessionId);
    }

    @Test
    public void testCreateSessionFromCredentials(@TestResource("no-mfa-user") TestUser user) {
        String sessionId = sessionsHelper.createSession(user.getLogin(), user.getPassword()).assertThat().body("login", is(user.getLogin())).extract().path("id");
        sessionId = sessionsHelper.getSessionWithApiToken(sessionId).extract().path("id");
        sessionsHelper.deleteSessionWithApiToken(sessionId);
    }

    @Test
    public void testCreateSessionFromCredentialsWithApiToken(@TestResource("no-mfa-user") TestUser user) {
        String sessionId = sessionsHelper.createSessionWithApiToken(user.getLogin(), user.getPassword()).assertThat().body("login", is(user.getLogin())).extract().path("id");
        sessionId = sessionsHelper.getSessionWithApiToken(sessionId).extract().path("id");
        sessionsHelper.deleteSessionWithApiToken(sessionId);
    }

}
