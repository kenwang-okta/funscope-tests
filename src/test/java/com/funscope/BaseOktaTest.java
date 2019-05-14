package com.funscope;

import com.funscope.resources.TestEnvironment;
import io.restassured.RestAssured;
import io.restassured.specification.RequestSpecification;
import org.apache.commons.lang3.ObjectUtils;
import org.junit.Before;
import org.junit.Rule;
import org.junit.runner.RunWith;

@RunWith(TestResourceRunner.class)
public class BaseOktaTest {

    @Rule
    public TestResourceRule testResourceRule = new TestResourceRule();

    @TestResource("environment")
    protected TestEnvironment testEnvironment;

    @Before
    public void setup() {
        RestAssured.baseURI = testEnvironment.getBaseUrl();
        RestAssured.basePath = ObjectUtils.firstNonNull(testEnvironment.getBasePath(), "/api/v1");
        RestAssured.port = ObjectUtils.firstNonNull(testEnvironment.getPort(), -1);
    }

    protected RequestSpecification with() {
        return RestAssured.with()
                .header("Accept", "application/json")
                .header("Content-Type", "application/json");
    }

}
