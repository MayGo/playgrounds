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
public class KidTests {

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
        public void testKidCRUD() throws Exception {

                // Create a kid

                mockMvc.perform(post("/api/kids")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content("{\"name\": \"John\",\"ticketNumber\": 1, \"age\": 10}"))
                                .andExpect(status().isOk());

                // Get all kids

                MvcResult result = mockMvc.perform(get("/api/kids"))
                                .andExpect(status().isOk())
                                .andExpect(jsonPath("$[0].id").value(1))
                                .andReturn();

                // Extract the first kid ID from the response
                ObjectMapper objectMapper = new ObjectMapper();
                Kid[] kids = objectMapper.readValue(result.getResponse().getContentAsString(), Kid[].class);
                Long firstKidId = kids[0].getId();

                // Get a kid
                mockMvc.perform(get("/api/kids/" + firstKidId.toString()))
                                .andExpect(status().isOk());

                // Update a kid
                mockMvc.perform(put("/api/kids/" + firstKidId.toString())
                                .contentType(MediaType.APPLICATION_JSON)
                                .content("{\"name\": \"John Doe\",\"ticketNumber\": 2 }"))
                                .andExpect(status().isOk());

                // Check if the kid was updated
                mockMvc.perform(get("/api/kids/" + firstKidId.toString()))
                                .andExpect(status().isOk())
                                .andExpect(jsonPath("$.name").value("John Doe"))
                                .andExpect(jsonPath("$.ticketNumber").value(2));

                // Delete a kid
                mockMvc.perform(delete("/api/kids/" + firstKidId.toString()))
                                .andExpect(status().isOk());
        }

}