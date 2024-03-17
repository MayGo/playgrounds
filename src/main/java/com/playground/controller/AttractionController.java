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
    @GetMapping("/{id}")
    public Attraction getAttraction(@PathVariable Long id) {
        return attractionRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Attraction not found"));
    }

    @Operation(summary = "Add an attraction")
    @PostMapping
    public Attraction createAttraction(@RequestBody Attraction attraction) {
        return attractionRepository.save(attraction);
    }

    @Operation(summary = "Update an attraction")
    @PutMapping("/{id}")
    public Attraction updateAttraction(@PathVariable Long id, @RequestBody Attraction newAttraction) {
        return attractionRepository.findById(id)
                .map(attraction -> {
                    attraction.setName(newAttraction.getName());
                    return attractionRepository.save(attraction);
                })
                .orElseGet(() -> {
                    newAttraction.setId(id);
                    return attractionRepository.save(newAttraction);
                });
    }

    @Operation(summary = "Delete an attraction")
    @DeleteMapping("/{id}")
    public void deleteAttraction(@PathVariable Long id) {
        attractionRepository.deleteById(id);
    }
}