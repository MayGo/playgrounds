package com.playground.model;

import java.util.ArrayList;
import java.util.List;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class PlaySite {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    @Size(min = 0, max = 100)
    private String name;

    @OneToMany
    private List<Attraction> attractions;

    @OneToMany
    private List<Kid> kids;

    @OneToMany
    private List<Kid> queue;

    public PlaySite() {
        this.attractions = new ArrayList<>();
        this.kids = new ArrayList<>();
        this.queue = new ArrayList<>();
    }

    public PlaySite(String name, List<Attraction> attractions) {
        this.name = name;
        this.attractions = attractions;
        this.kids = new ArrayList<>();
        this.queue = new ArrayList<>();
    }

    public boolean isFull() {
        int totalCapacity = attractions.stream()
                .mapToInt(Attraction::getCapacity)
                .sum();
        return kids.size() >= totalCapacity;
    }

}