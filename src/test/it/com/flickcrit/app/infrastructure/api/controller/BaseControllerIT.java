package com.flickcrit.app.infrastructure.api.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.flickcrit.app.config.TestSecurityConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest
@Import(TestSecurityConfig.class)
public class BaseControllerIT  {

    @Autowired
    protected MockMvc mockMvc;

    @Autowired
    protected ObjectMapper objectMapper;

}
