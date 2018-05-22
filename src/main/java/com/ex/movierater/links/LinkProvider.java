package com.ex.movierater.links;

import com.ex.movierater.model.Movie;

public interface LinkProvider {
    void generateLinkForMovie(Movie movie);
}
