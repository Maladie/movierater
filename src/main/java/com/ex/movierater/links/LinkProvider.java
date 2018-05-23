package com.ex.movierater.links;

import com.ex.movierater.model.Movie;
import com.ex.movierater.model.Review;

public interface LinkProvider {
    void generateLinkForMovie(Movie movie);

    void generateLinkForReviews(Review review, String title);
}
