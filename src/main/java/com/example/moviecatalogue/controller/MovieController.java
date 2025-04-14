package com.example.moviecatalogue.controller;

import com.example.moviecatalogue.model.Movie;
import com.example.moviecatalogue.service.FavoriteService;
import com.example.moviecatalogue.service.TMDbService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
public class MovieController {

    private final TMDbService tmdbService;
    private final FavoriteService favoriteService;

    public MovieController(TMDbService tmdbService, FavoriteService favoriteService) {
        this.tmdbService = tmdbService;
        this.favoriteService = favoriteService;
    }

    // Home Page - Trending Movies
    @GetMapping("/")
    public String home(Model model) {
        List<Movie> trendingMovies = tmdbService.getTrendingMovies();
        model.addAttribute("movies", trendingMovies);
        return "index";
    }

    // Search
    @GetMapping("/search")
    public String search(@RequestParam("query") String query, Model model) {
        List<Movie> searchResults = tmdbService.searchMovies(query);
        model.addAttribute("movies", searchResults);
        model.addAttribute("searchQuery", query);
        return "index";
    }

    // Browse by Genre
    @GetMapping("/genre/{genreId}")
    public String moviesByGenre(@PathVariable int genreId, Model model) {
        List<Movie> movies = tmdbService.getMoviesByGenre(genreId);
        model.addAttribute("movies", movies);
        return "index";
    }

    // Movie Details
    @GetMapping("/movie/{id}")
    public String movieDetails(@PathVariable Long id, Model model) {
        Movie movie = tmdbService.getMovieDetails(id);
        String trailerUrl = tmdbService.getMovieTrailerUrl(id);
        List<String> reviews = tmdbService.getMovieReviews(id);

        model.addAttribute("movie", movie);
        model.addAttribute("trailerUrl", trailerUrl);
        model.addAttribute("reviews", reviews);
        model.addAttribute("isFavorite", favoriteService.isFavorite(id));

        return "detail";
    }

    // Add to Favorites
    @PostMapping("/favorite/add")
    public String addFavorite(@ModelAttribute Movie movie) {
        favoriteService.addFavorite(movie);
        return "redirect:/favorites";
    }

    // Remove from Favorites
    @PostMapping("/favorite/remove")
    public String removeFavorite(@RequestParam Long id) {
        favoriteService.removeFavorite(id);
        return "redirect:/favorites";
    }

    // View Favorites
    @GetMapping("/favorites")
    public String favorites(Model model) {
        List<Movie> favoriteMovies = favoriteService.getFavorites();
        model.addAttribute("favorites", favoriteMovies);
        return "favorites";
    }

    // Export Favorites to CSV
    @GetMapping("/favorites/export")
    @ResponseBody
    public String exportFavorites() {
        List<Movie> favorites = favoriteService.getFavorites();
        StringBuilder csv = new StringBuilder("ID,Title,Release Date,Rating\n");
        for (Movie movie : favorites) {
            csv.append(movie.getId()).append(",")
                    .append('"').append(movie.getTitle().replace("\"", "\"\"")).append('"').append(",")
                    .append(movie.getReleaseDate()).append(",")
                    .append(movie.getRating()).append("\n");
        }
        return csv.toString();
    }

    // Watchlist (Placeholder)
    @GetMapping("/watchlist")
    public String watchlist(Model model) {
        model.addAttribute("watchlist", List.of());
        return "watchlist";
    }
}
