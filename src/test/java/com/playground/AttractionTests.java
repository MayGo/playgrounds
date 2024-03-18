package com.playground;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.playground.model.Attraction;
import com.playground.repository.AttractionRepository;
import com.playground.repository.KidRepository;
import com.playground.repository.PlaySiteRepository;
import com.playground.service.VisitorService;
import com.playground.utils.TestData;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.annotation.DirtiesContext.ClassMode;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultActions;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@DirtiesContext(classMode = ClassMode.BEFORE_EACH_TEST_METHOD)
public class AttractionTests {

        @Autowired
        private MockMvc mockMvc;

        @Autowired
        private AttractionRepository attractionRepository;

        @Autowired
        private KidRepository kidRepository;

        @Autowired
        private PlaySiteRepository playSiteRepository;

        @Autowired
        private VisitorService visitorService;

        @Test
        public void testAttractionCRUD() throws Exception {

                // Create an attraction
                MvcResult result = mockMvc.perform(post("/api/attractions")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content("{\"name\": \"Swing\"}"))
                                .andExpect(status().isOk())
                                .andReturn();

                // Extract the ID from the response

                // Extract the the ID from the response
                ObjectMapper objectMapper = new ObjectMapper();
                Attraction attraction = objectMapper.readValue(result.getResponse().getContentAsString(),
                                Attraction.class);
                Long attractionId = attraction.getId();

                // Get all attractions
                mockMvc.perform(get("/api/attractions"))
                                .andExpect(status().isOk())
                                .andExpect(jsonPath("$[0].id").value(1));

                // Get an attraction
                mockMvc.perform(get("/api/attractions/" + attractionId.toString()))
                                .andExpect(status().isOk());

                // Update an attraction
                mockMvc.perform(put("/api/attractions/" + attractionId.toString())
                                .contentType(MediaType.APPLICATION_JSON)
                                .content("{\"name\": \"Swing Set\"}"))
                                .andExpect(status().isOk());

                // Check if the attraction was updated
                mockMvc.perform(get("/api/attractions/" + attractionId.toString()))
                                .andExpect(status().isOk())
                                .andExpect(jsonPath("$.name").value("Swing Set"));

                // Delete an attraction
                mockMvc.perform(delete("/api/attractions/" + attractionId.toString()))
                                .andExpect(status().isOk());
        }

        @Test
        public void test400ErrorResponse() throws Exception {

                // Test for POST request with name over 100 characters long
                MvcResult result = mockMvc.perform(post("/api/attractions")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content("{\"name\": \"" + TestData.LONG_STRING + "\"}"))
                                .andExpect(status().isBadRequest())
                                .andExpect(jsonPath("$[0].field").value("name"))
                                .andExpect(jsonPath("$[0].message").value("size must be between 1 and 100"))
                                .andReturn();

                assertEquals(400, result.getResponse().getStatus());
        }

        @Test
        public void test404ErrorResponse() throws Exception {
                ResultActions resultActions = mockMvc.perform(get("/api/attractions/123"))
                                .andExpect(status().isNotFound());

                assertEquals(404, resultActions.andReturn().getResponse().getStatus());
        }

}