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
    @GetMapping("/{id}")
    public Kid getKid(@PathVariable Long id) {
        return kidRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Kid not found"));
    }

    @Operation(summary = "Add a kid")
    @PostMapping
    public Kid createKid(@RequestBody Kid kid) {
        return kidRepository.save(kid);
    }

    @Operation(summary = "Update a kid")
    @PutMapping("/{id}")
    public Kid updateKid(@PathVariable Long id, @RequestBody Kid newKid) {
        return kidRepository.findById(id)
                .map(kid -> {
                    kid.setName(newKid.getName());
                    return kidRepository.save(kid);
                })
                .orElseGet(() -> {
                    newKid.setId(id);
                    return kidRepository.save(newKid);
                });
    }

    @Operation(summary = "Delete a kid")
    @DeleteMapping("/{id}")
    public void deleteKid(@PathVariable Long id) {
        kidRepository.deleteById(id);
    }
}