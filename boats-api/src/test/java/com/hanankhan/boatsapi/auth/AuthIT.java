package com.hanankhan.boatsapi.auth;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
public class AuthIT {

    @Autowired
    MockMvc mockMvc;
    @Autowired
    ObjectMapper om;

    private static final String USERNAME = "admin";
    private static final String PASSWORD_OK = "123456789";
    private static final String PASSWORD_BAD = "wrong";
    private static final String API_AUTH_LOGIN = "/api/auth/login";
    private static final String API_BOATS = "/api/boats";

    @Test
    @DisplayName("Login fails with 401 for bad credentials")
    void login_withBadCredentials_returns401() throws Exception {
        mockMvc.perform(post(API_AUTH_LOGIN)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(om.writeValueAsBytes(Map.of("username", USERNAME, "password", PASSWORD_BAD))))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @DisplayName("Login returns 200 and accessToken for valid credentials")
    void login_withValidCredentials_returnsToken() throws Exception {
        String token = loginAndGetAccessToken(USERNAME, PASSWORD_OK);
        assertThat(token).isNotBlank();
    }

    @Test
    @DisplayName("Protected endpoint returns 401 without Authorization header")
    void protectedEndpoint_withoutToken_returns401() throws Exception {
        mockMvc.perform(get(API_BOATS))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @DisplayName("Protected endpoint returns 200 with valid Bearer token")
    void protectedEndpoint_withValidToken_returns200() throws Exception {
        String token = loginAndGetAccessToken(USERNAME, PASSWORD_OK);
        mockMvc.perform(get(API_BOATS).header("Authorization", bearer(token)))
                .andExpect(status().isOk());
    }

    private String loginAndGetAccessToken(String username, String password) throws Exception {
        var res = mockMvc.perform(post(API_AUTH_LOGIN)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(om.writeValueAsBytes(Map.of("username", username, "password", password))))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.accessToken").exists())
                .andReturn();

        JsonNode body = om.readTree(res.getResponse().getContentAsString());
        return body.get("accessToken").asText();
    }

    private String bearer(String token) {
        return "Bearer " + token;
    }
}
