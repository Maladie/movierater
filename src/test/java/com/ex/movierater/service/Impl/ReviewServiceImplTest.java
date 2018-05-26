package com.ex.movierater.service.Impl;

import com.ex.movierater.exception.InvalidCharacterException;
import com.ex.movierater.exception.InvalidLenghtException;
import com.ex.movierater.info.Info;
import com.ex.movierater.info.InfoCode;
import com.ex.movierater.links.LinkProvider;
import com.ex.movierater.model.Movie;
import com.ex.movierater.model.Review;
import com.ex.movierater.repository.MovieRepository;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import java.util.Collections;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

public class ReviewServiceImplTest {

    @Mock
    private MovieRepository movieRepository;

    @Mock
    private LinkProvider linkProvider;

    @InjectMocks
    private ReviewServiceImpl reviewService;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void getReviewsShouldReturnUnsucesfulInfoWhenTitleIsTooShort() throws Exception {
        //given
        String title = "";
        final Info expected = Info.unsuccesfulInfo("Title should be 3 to 50 characters long", InfoCode.INVALID_TITLE_LENGHT, InvalidLenghtException.class);

        getReviewsShouldReturnUnsucesfulInfoWhenTitleValidationFails(title, expected);
    }

    @Test
    public void getReviewsShouldReturnUnsucesfulInfoWhenTitleIsTooLong() throws Exception {
        //given
        String title = "Dluuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuugi Tytuł";
        final Info expected = Info.unsuccesfulInfo("Title should be 3 to 50 characters long", InfoCode.INVALID_TITLE_LENGHT, InvalidLenghtException.class);

        getReviewsShouldReturnUnsucesfulInfoWhenTitleValidationFails(title, expected);
    }

    @Test
    public void getReviewsShouldReturnUnsucesfulInfoWhenTitleContainsNonAlphaCharacters() throws Exception {
        String title = "Terminator 85";
        final Info expected = Info.unsuccesfulInfo("Title can only contain alpha characters", InfoCode.INVALID_CHARACTER, new InvalidCharacterException());

        getReviewsShouldReturnUnsucesfulInfoWhenTitleValidationFails(title, expected);
    }

    @Test
    public void getReviewsShouldReturnSuccesfulInfoWhenReviewsFound() {
        String title = "Movie";
        Movie movie = Mockito.mock(Movie.class);
        final Set<Review> reviews = new HashSet<>();
        final Review review = new Review();
        review.setAuthor("Author");
        reviews.add(review);
        final Info expected = Info.succesfulInfo(String.format("Reviews for movie: %s", title), InfoCode.OK, reviews);

        testGetReviews(title, Optional.of(movie), reviews, expected);
    }

    @Test
    public void getReviewsShouldReturnUnsuccesfulInfoWhenMovieNotFound() {
        String title = "Movie";
        final Info expected = Info.notFound(String.format("Could not find any reviews for movie: %s", title), InfoCode.REVIEWS_NOT_FOUND, title);
        final Review review = new Review();
        review.setAuthor("Author");
        final Set<Review> reviews = new HashSet<>();
        reviews.add(review);

        testGetReviews(title, Optional.empty(), reviews, expected);
    }

    @Test
    public void getReviewsShouldReturnUnsuccesfulInfoWhenMovieWithoutReviewsFound() {
        String title = "Movie";
        Movie movie = Mockito.mock(Movie.class);
        final Info expected = Info.notFound(String.format("Could not find any reviews for movie: %s", title), InfoCode.REVIEWS_NOT_FOUND, title);
        final Set<Review> reviews = Collections.emptySet();

        testGetReviews(title, Optional.of(movie), reviews, expected);
    }

    private void testGetReviews(String title, Optional<Movie> optionalMovie, Set<Review> reviewSet, Info expected) {
        Mockito.when(movieRepository.findByTitle(title)).thenReturn(optionalMovie);
        optionalMovie.ifPresent(movie -> Mockito.when(movie.getReviews()).thenReturn(reviewSet));

        //when
        final Info actual = reviewService.getReviews(title);

        //then
        Assert.assertTrue(EqualsBuilder.reflectionEquals(actual, expected, "key"));
    }


