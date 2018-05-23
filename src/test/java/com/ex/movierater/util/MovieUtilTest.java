package com.ex.movierater.util;

import com.ex.movierater.exception.InvalidCharacterException;
import com.ex.movierater.exception.InvalidLenghtException;
import com.ex.movierater.exception.InvalidRatingException;
import com.ex.movierater.model.Movie;
import com.ex.movierater.model.MovieDto;
import org.junit.Assert;
import org.junit.Test;

public class MovieUtilTest {

    @Test
    public void calculateNewTotalRatingTest() throws Exception {
        double actual = calculateNewTotalRatingTest(1.0, 3.0, 10);
        double expected = 2.8182;

        Assert.assertEquals(actual, expected, 2);
    }

    private Double calculateNewTotalRatingTest(Double newRating, Double totalRating, long votes) {
        Movie movie = new Movie();
        movie.setVotes(votes);
        movie.setTotalRating(totalRating);

        MovieDto movieDto = new MovieDto();
        movieDto.setRating(newRating);

        MovieUtil.calculateNewTotalRating(movieDto, movie);

        return movie.getTotalRating();
    }

    @Test(expected = InvalidLenghtException.class)
    public void validateTitleShouldThrowInvalidLenghtExceptionWhenTitleIsTooShort() throws Exception, InvalidCharacterException, InvalidLenghtException {
        String title = "";
        MovieUtil.validateTitle(title);
    }

    @Test(expected = InvalidLenghtException.class)
    public void validateTitleShouldThrowInvalidLenghtExceptionWhenTitleIsTooLong() throws Exception, InvalidCharacterException, InvalidLenghtException {
        String title = "LOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOONG";
        MovieUtil.validateTitle(title);
    }

    @Test(expected = InvalidCharacterException.class)
    public void validateTitleShouldThrowInvalidCharacterExceptionWhenTitleContainsNumericSymbols() throws Exception, InvalidCharacterException, InvalidLenghtException {
        String title = "Alpha3";
        MovieUtil.validateTitle(title);
    }

    @Test(expected = InvalidRatingException.class)
    public void validateRatingShouldThrowExceptionWhenRatingIsNull() throws Exception, InvalidRatingException {
        Double rating = null;
        MovieUtil.validateRating(rating);
    }

    @Test(expected = InvalidRatingException.class)
    public void validateRatingShouldThrowExceptionWhenRatingIsGreaterThanFive() throws Exception, InvalidRatingException {
        Double rating = 5.1;
        MovieUtil.validateRating(rating);
    }

    @Test(expected = InvalidRatingException.class)
    public void validateRatingShouldThrowExceptionWhenRatingIsLessThanZero() throws Exception, InvalidRatingException {
        Double rating = -0.1;
        MovieUtil.validateRating(rating);
    }

}