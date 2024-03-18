package com.playground;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.playground.model.Kid;
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

        @Test
        public void test400ErrorResponse() throws Exception {

                // Test for POST request with name over 100 characters long
                MvcResult result = mockMvc.perform(post("/api/kids")
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
                ResultActions resultActions = mockMvc.perform(get("/api/kids/123"))
                                .andExpect(status().isNotFound());

                assertEquals(404, resultActions.andReturn().getResponse().getStatus());
        }

}