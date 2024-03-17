package com.playground.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.playground.model.Attraction;

public interface AttractionRepository extends JpaRepository<Attraction, Long> {

}