package com.ex.movierater.service.Impl;

import com.ex.movierater.exception.InvalidCharacterException;
import com.ex.movierater.exception.InvalidLenghtException;
import com.ex.movierater.info.Info;
import com.ex.movierater.info.InfoCode;
import com.ex.movierater.links.LinkProvider;
import com.ex.movierater.messaging.ReviewPublisher;
import com.ex.movierater.model.Movie;
import com.ex.movierater.model.Review;
import com.ex.movierater.model.ReviewDto;
import com.ex.movierater.model.factory.ReviewFactory;
import com.ex.movierater.repository.MovieRepository;
import com.ex.movierater.service.ReviewService;
import com.ex.movierater.util.MovieUtil;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.Set;

@Service
public class ReviewServiceImpl implements ReviewService {

    private MovieRepository movieRepository;

    private LinkProvider linkProvider;

    private ReviewFactory reviewFactory;

    private ReviewPublisher reviewPublisher;

    protected final Logger log = org.slf4j.LoggerFactory.getLogger(getClass());

    @Autowired
    public ReviewServiceImpl(MovieRepository movieRepository, LinkProvider linkProvider, ReviewFactory reviewFactory, ReviewPublisher reviewPublisher) {
        this.movieRepository = movieRepository;
        this.linkProvider = linkProvider;
        this.reviewFactory = reviewFactory;
        this.reviewPublisher = reviewPublisher;
    }

    @Override
    public Info getReviews(String title) {
        Info titleInfo = validateTitleInfo(title);
        if (!titleInfo.getInfoCode().equals(InfoCode.EMPTY)) {
            return titleInfo;
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
        Info titleInfo = validateTitleInfo(title);
        if (!titleInfo.getInfoCode().equals(InfoCode.EMPTY)) {
            return titleInfo;
        }
        Optional<Movie> movieByTitle = movieRepository.findByTitle(title);
        if (movieByTitle.isPresent()) {
            Set<Review> reviews = movieByTitle.get().getReviews();
            Optional<Review> reviewByAuthor = reviews.stream().filter(review -> review.getAuthor().equals(author)).findFirst();
            if (reviewByAuthor.isPresent()) {
                linkProvider.generateLinkForReviews(reviewByAuthor.get(), title);
                return Info.succesfulInfo(String.format("Review for movie: %s by %s", title, author), InfoCode.OK, reviewByAuthor.get());
            }
        }
        return Info.notFound(String.format("Could not find any reviews for movie: %s by %s", title, author), InfoCode.REVIEWS_NOT_FOUND, title);
    }

    @Override
    public Info addReview(ReviewDto reviewDto) {
        final String title = reviewDto.getTitle();
        final String author = reviewDto.getAuthor();
        Info titleInfo = validateTitleInfo(title);
        if (!titleInfo.getInfoCode().equals(InfoCode.EMPTY)) {
            return titleInfo;
        }
        Optional<Movie> movieByTitle = movieRepository.findByTitle(title);
        if (!movieByTitle.isPresent()) {
            return Info.notFound("Movie not found", InfoCode.MOVIES_NOT_FOUND, title);
        }
        final Movie movie = movieByTitle.get();
        Set<Review> reviews = movieByTitle.get().getReviews();
        Optional<Review> reviewByAuthor = reviews.stream().filter(review -> review.getAuthor().equals(author)).findFirst();
        if (reviewByAuthor.isPresent()) {
            final Info.InfoBuilder builder = new Info.InfoBuilder();
            return builder.setDescription("Review for this movie already added").setHttpStatusCode(406L).setInfoCode(InfoCode.REVIEW_ALREADY_EXISTS).build();
        }
        return sendToVerification(movie, reviewDto);
    }

    private Info validateTitleInfo(String title) {
        try {
            validateTitle(title);
        } catch (InvalidLenghtException e) {
            log.error("Invalid title lenght", e);
            return Info.unsuccesfulInfo("Title should be 3 to 50 characters long", InfoCode.INVALID_TITLE_LENGHT, e);
        } catch (InvalidCharacterException e) {
            log.error("Nonalpha character", e);
            return Info.unsuccesfulInfo("Title can only contain alpha characters", InfoCode.INVALID_CHARACTER, e);
        }
        return Info.empty();
    }

    private Info sendToVerification(Movie movie, ReviewDto reviewDto) {
        reviewPublisher.publish(reviewDto);
        log.info("Review sent to verification " + reviewDto);
        linkProvider.generateLinkForMovie(movie);
        final Info.InfoBuilder builder = new Info.InfoBuilder();

        return builder.setDescription("Review added. It will be visible after verification. Combine GET method with provided link to Movie Resource to check if it has been accepted.").setHttpStatusCode(202L).setInfoCode(InfoCode.REVIEW_ADDED).setObject(movie).build();
    }


    public void updateReview(ReviewDto reviewDto) {
        final String title = reviewDto.getTitle();
        final Review review = reviewFactory.fromDto(reviewDto);
        final Optional<Movie> byTitle = movieRepository.findByTitle(title);
        byTitle.ifPresent(movie -> {
                    movie.updateReview(review);
                    movieRepository.save(movie);
                }
        );
    }

    private void validateTitle(String title) throws InvalidCharacterException, InvalidLenghtException {
        MovieUtil.validateTitle(title);
    }
}
