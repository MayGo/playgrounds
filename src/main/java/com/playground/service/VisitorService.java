
package com.playground.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import com.playground.repository.PlaySiteRepository;

@Service
public class VisitorService {

    private final PlaySiteRepository playSiteRepository;

    @Autowired
    public VisitorService(PlaySiteRepository playSiteRepository) {
        this.playSiteRepository = playSiteRepository;
    }

    @Scheduled(cron = "0 0 0 * * ?") // this runs at midnight every day
    public void resetVisitorCount() {
        playSiteRepository.findAll().forEach(playSite -> {
            playSite.setTotalVisitorCount(0);
            playSiteRepository.save(playSite);
        });
    }
}