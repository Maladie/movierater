package com.ex.movierater.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.hateoas.ResourceSupport;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Document
public class Movie extends ResourceSupport {

    @Id
    private String title;

    private double totalRating;

    private long votes;

    private String director;

    private Set<String> actors;

    private LocalDateTime creationDate;

    private Set<Review> reviews;

    public Movie() {
    }

    @JsonCreator
    public Movie(String title, double totalRating, long votes, String director, Set<String> actors, LocalDateTime creationDate, Set<Review> reviews) {
        this.title = title;
        this.totalRating = totalRating;
        this.votes = votes;
        this.director = director;
        this.actors = actors;
        this.creationDate = creationDate;
        this.reviews = reviews;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Double getTotalRating() {
        return totalRating;
    }

    public void setTotalRating(Double totalRating) {
        this.totalRating = totalRating;
    }

    public String getDirector() {
        return director;
    }

    public void setDirector(String director) {
        this.director = director;
    }

    public Set<String> getActors() {
        return new HashSet<>(actors);
    }

    public LocalDateTime getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(LocalDateTime creationDate) {
        this.creationDate = creationDate;
    }

    public Set<Review> getReviews() {
        return new HashSet<>(reviews);
    }

    public void setReviews(Set<Review> reviews) {
        this.reviews = reviews;
    }

    public long getVotes() {
        return votes;
    }

    public void setVotes(long votes) {
        this.votes = votes;
    }

    public void setActors(Set<String> actors) {
        this.actors = actors;
    }
}
