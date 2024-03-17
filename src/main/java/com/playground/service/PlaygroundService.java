package com.playground.service;

import com.playground.exception.NotWaitingException;
import com.playground.model.Kid;
import com.playground.model.PlaySite;
import com.playground.repository.KidRepository;
import com.playground.repository.PlaySiteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PlaygroundService {

    private final KidRepository kidRepository;
    private final PlaySiteRepository playSiteRepository;

    @Autowired
    public PlaygroundService(KidRepository kidRepository, PlaySiteRepository playSiteRepository) {
        this.kidRepository = kidRepository;
        this.playSiteRepository = playSiteRepository;
    }

    public void removeKidFromPlaySite(PlaySite playSite, Kid kid) {
        playSite.getKids().remove(kid);
        playSite.getQueue().remove(kid);
        playSiteRepository.save(playSite);
    }

    public void addKidToPlaySite(PlaySite playSite, Kid kid) {
        if (playSite.isFull()) {
            if (kid.acceptsWaitingInQueue()) {
                playSite.getQueue().add(kid);
            } else {
                throw new NotWaitingException("Kid does not accept waiting in queue");
            }
        } else {
            playSite.getKids().add(kid);
        }

        playSiteRepository.save(playSite);
    }

    public double getPlaySiteUtilization(PlaySite playSite) {
        int totalCapacity = playSite.getAttractions().stream()
                .mapToInt(attraction -> attraction.getCapacity())
                .sum();

        int currentOccupancy = playSite.getKids().size();
        return (double) currentOccupancy / totalCapacity * 100;
    }

    public int getTotalVisitorCount() {

        return playSiteRepository.findAll().stream()
                .mapToInt(playSite -> playSite.getKids().size())
                .sum();
    }
}