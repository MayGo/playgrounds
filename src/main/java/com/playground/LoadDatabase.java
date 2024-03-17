
package com.playground;

import com.playground.model.Attraction;
import com.playground.repository.AttractionRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class LoadDatabase {

    @Bean
    CommandLineRunner initDatabase(AttractionRepository repository) {
        return args -> {

            repository.save(new Attraction("Swing"));
            repository.save(new Attraction("Carousel"));
            repository.save(new Attraction("Slide"));
            repository.save(new Attraction("Ball Pit"));
        };
    }
}