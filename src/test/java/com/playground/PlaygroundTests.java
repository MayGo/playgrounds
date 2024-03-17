package com.playground;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.playground.model.Attraction;
import com.playground.model.Kid;
import com.playground.model.PlaySite;
import com.playground.repository.AttractionRepository;
import com.playground.repository.KidRepository;
import com.playground.repository.PlaySiteRepository;

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
public class PlaygroundTests {

        @Autowired
        private MockMvc mockMvc;

        @Autowired
        private AttractionRepository attractionRepository;

        @Autowired
        private KidRepository kidRepository;

        @Autowired
        private PlaySiteRepository playSiteRepository;

        @BeforeEach
        public void clearDatabase() {

                // playSiteRepository.deleteAll();
                // kidRepository.deleteAll();
                // attractionRepository.deleteAll();
        }

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

        public void testPlaySiteCRUD() throws Exception {

                // Create attractions
                Attraction swing = new Attraction("Swing");
                Attraction slide = new Attraction("Slide");
                Attraction ballPit = new Attraction("Ball Pit");

                attractionRepository.saveAll(Arrays.asList(swing, slide, ballPit));
                // Get all saved attractions ids
                List<Long> attractionIds = attractionRepository.findAll().stream()
                                .map(Attraction::getId)
                                .collect(Collectors.toList());

                // Convert attractionIds to a string
                String attractionIdsString = attractionIds.stream()
                                .map(String::valueOf)
                                .collect(Collectors.joining(","));

                // Create a play site
                MvcResult result = mockMvc.perform(post("/api/playsites")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content("{\"name\": \"PlaySite 1\",\"attractions\": \"" + attractionIdsString
                                                + "\" }"))
                                .andExpect(status().isOk())
                                .andReturn();

                // Extract the ID from the response

                // Extract the the ID from the response
                ObjectMapper objectMapper = new ObjectMapper();
                Attraction attraction = objectMapper.readValue(result.getResponse().getContentAsString(),
                                Attraction.class);
                Long playSiteId = attraction.getId();

                // Get all play sites
                mockMvc.perform(get("/api/playsites"))
                                .andExpect(status().isOk())
                                .andExpect(jsonPath("$[0].id").value(1));

                // Get a play site
                mockMvc.perform(get("/api/playsites/" + playSiteId.toString()))
                                .andExpect(status().isOk());

                // Update a play site
                mockMvc.perform(put("/api/playsites/" + playSiteId.toString())
                                .contentType(MediaType.APPLICATION_JSON)
                                .content("{\"name\": \"PlaySite 2\"}"))
                                .andExpect(status().isOk());

                // Check if the play site was updated
                mockMvc.perform(get("/api/playsites/" + playSiteId.toString()))
                                .andExpect(status().isOk())
                                .andExpect(jsonPath("$.name").value("PlaySite 2"));

                // Delete a play site
                mockMvc.perform(delete("/api/playsites/" + playSiteId.toString()))
                                .andExpect(status().isOk());
        }

        @Test
        public void testAttractionCapacity() throws Exception {

                // Create attractions
                Attraction swing = new Attraction("Double Swing");
                swing.setCapacity(2);

                attractionRepository.save(swing);

                // Create a play site
                PlaySite playSite = new PlaySite("PlaySite 1", Arrays.asList(swing));
                playSiteRepository.save(playSite);

                // Add a kid to the play site
                Kid kid = new Kid("John", 1, 10);

                kidRepository.save(kid);

                mockMvc.perform(post("/api/playsites/" + playSite.getId() + "/kids")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(kid.getId().toString()))
                                .andExpect(status().isOk());

                // Check play site utilization
                mockMvc.perform(get("/api/playsites/" + playSite.getId() + "/utilization"))
                                .andExpect(status().isOk())
                                .andExpect(content().string("50.0"));

        }