    private void getReviewsShouldReturnUnsucesfulInfoWhenTitleValidationFails(String title, Info expected) {
        //when
        final Info actual = reviewService.getReviews(title);

        //then
        Assert.assertTrue(EqualsBuilder.reflectionEquals(actual, expected, "key", "object"));
    }

    @Test
    public void getReviewShouldReturnUnsucesfulInfoWhenTitleIsTooShort() throws Exception {
        //given
        String title = "";
        final Info expected = Info.unsuccesfulInfo("Title should be 3 to 50 characters long", InfoCode.INVALID_TITLE_LENGHT, InvalidLenghtException.class);

        getReviewShouldReturnUnsucesfulInfoWhenTitleValidationFails(title, expected);
    }

    @Test
    public void getReviewShouldReturnUnsucesfulInfoWhenTitleIsTooLong() throws Exception {
        //given
        String title = "Dluuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuugi Tytuł";
        final Info expected = Info.unsuccesfulInfo("Title should be 3 to 50 characters long", InfoCode.INVALID_TITLE_LENGHT, InvalidLenghtException.class);

        getReviewShouldReturnUnsucesfulInfoWhenTitleValidationFails(title, expected);
    }

    @Test
    public void getReviewShouldReturnUnsucesfulInfoWhenTitleContainsNonAlphaCharacters() throws Exception {
        String title = "Terminator 85";
        final Info expected = Info.unsuccesfulInfo("Title can only contain alpha characters", InfoCode.INVALID_CHARACTER, new InvalidCharacterException());

        getReviewShouldReturnUnsucesfulInfoWhenTitleValidationFails(title, expected);
    }

    private void getReviewShouldReturnUnsucesfulInfoWhenTitleValidationFails(String title, Info expected) {
        String author = "Author";
        //when
        final Info actual = reviewService.getReview(title, author);

        //then
        Assert.assertTrue(EqualsBuilder.reflectionEquals(actual, expected, "key", "object"));
    }

    @Test
    public void getReviewShouldReturnSuccesfulInfoWhenReviewFound() {
        String title = "Movie";
        Movie movie = Mockito.mock(Movie.class);
        final Review review = new Review();
        review.setAuthor("Hulk");
        final Set<Review> reviews = new HashSet<>();
        reviews.add(review);
        final Info expected = Info.succesfulInfo(String.format("Review for movie: %s by %s", title, "Hulk"), InfoCode.OK, review);

        testGetReview(title, Optional.of(movie), reviews, expected);
    }

    @Test
    public void getReviewShouldReturnUnsuccesfulInfoWhenMovieNotFound() {
        String title = "Movie";
        final Info expected = Info.notFound(String.format("Could not find any reviews for movie: %s by %s", title, "Hulk"), InfoCode.REVIEWS_NOT_FOUND, title);
        final Review review = new Review();
        review.setAuthor("Author");
        final Set<Review> reviews = new HashSet<>();
        reviews.add(review);

        testGetReview(title, Optional.empty(), reviews, expected);
    }

    @Test
    public void getReviewShouldReturnUnsuccesfulInfoWhenMovieWithoutReviewsFound() {
        String title = "Movie";
        Movie movie = Mockito.mock(Movie.class);
        final Info expected = Info.notFound(String.format("Could not find any reviews for movie: %s by %s", title, "Hulk"), InfoCode.REVIEWS_NOT_FOUND, title);
        final Set<Review> reviews = Collections.emptySet();

        testGetReview(title, Optional.of(movie), reviews, expected);
    }

    private void testGetReview(String title, Optional<Movie> optionalMovie, Set<Review> reviewSet, Info expected) {
        String author = "Hulk";
        Mockito.when(movieRepository.findByTitle(title)).thenReturn(optionalMovie);
        optionalMovie.ifPresent(movie -> Mockito.when(movie.getReviews()).thenReturn(reviewSet));

        //when
        final Info actual = reviewService.getReview(title, author);

        //then
        Assert.assertTrue(EqualsBuilder.reflectionEquals(actual, expected, "key", "object"));
    }


}