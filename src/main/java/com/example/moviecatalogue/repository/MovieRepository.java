package com.example.moviecatalogue.repository;

import com.example.moviecatalogue.model.Movie;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MovieRepository extends JpaRepository<Movie, Long> {
}
