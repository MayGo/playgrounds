package com.playground.controller;

import com.playground.exception.NotWaitingException;
import com.playground.model.Kid;
import com.playground.model.PlaySite;
import com.playground.repository.KidRepository;
import com.playground.repository.PlaySiteRepository;
import com.playground.service.PlaygroundService;
import com.playground.utils.ApiDeleteResponses;
import com.playground.utils.ApiGetEntityResponses;
import com.playground.utils.ApiGetResponses;
import com.playground.utils.ApiPostResponses;
import com.playground.utils.ApiPutResponses;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "PlaySite Management System")
@RestController
@RequestMapping("/api/playsites")
public class PlaySiteController {

    private final PlaySiteRepository playSiteRepository;
    private final KidRepository kidRepository;
    private final PlaygroundService playgroundService;

    @Autowired
    public PlaySiteController(PlaySiteRepository playSiteRepository, KidRepository kidRepository,
            PlaygroundService playgroundService) {
        this.playSiteRepository = playSiteRepository;
        this.kidRepository = kidRepository;
        this.playgroundService = playgroundService;
    }

    @ApiGetResponses
    @Operation(summary = "View a list of all playsites")
    @GetMapping
    public List<PlaySite> getAllPlaySites() {
        return playSiteRepository.findAll();
    }

    @ApiGetEntityResponses
    @Operation(summary = "Get a playsite by Id")
    @GetMapping("/{playSiteId}")
    public PlaySite getPlaySite(@PathVariable("playSiteId") PlaySite playSite) {
        return playSite;
    }

    @ApiPostResponses
    @Operation(summary = "Add a playsite")
    @PostMapping
    public PlaySite createPlaySite(@Valid @RequestBody PlaySite playSite) {
        return playSiteRepository.save(playSite);
    }

    @ApiPutResponses
    @Operation(summary = "Update a playsite")
    @PutMapping("/{playSiteId}")
    public PlaySite updatePlaySite(@PathVariable("playSiteId") PlaySite playSite,
            @Valid @RequestBody PlaySite newPlaySite) {

        playSite.setName(newPlaySite.getName());

        return playSiteRepository.save(playSite);
    }

    @ApiPostResponses
    @Operation(summary = "Add a kid to a playsite")
    @PostMapping("/{playSiteId}/kids")
    public ResponseEntity<String> addKidToPlaySite(
            @PathVariable("playSiteId") @Parameter(description = "Id of the playsite to add the kid to") PlaySite playSite,
            @RequestBody @Parameter(description = "Id of the kid to add to the playsite") Long kidId) {
        try {
            Kid kid = kidRepository.getReferenceById(kidId);
            playgroundService.addKidToPlaySite(playSite, kid);
            return ResponseEntity.status(HttpStatus.OK).build();
        } catch (NotWaitingException e) {
            // Handle the exception here
            return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).body(e.getMessage());
        }
    }

    @ApiDeleteResponses
    @Operation(summary = "Remove a kid from a playsite")
    @DeleteMapping("/{playSiteId}/kids/{kidId}")
    public ResponseEntity<Kid> removeKidFromPlaySite(
            @PathVariable("playSiteId") @Parameter(description = "Id of the playsite to remove the kid from") PlaySite playSite,
            @PathVariable("kidId") @Parameter(description = "Id of the kid to remove from the playsite") Kid kid) {

        playgroundService.removeKidFromPlaySite(playSite, kid);

        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @ApiDeleteResponses
    @Operation(summary = "Delete a playsite")
    @DeleteMapping("/{playSiteId}")
    public void deletePlaySite(@PathVariable("playSiteId") Long id) {
        playSiteRepository.deleteById(id);
    }

    @ApiGetEntityResponses
    @GetMapping("/{playSiteId}/utilization")
    public Double getPlaySiteUtilization(@PathVariable("playSiteId") PlaySite playSite) {
        return playgroundService.getPlaySiteUtilization(playSite);
    }

    @ApiGetResponses
    @GetMapping("/total-visitor-count")
    public Integer getTotalVisitorCount() {
        return playgroundService.getTotalVisitorCount();
    }
}