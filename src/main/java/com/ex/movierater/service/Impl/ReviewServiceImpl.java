package com.ex.movierater.service.Impl;

import com.ex.movierater.exception.InvalidCharacterException;
import com.ex.movierater.exception.InvalidLenghtException;
import com.ex.movierater.info.Info;
import com.ex.movierater.info.InfoCode;
import com.ex.movierater.links.LinkProvider;
import com.ex.movierater.model.Movie;
import com.ex.movierater.model.Review;
import com.ex.movierater.model.ReviewDto;
import com.ex.movierater.repository.MovieRepository;
import com.ex.movierater.service.ReviewService;
import com.ex.movierater.util.MovieUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.Set;

@Service
public class ReviewServiceImpl implements ReviewService {

    private MovieRepository movieRepository;

    private LinkProvider linkProvider;

    @Autowired
    public ReviewServiceImpl(MovieRepository movieRepository, LinkProvider linkProvider) {
        this.movieRepository = movieRepository;
        this.linkProvider = linkProvider;
    }

    @Override
    public Info getReviews(String title) {
        try {
            validateTitle(title);
        } catch (InvalidLenghtException e) {
            return Info.unsuccesfulInfo("Title should be 3 to 50 characters long", InfoCode.INVALID_TITLE_LENGHT, e);
        } catch (InvalidCharacterException e) {
            return Info.unsuccesfulInfo("Title can only contain alpha characters", InfoCode.INVALID_CHARACTER, e);
        }
        Optional<Movie> movieByTitle = movieRepository.findByTitle(title);
        if (movieByTitle.isPresent()) {
            Set<Review> reviews = movieByTitle.get().getReviews();
            if (!reviews.isEmpty()) {
                reviews.forEach(review -> linkProvider.generateLinkForReviews(review, title));
                return Info.succesfulInfo(String.format("Reviews for movie: %s", title), InfoCode.OK, reviews);
            }
        }
        return Info.notFound(String.format("Could not find any reviews for movie: %s", title), InfoCode.REVIEWS_NOT_FOUND, title);
    }

    @Override
    public Info getReview(String title, String author) {
        try {
            validateTitle(title);
        } catch (InvalidLenghtException e) {
            return Info.unsuccesfulInfo("Title should be 3 to 50 characters long", InfoCode.INVALID_TITLE_LENGHT, e);
        } catch (InvalidCharacterException e) {
            return Info.unsuccesfulInfo("Title can only contain alpha characters", InfoCode.INVALID_CHARACTER, e);
        }
        Optional<Movie> movieByTitle = movieRepository.findByTitle(title);
        if (movieByTitle.isPresent()) {
            Set<Review> reviews = movieByTitle.get().getReviews();
            Optional<Review> reviewByAuthor = reviews.stream().filter(review -> review.getAuthor().equals(author)).findFirst();
            if (reviewByAuthor.isPresent()) {
                linkProvider.generateLinkForReviews(reviewByAuthor.get(), title);
                return Info.succesfulInfo(String.format("Review for movie: %s by $s", title, author), InfoCode.OK, reviewByAuthor.get());
            }
        }
        return Info.notFound(String.format("Could not find any reviews for movie: %s by %s", title, author), InfoCode.REVIEWS_NOT_FOUND, title);
    }

    @Override
    public Info addReview(ReviewDto reviewDto) {
        return null;
    }

    private void validateTitle(String title) throws InvalidCharacterException, InvalidLenghtException {
        MovieUtil.validateTitle(title);
    }
}
