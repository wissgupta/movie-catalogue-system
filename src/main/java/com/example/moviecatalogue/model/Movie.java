package com.example.moviecatalogue.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;

@Entity
public class Movie {

    @Id
    private Long id;

    private String title;

    @Column(length = 2000) // ✅ Increase limit to avoid overflow
    private String overview;

    private String posterPath;
    private String backdropPath;
    private String releaseDate;
    private double rating;

    @Column(length = 1000)
    private String tagline;

    // Getters and setters
    public Movie() {}

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getOverview() { return overview; }
    public void setOverview(String overview) { this.overview = overview; }

    public String getPosterPath() { return posterPath; }
    public void setPosterPath(String posterPath) { this.posterPath = posterPath; }

    public String getBackdropPath() { return backdropPath; }
    public void setBackdropPath(String backdropPath) { this.backdropPath = backdropPath; }

    public String getReleaseDate() { return releaseDate; }
    public void setReleaseDate(String releaseDate) { this.releaseDate = releaseDate; }

    public double getRating() { return rating; }
    public void setRating(double rating) { this.rating = rating; }

    public String getTagline() { return tagline; }
    public void setTagline(String tagline) { this.tagline = tagline; }
}
