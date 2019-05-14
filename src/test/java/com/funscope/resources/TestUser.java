package com.funscope.resources;

import lombok.Data;

@Data
public class TestUser {

    private String id;
    private String login;
    private String password;
    private TestFactors factors;

}
