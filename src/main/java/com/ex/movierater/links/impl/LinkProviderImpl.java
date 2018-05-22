package com.ex.movierater.links.impl;

import com.ex.movierater.links.LinkProvider;
import com.ex.movierater.model.Movie;
import com.ex.movierater.model.Review;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.mvc.ControllerLinkBuilder;
import org.springframework.stereotype.Service;

@Service
public class LinkProviderImpl implements LinkProvider {

    public void generateLinkForMovie(Movie movie) {
        movie.getReviews().forEach(review -> generateLinkForReviews(review, movie.getTitle()));
        Link link = ControllerLinkBuilder.linkTo(Movie.class).slash("movies").slash(movie.getTitle()).withSelfRel();
        movie.getLinks().add(link);
    }

    public void generateLinkForReviews(Review review, String title) {
        if (review.isAccepted()) {
            Link link = ControllerLinkBuilder.linkTo(Movie.class).slash("movies").slash(title).slash(review.getAuthor()).withSelfRel();
            review.getLinks().add(link);
        }
    }
}
