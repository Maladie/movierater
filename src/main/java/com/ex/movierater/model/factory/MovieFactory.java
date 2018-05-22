package com.ex.movierater.model.factory;

import com.ex.movierater.model.Movie;
import com.ex.movierater.model.MovieDto;

public interface MovieFactory {

    Movie fromDto(MovieDto movieDto);

}
