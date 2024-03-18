package com.playground.controller;

import com.playground.model.Kid;
import com.playground.repository.KidRepository;
import com.playground.utils.ApiDeleteResponses;
import com.playground.utils.ApiGetEntityResponses;
import com.playground.utils.ApiGetResponses;
import com.playground.utils.ApiPostResponses;
import com.playground.utils.ApiPutResponses;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/kids")
@Tag(name = "Kids", description = "Operations related to kids")
public class KidController {

    private final KidRepository kidRepository;

    @Autowired
    public KidController(KidRepository kidRepository) {
        this.kidRepository = kidRepository;
    }

    @ApiGetResponses
    @Operation(summary = "View a list of all kids")
    @GetMapping
    public List<Kid> getAllKids() {
        return kidRepository.findAll();
    }

    @ApiGetEntityResponses
    @Operation(summary = "Get a kid by Id")
    @GetMapping("/{kidId}")
    public Kid getKid(@PathVariable("kidId") Kid kid) {
        return kid;
    }

    @ApiPostResponses
    @Operation(summary = "Add a kid")
    @PostMapping
    public Kid createKid(@Valid @RequestBody Kid kid) {
        return kidRepository.save(kid);
    }

    @ApiPutResponses
    @Operation(summary = "Update a kid")
    @PutMapping("/{kidId}")
    public Kid updateKid(@PathVariable("kidId") Kid kid, @Valid @RequestBody Kid newKid) {

        kid.setName(newKid.getName());
        kid.setTicketNumber(newKid.getTicketNumber());

        return kidRepository.save(newKid);
    }

    @ApiDeleteResponses
    @Operation(summary = "Delete a kid")
    @DeleteMapping("/{kidId}")
    public void deleteKid(@PathVariable("kidId") Long kidId) {
        kidRepository.deleteById(kidId);
    }
}