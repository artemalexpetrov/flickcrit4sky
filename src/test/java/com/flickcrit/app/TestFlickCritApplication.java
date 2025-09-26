package com.flickcrit.app;

import org.springframework.boot.SpringApplication;

public class TestFlickCritApplication {

    public static void main(String[] args) {
        SpringApplication.from(FlickCritApplication::main)
            .with(TestcontainersConfiguration.class)
            .run(args);
    }

}
