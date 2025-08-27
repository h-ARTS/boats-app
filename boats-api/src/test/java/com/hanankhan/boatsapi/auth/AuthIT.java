package com.hanankhan.boatsapi.auth;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Map;

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

    @Test
    void login_ok_and_protected_endpoints_require_jwt() throws Exception {
        // wrong creds
        mockMvc.perform(post("/api/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(om.writeValueAsBytes(Map.of("username", "admin", "password", "wrong")))
        ).andExpect(status().isUnauthorized());

        // correct creds, meaning jwt returned
        var res = mockMvc.perform(post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(om.writeValueAsBytes(Map.of("username", "admin", "password", "something")))
                ).andExpect(status().isOk())
                .andExpect(jsonPath("$.accessToken").exists())
                .andReturn();

        String token = om.readTree(res.getResponse().getContentAsString()).get("accessToken").asText();

        // access protected endpoint without jwt
        mockMvc.perform(get("/api/boats"))
                .andExpect(status().isUnauthorized());

        // protected endpoint with token -> 200
        mockMvc.perform(get("/api/boats").header("Authorization", "Bearer " + token))
                .andExpect(status().isOk());
    }
}
