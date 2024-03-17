package com.playground.controller;

import com.playground.model.PlaySite;
import com.playground.repository.PlaySiteRepository;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "PlaySite Management System")
@RestController
@RequestMapping("/api/playsites")
public class PlaySiteController {

    private final PlaySiteRepository playSiteRepository;

    @Autowired
    public PlaySiteController(PlaySiteRepository playSiteRepository) {
        this.playSiteRepository = playSiteRepository;
    }

    @Operation(summary = "View a list of all playsites")
    @GetMapping
    public List<PlaySite> getAllPlaySites() {
        return playSiteRepository.findAll();
    }

    @Operation(summary = "Get a playsite by Id")
    @GetMapping("/{id}")
    public PlaySite getPlaySite(@PathVariable Long id) {
        return playSiteRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("PlaySite not found"));
    }

    @Operation(summary = "Add a playsite")
    @PostMapping
    public PlaySite createPlaySite(@RequestBody PlaySite playSite) {
        return playSiteRepository.save(playSite);
    }

    @Operation(summary = "Update a playsite")
    @PutMapping("/{id}")
    public PlaySite updatePlaySite(@PathVariable Long id, @RequestBody PlaySite newPlaySite) {
        return playSiteRepository.findById(id)
                .map(playSite -> {
                    playSite.setName(newPlaySite.getName());
                    return playSiteRepository.save(playSite);
                })
                .orElseGet(() -> {
                    newPlaySite.setId(id);
                    return playSiteRepository.save(newPlaySite);
                });
    }

    @Operation(summary = "Delete a playsite")
    @DeleteMapping("/{id}")
    public void deletePlaySite(@PathVariable Long id) {
        playSiteRepository.deleteById(id);
    }
}