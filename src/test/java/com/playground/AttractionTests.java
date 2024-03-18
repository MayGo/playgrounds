package com.playground;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.playground.model.Attraction;
import com.playground.model.Kid;
import com.playground.model.PlaySite;
import com.playground.repository.AttractionRepository;
import com.playground.repository.KidRepository;
import com.playground.repository.PlaySiteRepository;
import com.playground.service.VisitorService;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.annotation.DirtiesContext.ClassMode;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

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

}