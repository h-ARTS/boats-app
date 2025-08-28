package com.hanankhan.boatsapi.boats;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
public class BoatsIT {

    @Autowired
    MockMvc mockMvc;
    @Autowired
    ObjectMapper om;

    private String bearer;

    @BeforeEach
    void login() throws Exception {
        var res = mockMvc.perform(post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(om.writeValueAsBytes(Map.of(
                                "username", "admin",
                                "password", "123456789"
                        ))))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.accessToken").exists())
                .andReturn();

        String token = om.readTree(res.getResponse().getContentAsString()).get("accessToken").asText();
        bearer = "Bearer " + token;
    }

    @Test
    void list_requires_auth_and_returns_page() throws Exception {
        mockMvc.perform(get("/api/boats"))
                .andExpect(status().isUnauthorized());

        mockMvc.perform(get("/api/boats").header("Authorization", bearer))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content").isArray())
                .andExpect(jsonPath("$.number").isNumber())
                .andExpect(jsonPath("$.size").isNumber())
                .andExpect(jsonPath("$.totalElements").exists());
    }

    @Test
    void crud_flow_happy_path() throws Exception {
        // CREATE
        var createPayload = Map.of("name", "Aurora", "description", "River cruiser", "type", "Cruiser", "length", "21.5");
        var createRes = mockMvc.perform(post("/api/boats")
                        .header("Authorization", bearer)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(om.writeValueAsBytes(createPayload)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.name").value("Aurora"))
                .andReturn();

        JsonNode createdJson = om.readTree(createRes.getResponse().getContentAsString());
        long id = createdJson.get("id").asLong();
        assertThat(id).isPositive();

        // GET by id
        mockMvc.perform(get("/api/boats/{id}", id)
                        .header("Authorization", bearer))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.description").value("River cruiser"));

        // UPDATE
        var updatePayload = Map.of("name", "Aurora II", "description", "Updated", "type", "Cruiser", "length", "21.5");
        mockMvc.perform(put("/api/boats/{id}", id)
                        .header("Authorization", bearer)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(om.writeValueAsBytes(updatePayload)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Aurora II"))
                .andExpect(jsonPath("$.description").value("Updated"));

        // DELETE
        mockMvc.perform(delete("/api/boats/{id}", id)
                        .header("Authorization", bearer))
                .andExpect(status().isNoContent());

        // GET after delete -> 404
        mockMvc.perform(get("/api/boats/{id}", id)
                        .header("Authorization", bearer))
                .andExpect(status().isNotFound());
    }

    @Test
    void validation_errors_return_401() throws Exception {
        var badPayload = Map.of("name", "", "description", "x");
        mockMvc.perform(post("/api/boats")
                        .header("Authorization", bearer)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(om.writeValueAsBytes(badPayload)))
                .andExpect(status().isUnauthorized());
    }
}
