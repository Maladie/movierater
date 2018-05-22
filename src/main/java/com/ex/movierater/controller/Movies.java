package com.ex.movierater.controller;

import com.ex.movierater.info.Info;
import com.ex.movierater.model.MovieDto;
import com.ex.movierater.service.MovieService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
    public ResponseEntity<Info> retrieveMovies() {
        Info info = movieService.allMoviesByRating();
        if (info.getHttpStatusCode() == 404) {
            return new ResponseEntity<>(info, HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(info, HttpStatus.OK);
    }

    @GetMapping(value = "movies/{title}", produces = "application/json")
    public ResponseEntity<Info> getMovie(@PathVariable String title) {
        Info info = movieService.get(title);
        if (info.getHttpStatusCode() == 404) {
            return new ResponseEntity<>(info, HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(info, HttpStatus.OK);
    }

    @PutMapping(value = "movie", consumes = "application/json", produces = "application/json")
    public ResponseEntity<Info> add(@RequestBody MovieDto movie) {
        Info info = movieService.persist(movie);
        if (info.getHttpStatusCode() == 400) {
            return new ResponseEntity<>(info, HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(info, HttpStatus.CREATED);
    }

    @DeleteMapping(value = "movies/{title}", consumes = "application/json", produces = "application/json")
    public ResponseEntity<Info> delete(@PathVariable String title) {
        Info info = movieService.delete(title);
        if (info.getHttpStatusCode() == 404) {
            return new ResponseEntity<>(info, HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(info, HttpStatus.OK);
    }

}
