package com.example.moviecatalogue.service;

import com.example.moviecatalogue.model.Movie;
import com.example.moviecatalogue.repository.MovieRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class FavoriteService {

    private final MovieRepository movieRepository;

    public FavoriteService(MovieRepository movieRepository) {
        this.movieRepository = movieRepository;
    }

    public void addFavorite(Movie movie) {
        movieRepository.save(movie);
    }

    public void removeFavorite(Long id) {
        movieRepository.deleteById(id);
    }

    public List<Movie> getFavorites() {
        return movieRepository.findAll();
    }

    public boolean isFavorite(Long id) {
        return movieRepository.existsById(id);
    }
}
