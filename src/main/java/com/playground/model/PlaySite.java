package com.playground.model;

import java.util.ArrayList;
import java.util.List;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
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

    @ManyToMany
    @JoinTable(name = "play_site_attractions", joinColumns = @JoinColumn(name = "play_site_id"), inverseJoinColumns = @JoinColumn(name = "attractions_id"))
    private List<Attraction> attractions;

    @OneToMany
    private List<Kid> kids;

    @OneToMany
    private List<Kid> queue;

    private int totalVisitorCount = 0;

    public void addKid(Kid kid) {
        kids.add(kid);
        totalVisitorCount++;
    }

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