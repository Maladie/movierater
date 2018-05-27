package com.ex.movierater.links;

import com.ex.movierater.model.LinkTO;
import com.ex.movierater.model.Movie;
import com.ex.movierater.model.Review;
import org.springframework.hateoas.Link;

public interface LinkProvider {
    void generateLinkForMovie(Movie movie);

    void generateLinkForReviews(Review review, String title);

    LinkTO getLinkToMovies();

    Link generateLinkForReviews(String title);
}
