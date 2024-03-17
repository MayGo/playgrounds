package com.playground.controller;

import com.playground.model.Attraction;
import com.playground.repository.AttractionRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/api/attractions")
@Tag(name = "Attractions", description = "Operations related to attractions")
public class AttractionController {

    private final AttractionRepository attractionRepository;

    @Autowired
    public AttractionController(AttractionRepository attractionRepository) {
        this.attractionRepository = attractionRepository;
    }

    @Operation(summary = "View a list of available attractions")
    @GetMapping
    public List<Attraction> getAllAttractions() {
        return attractionRepository.findAll();
    }

    @Operation(summary = "Get an attraction by Id")
    @GetMapping("/{attractionId}")
    public Attraction getAttraction(@PathVariable("attractionId") Attraction attraction) {
        return attraction;
    }

    @Operation(summary = "Add an attraction")
    @PostMapping
    public Attraction createAttraction(@RequestBody Attraction attraction) {
        return attractionRepository.save(attraction);
    }

    @Operation(summary = "Update an attraction")
    @PutMapping("/{attractionId}")
    public Attraction updateAttraction(@PathVariable("attractionId") Attraction attraction,
            @RequestBody Attraction newAttraction) {

        attraction.setName(newAttraction.getName());

        return attractionRepository.save(attraction);
    }

    @Operation(summary = "Delete an attraction")
    @DeleteMapping("/{attractionId}")
    public void deleteAttraction(@PathVariable("attractionId") Long attractionId) {
        attractionRepository.deleteById(attractionId);
    }
}