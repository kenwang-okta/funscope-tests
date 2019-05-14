package com.funscope.resources;

import lombok.Data;

@Data
public class TestEnvironment {

    private String baseUrl;
    private String basePath;
    private Integer port;
    private String apiToken;

}
