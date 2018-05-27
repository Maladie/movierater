package com.ex.movierater.controller;

import com.ex.movierater.info.Info;
import com.ex.movierater.model.MovieDto;
import com.ex.movierater.service.MovieService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "/")
public class Movies {

    private MovieService movieService;

    @Autowired
    public Movies(MovieService movieService) {
        this.movieService = movieService;
    }

    @GetMapping(value = "movies", produces = "application/json")
    public Info retrieveMovies() {
        return movieService.allMoviesByRating();
    }

    @GetMapping(value = "movies/{title}", produces = "application/json")
    public Info getMovie(@PathVariable String title) {
        return movieService.get(title);
    }

    @PutMapping(value = "movies", consumes = "application/json", produces = "application/json")
    public Info add(@RequestBody MovieDto movie) {
        return movieService.persist(movie);
    }

    @DeleteMapping(value = "movies/{title}", produces = "application/json")
    public Info delete(@PathVariable String title) {
        return movieService.delete(title);
    }

}
