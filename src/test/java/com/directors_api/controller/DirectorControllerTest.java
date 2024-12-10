package com.directors_api.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get; // For performing GET requests
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status; // For checking the response status
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath; // For JSON path evaluations

@SpringBootTest
@AutoConfigureMockMvc
public class DirectorControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @Test
    public void testGetDirectorsEndpoint() throws Exception {
        mockMvc.perform(get("/api/directors?threshold=4"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.directors").isArray())
                .andExpect(jsonPath("$.directors[0]").value("Pedro Almodóvar"));
    }
}