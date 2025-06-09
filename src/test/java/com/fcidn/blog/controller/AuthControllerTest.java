package com.fcidn.blog.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fcidn.blog.request.auth.LoginRequest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc
@SpringBootTest
@ActiveProfiles("test")
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class AuthControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void login_givenValidCredentials_shouldReturnSuccess() throws Exception {
        LoginRequest request = new LoginRequest();
        request.setUsername("aldo");
        request.setPassword("123");

        String json = objectMapper.writeValueAsString(request);

        mockMvc.perform(post("/api/public/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json))
                        .andExpect(status().isOk())
                .andExpect(jsonPath("$.data").isNotEmpty())
                .andExpect(jsonPath("$.data.token").isNotEmpty() );
    }

    @Test
    void login_givenInvalidCredentials_shouldReturnUnauthorized() throws Exception {
        LoginRequest request = new LoginRequest();
        request.setUsername("wronguser");
        request.setPassword("wrongpass");

        String json = objectMapper.writeValueAsString(request);

        // TODO: change code if invalid credentials
        mockMvc.perform(post("/api/public/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isInternalServerError());
    }
}
