package com.playground.controller;

import com.playground.model.Kid;
import com.playground.repository.KidRepository;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

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

    @Operation(summary = "View a list of all kids")
    @GetMapping
    public List<Kid> getAllKids() {
        return kidRepository.findAll();
    }

    @Operation(summary = "Get a kid by Id")
    @GetMapping("/{kidId}")
    public Kid getKid(@PathVariable("kidId") Kid kid) {
        return kid;
    }

    @Operation(summary = "Add a kid")
    @PostMapping
    public Kid createKid(@RequestBody Kid kid) {
        return kidRepository.save(kid);
    }

    @Operation(summary = "Update a kid")
    @PutMapping("/{kidId}")
    public Kid updateKid(@PathVariable("kidId") Kid kid, @RequestBody Kid newKid) {

        kid.setName(newKid.getName());
        kid.setTicketNumber(newKid.getTicketNumber());

        return kidRepository.save(newKid);
    }

    @Operation(summary = "Delete a kid")
    @DeleteMapping("/{kidId}")
    public void deleteKid(@PathVariable("kidId") Long kidId) {
        kidRepository.deleteById(kidId);
    }
}