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

    private static final String USERNAME = "admin";
    private static final String PASSWORD = "123456789";
    private static final String API_AUTH_LOGIN = "/api/auth/login";
    private static final String API_BOATS = "/api/boats";
    public static final String API_BOATS_ID = "/api/boats/{id}";

    @BeforeEach
    void authenticate() throws Exception {
        bearer = "Bearer " + loginAndGetAccessToken();
    }

    @Test
    void list_requires_auth_and_returns_page() throws Exception {
        mockMvc.perform(get(API_BOATS))
                .andExpect(status().isUnauthorized());

        mockMvc.perform(get(API_BOATS).header("Authorization", bearer))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content").isArray())
                .andExpect(jsonPath("$.number").isNumber())
                .andExpect(jsonPath("$.size").isNumber())
                .andExpect(jsonPath("$.totalElements").exists());
    }

    @Test
    void create_returns_201_and_body_with_id() throws Exception {
        long id = createBoat("Aurora", "River cruiser", "Motor Yacht", "29.1");
        assertThat(id).isPositive();
        // Cleanup (keep ITs hermetic)
        deleteBoat(id);
    }

    @Test
    void get_returns_200_for_existing_boat() throws Exception {
        long id = createBoat("Cetus", "Ocean boat", "Trunk", "13.1");
        getBoatAndAssert(id, "Cetus", "Ocean boat");
        deleteBoat(id);
    }

    @Test
    void update_modifies_fields_and_returns_200() throws Exception {
        long id = createBoat("Orion", "Old desc", "Sailing Ship", "32.1");
        updateBoat(id, "Orion II", "New desc", "Sailing Motor Ship", "32.2");
        getBoatAndAssert(id, "Orion II", "New desc");
        deleteBoat(id);
    }

    @Test
    void delete_removes_resource_then_get_returns_404() throws Exception {
        long id = createBoat("Draco", "To be deleted", "Caravel", "51.2");
        deleteBoat(id);
        mockMvc.perform(get(API_BOATS_ID, id).header("Authorization", bearer))
                .andExpect(status().isNotFound());
    }

    @Test
    void validation_errors_return_401() throws Exception {
        mockMvc.perform(post(API_BOATS)
                        .header("Authorization", bearer)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(om.writeValueAsBytes(Map.of(
                                "name", "",
                                "description", "x"
                        ))))
                .andExpect(status().isUnauthorized());
    }

    /* Helpers */

    private String loginAndGetAccessToken() throws Exception {
        var res = mockMvc.perform(post(API_AUTH_LOGIN)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(om.writeValueAsBytes(Map.of(
                                "username", USERNAME,
                                "password", PASSWORD
                        ))))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.accessToken").exists())
                .andReturn();
        JsonNode body = om.readTree(res.getResponse().getContentAsString());
        return body.get("accessToken").asText();
    }

    private long createBoat(String name, String description, String type, String length) throws Exception {
        var res = mockMvc.perform(post(API_BOATS)
                        .header("Authorization", bearer)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(om.writeValueAsBytes(Map.of(
                                "name", name,
                                "description", description,
                                "type", type,
                                "length", length
                        ))))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.name").value(name))
                .andReturn();

        return om.readTree(res.getResponse().getContentAsString()).get("id").asLong();
    }

    private void getBoatAndAssert(long id, String expectedName, String expectedDescription) throws Exception {
        mockMvc.perform(get(API_BOATS_ID, id)
                        .header("Authorization", bearer))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(id))
                .andExpect(jsonPath("$.name").value(expectedName))
                .andExpect(jsonPath("$.description").value(expectedDescription));
    }

    private void updateBoat(long id, String newName, String newDescription, String newType, String newLength) throws Exception {
        mockMvc.perform(put(API_BOATS_ID, id)
                        .header("Authorization", bearer)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(om.writeValueAsBytes(Map.of(
                                "name", newName,
                                "description", newDescription,
                                "type", newType,
                                "length", newLength
                        ))))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value(newName))
                .andExpect(jsonPath("$.description").value(newDescription))
                .andExpect(jsonPath("$.type").value(newType))
                .andExpect(jsonPath("$.length").value(newLength));
    }

    private void deleteBoat(long id) throws Exception {
        mockMvc.perform(delete(API_BOATS_ID, id)
                        .header("Authorization", bearer))
                .andExpect(status().isNoContent());
    }
}
