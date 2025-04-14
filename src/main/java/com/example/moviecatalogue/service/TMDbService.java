package com.example.moviecatalogue.service;

import com.example.moviecatalogue.model.Movie;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.json.*;

import java.util.ArrayList;
import java.util.List;

@Service
public class TMDbService {

    @Value("${tmdb.api.key}")
    private String apiKey;

    private final String TMDB_BASE = "https://api.themoviedb.org/3";

    private final RestTemplate restTemplate = new RestTemplate();

    public List<Movie> getTrendingMovies() {
        String url = TMDB_BASE + "/trending/movie/week?api_key=" + apiKey;
        return fetchMoviesFromUrl(url);
    }

    public List<Movie> searchMovies(String query) {
        String url = TMDB_BASE + "/search/movie?query=" + query + "&api_key=" + apiKey;
        return fetchMoviesFromUrl(url);
    }

    public Movie getMovieDetails(Long id) {
        String url = TMDB_BASE + "/movie/" + id + "?api_key=" + apiKey;
        String json = restTemplate.getForObject(url, String.class);
        JSONObject obj = new JSONObject(json);

        Movie movie = new Movie();
        movie.setId(obj.getLong("id"));
        movie.setTitle(obj.getString("title"));
        movie.setOverview(obj.getString("overview"));
        movie.setPosterPath(obj.getString("poster_path"));
        movie.setBackdropPath(obj.optString("backdrop_path", ""));
        movie.setReleaseDate(obj.getString("release_date"));
        movie.setRating(obj.getDouble("vote_average"));
        movie.setTagline(obj.optString("tagline", ""));
        return movie;
    }

    public List<Movie> getMoviesByGenre(int genreId) {
        String url = TMDB_BASE + "/discover/movie?api_key=" + apiKey + "&with_genres=" + genreId;
        return fetchMoviesFromUrl(url);
    }

    public String getMovieTrailerUrl(Long id) {
        String url = TMDB_BASE + "/movie/" + id + "/videos?api_key=" + apiKey;
        String json = restTemplate.getForObject(url, String.class);
        JSONArray results = new JSONObject(json).getJSONArray("results");

        for (int i = 0; i < results.length(); i++) {
            JSONObject video = results.getJSONObject(i);
            String type = video.getString("type");
            String site = video.getString("site");

            if ("Trailer".equalsIgnoreCase(type) && "YouTube".equalsIgnoreCase(site)) {
                String key = video.getString("key");
                return "https://www.youtube.com/embed/" + key; // ✅ embed format
            }
        }
        return null;
    }

    public List<String> getMovieReviews(Long id) {
        String url = TMDB_BASE + "/movie/" + id + "/reviews?api_key=" + apiKey;
        String json = restTemplate.getForObject(url, String.class);
        JSONArray results = new JSONObject(json).getJSONArray("results");

        List<String> reviews = new ArrayList<>();
        for (int i = 0; i < results.length(); i++) {
            JSONObject review = results.getJSONObject(i);
            reviews.add(review.getString("content"));
        }
        return reviews;
    }

    private List<Movie> fetchMoviesFromUrl(String url) {
        String json = restTemplate.getForObject(url, String.class);
        JSONArray results = new JSONObject(json).getJSONArray("results");

        List<Movie> movies = new ArrayList<>();
        for (int i = 0; i < results.length(); i++) {
            JSONObject obj = results.getJSONObject(i);
            Movie movie = new Movie();
            movie.setId(obj.getLong("id"));
            movie.setTitle(obj.getString("title"));
            movie.setOverview(obj.getString("overview"));
            movie.setPosterPath(obj.optString("poster_path", ""));
            movie.setBackdropPath(obj.optString("backdrop_path", ""));
            movie.setReleaseDate(obj.optString("release_date", ""));
            movie.setRating(obj.optDouble("vote_average", 0));
            movies.add(movie);
        }
        return movies;
    }
}
