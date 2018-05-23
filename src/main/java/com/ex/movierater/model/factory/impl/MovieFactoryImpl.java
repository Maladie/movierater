package com.ex.movierater.model.factory.impl;

import com.ex.movierater.model.Movie;
import com.ex.movierater.model.MovieDto;
import com.ex.movierater.model.factory.MovieFactory;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.Collections;

@Component
public class MovieFactoryImpl implements MovieFactory {

    @Override
    public Movie fromDto(MovieDto movieDto) {

        Movie movie = new Movie();
        movie.setTitle(movieDto.getTitle());
        movie.setDirector(movieDto.getDirector() != null ? movieDto.getDirector() : "");
        movie.setActors(movieDto.getActors() != null ? movieDto.getActors() : Collections.emptySet());
        movie.setTotalRating(movieDto.getRating() != null ? movieDto.getRating() : 0.0);
        movie.setVotes(1L);
        movie.setCreationDate(LocalDateTime.now());
        movie.setReviews(Collections.emptySet());

        return movie;
    }
}
