package com.funscope;

import com.funscope.resources.TestEnvironment;
import com.okta.authn.helpers.AuthNHelper;
import com.okta.authn.helpers.SessionsHelper;
import io.restassured.RestAssured;
import org.apache.commons.lang3.ObjectUtils;
import org.junit.Before;
import org.junit.Rule;
import org.junit.runner.RunWith;

@RunWith(TestResourceRunner.class)
public class BaseOktaTest {

    @Rule
    public TestResourceRule testResourceRule = new TestResourceRule();

    @TestResource("environment")
    protected TestEnvironment environment;

    protected AuthNHelper authNHelper;
    protected SessionsHelper sessionsHelper;

    @Before
    public void setup() {
        RestAssured.baseURI = environment.getBaseUrl();
        RestAssured.basePath = ObjectUtils.firstNonNull(environment.getBasePath(), "/api/v1");
        RestAssured.port = ObjectUtils.firstNonNull(environment.getPort(), -1);

        authNHelper = new AuthNHelper(environment);
        sessionsHelper = new SessionsHelper(environment);
    }

}
