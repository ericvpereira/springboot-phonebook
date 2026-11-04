package com.eric.phonebook.controllers;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@Transactional
class PhonebookWebIntegrationTest {

    @Autowired MockMvc mockMvc;
    @Autowired ObjectMapper mapper;

    @Test
    void webAssetsAndHealthArePublicButContactsRequireAuthentication() throws Exception {
        mockMvc.perform(get("/")).andExpect(status().isOk());
        mockMvc.perform(get("/assets/phonebook.css")).andExpect(status().isOk());
        mockMvc.perform(get("/assets/phonebook.js")).andExpect(status().isOk());
        mockMvc.perform(get("/actuator/health")).andExpect(status().isOk());
        mockMvc.perform(get("/contacts")).andExpect(status().isUnauthorized());
    }

    @Test
    void usersCannotReadOrUpdateEachOthersContacts() throws Exception {
        String payload = "{\"name\":\"Alice Private\",\"phone\":\"11999999999\",\"email\":\"alice@example.com\",\"type\":\"FRIEND\"}";
        String created = mockMvc.perform(post("/contacts").with(user("alice").roles("USER"))
                .contentType(MediaType.APPLICATION_JSON).content(payload))
                .andExpect(status().isCreated()).andReturn().getResponse().getContentAsString();
        long id = mapper.readTree(created).path("id").asLong();
        assertTrue(id > 0);
        mockMvc.perform(get("/contacts").with(user("alice").roles("USER")))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name").value("Alice Private"));
        mockMvc.perform(get("/contacts").with(user("bob").roles("USER")))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(0));
        mockMvc.perform(get("/contacts/" + id).with(user("bob").roles("USER")))
                .andExpect(status().isNotFound());
        mockMvc.perform(put("/contacts/" + id).with(user("bob").roles("USER"))
                .contentType(MediaType.APPLICATION_JSON).content(payload))
                .andExpect(status().isNotFound());
        mockMvc.perform(delete("/contacts/" + id).with(user("bob").roles("ADMIN")))
                .andExpect(status().isNotFound());
        mockMvc.perform(delete("/contacts/" + id).with(user("alice").roles("USER")))
                .andExpect(status().isForbidden());
        mockMvc.perform(delete("/contacts/" + id).with(user("alice").roles("ADMIN")))
                .andExpect(status().isNoContent());
    }
    @Test
    void registrationLoginAndJwtAuthenticatedRequest() throws Exception {
        String credentials = "{\"username\":\"integration_user\",\"password\":\"strongPassword123!\"}";
        mockMvc.perform(post("/auth/register").contentType(MediaType.APPLICATION_JSON)
                .content(credentials)).andExpect(status().isOk());

        String login = mockMvc.perform(post("/auth/login")
                .contentType(MediaType.APPLICATION_JSON).content(credentials))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.token").isNotEmpty())
                .andReturn().getResponse().getContentAsString();
        String token = mapper.readTree(login).path("token").asText();
        mockMvc.perform(get("/contacts").header("Authorization", "Bearer " + token))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(0));
        mockMvc.perform(get("/contacts").header("Authorization", "Bearer invalid-token"))
                .andExpect(status().isUnauthorized());
    }

}