        @Test
        public void testSimplePlayground() throws Exception {
                // Create attractions
                Attraction swing = new Attraction("Swing");
                Attraction slide = new Attraction("Slide");
                Attraction ballPit = new Attraction("Ball Pit");

                attractionRepository.saveAll(Arrays.asList(swing, slide, ballPit));

                // Create a play site
                PlaySite playSite = new PlaySite("PlaySite 1", Arrays.asList(swing, slide, ballPit));
                playSiteRepository.save(playSite);

                // Add a kid to the play site
                Kid kid = new Kid("John", 1, 10);

                kidRepository.save(kid);

                mockMvc.perform(post("/api/playsites/" + playSite.getId() + "/kids")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(kid.getId().toString()))
                                .andExpect(status().isOk());

                // Check play site utilization
                mockMvc.perform(get("/api/playsites/" + playSite.getId() + "/utilization"))
                                .andExpect(status().isOk())
                                .andExpect(content().string("33.33"));

                // Check total visitor count
                mockMvc.perform(get("/api/playsites/total-visitor-count"))
                                .andExpect(status().isOk())
                                .andExpect(content().string("1"));

                // Remove kid from play site
                mockMvc.perform(delete("/api/playsites/" + playSite.getId() + "/kids/" +
                                kid.getId()))
                                .andExpect(status().isOk());

                // Check total visitor count
                mockMvc.perform(get("/api/playsites/total-visitor-count"))
                                .andExpect(status().isOk())
                                .andExpect(content().string("0"));

                // Check play site utilization
                mockMvc.perform(get("/api/playsites/" + playSite.getId() + "/utilization"))
                                .andExpect(status().isOk())
                                .andExpect(content().string("0.0"));
        }

        @Test
        public void testKidWaitingInQueue() throws Exception {
                // Create attractions
                Attraction swing = new Attraction("Swing");
                Attraction slide = new Attraction("Slide");

                attractionRepository.saveAll(Arrays.asList(swing, slide));

                // Create a play site
                PlaySite playSite = new PlaySite("PlaySite 1", Arrays.asList(swing, slide));
                playSiteRepository.save(playSite);

                // Add a kid to the play site
                Kid kid = new Kid("John", 1, 10);
                Kid kid2 = new Kid("Tim", 2, 8);
                Kid kidYounger = new Kid("Tom", 3, 6);
                Kid kidOlder = new Kid("Tina", 4, 11);

                kidRepository.saveAll(Arrays.asList(kid, kid2, kidYounger, kidOlder));

                mockMvc.perform(post("/api/playsites/" + playSite.getId() + "/kids")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(kid.getId().toString()))
                                .andExpect(status().isOk());

                mockMvc.perform(post("/api/playsites/" + playSite.getId() + "/kids")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(kid2.getId().toString()))
                                .andExpect(status().isOk());

                // Check play site utilization = it is full
                mockMvc.perform(get("/api/playsites/" + playSite.getId() + "/utilization"))
                                .andExpect(status().isOk())
                                .andExpect(content().string("100.0"));

                // Check total visitor count
                mockMvc.perform(get("/api/playsites/total-visitor-count"))
                                .andExpect(status().isOk())
                                .andExpect(content().string("2"));

                // Try to add younger kid to the play site if it is full
                mockMvc.perform(post("/api/playsites/" + playSite.getId() + "/kids")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(kidYounger.getId().toString()))
                                .andExpect(status().isUnprocessableEntity())
                                .andExpect(content().string("Kid does not accept waiting in queue"));

                // Add older kid to the play site if it is full
                mockMvc.perform(post("/api/playsites/" + playSite.getId() + "/kids")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(kidOlder.getId().toString()))
                                .andExpect(status().isOk());

                // Check kid in queue
                mockMvc.perform(get("/api/playsites/" + playSite.getId()))
                                .andExpect(status().isOk())
                                .andExpect(jsonPath("$.kids.length()").value(2))
                                .andExpect(jsonPath("$.queue.length()").value(1))
                                .andExpect(jsonPath("$.queue[0].id").value(kidOlder.getId().intValue()));

                // Remove kid from play site to check if the kid from the queue will be added
                mockMvc.perform(delete("/api/playsites/" + playSite.getId() + "/kids/" +
                                kid.getId()))
                                .andExpect(status().isOk());

                // Check kid in queue
                mockMvc.perform(get("/api/playsites/" + playSite.getId()))
                                .andExpect(status().isOk())
                                .andExpect(jsonPath("$.kids.length()").value(2))
                                .andExpect(jsonPath("$.kids[0].id").value(kid2.getId().intValue()))
                                .andExpect(jsonPath("$.kids[1].id").value(kidOlder.getId().intValue()))
                                .andExpect(jsonPath("$.queue.length()").value(0));

        }

}