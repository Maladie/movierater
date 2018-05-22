package com.ex.movierater.model;

import java.util.Set;

public class MovieDto {

    private String title;

    private Double rating;

    private String director;

    private Set actors;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Double getRating() {
        return rating;
    }

    public void setRating(Double rating) {
        this.rating = rating;
    }

    public String getDirector() {
        return director;
    }

    public void setDirector(String director) {
        this.director = director;
    }

    public Set getActors() {
        return actors;
    }

    public void setActors(Set actors) {
        this.actors = actors;
    }
}
