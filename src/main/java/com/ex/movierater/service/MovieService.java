package com.ex.movierater.service;

import com.ex.movierater.info.Info;
import com.ex.movierater.model.MovieDto;

public interface MovieService {

    Info persist(MovieDto movie);

    Info allMoviesByRating();

    Info delete(String movieTitle);

    Info get(String title);

}
