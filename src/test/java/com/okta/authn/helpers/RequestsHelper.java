package com.okta.authn.helpers;

import com.funscope.resources.TestEnvironment;
import io.restassured.RestAssured;
import io.restassured.specification.RequestSpecification;

public abstract class RequestsHelper {

    private final TestEnvironment testEnvironment;

    public RequestsHelper(TestEnvironment testEnvironment) {
        this.testEnvironment = testEnvironment;
    }

    protected RequestSpecification with() {
        return RestAssured.with()
                .header("Accept", "application/json")
                .header("Content-Type", "application/json");
    }

    protected RequestSpecification withApiToken() {
        return with().with().header("Authorization", "SSWS " + testEnvironment.getApiToken());
    }
}
