
package com.playground;

import com.playground.model.Attraction;
import com.playground.model.Kid;
import com.playground.model.PlaySite;
import com.playground.repository.AttractionRepository;
import com.playground.repository.KidRepository;
import com.playground.repository.PlaySiteRepository;
import java.util.List;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class LoadDatabase {

    @Bean
    CommandLineRunner initDatabase(AttractionRepository attractionRepository, PlaySiteRepository playSiteRepository,
            KidRepository kidRepository) {
        return args -> {
            attractionRepository.save(new Attraction("Swing"));
            attractionRepository.save(new Attraction("Carousel"));
            attractionRepository.save(new Attraction("Slide"));
            attractionRepository.save(new Attraction("Ball Pit"));

            kidRepository.save(new Kid("Tim", 1, 10));
            kidRepository.save(new Kid("John", 2, 8));

            List<Attraction> attractions = attractionRepository.findAll();

            playSiteRepository.save(new PlaySite("PlaySite 1", attractions));
        };
    }

}