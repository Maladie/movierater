package com.ex.movierater.util;

import com.ex.movierater.exception.InvalidCharacterException;
import com.ex.movierater.exception.InvalidLenghtException;
import com.ex.movierater.exception.InvalidRatingException;
import com.ex.movierater.model.Movie;
import com.ex.movierater.model.MovieDto;
import org.apache.commons.lang3.StringUtils;

public class MovieUtil {

    public static void calculateNewTotalRating(MovieDto movieDto, Movie movie) {
        Double rating = movieDto.getRating();
        Double totalRating = movie.getTotalRating();
        long votes = movie.getVotes();
        totalRating = (totalRating * votes + rating) / ++votes;

        movie.setVotes(votes);
        movie.setTotalRating(totalRating);

    }

    public static void validateTitle(String title) throws InvalidLenghtException, InvalidCharacterException {
        if (title == null) {
            throw new InvalidLenghtException();
        }
        int length = title.length();
        if (length < 3 || length > 50) {
            throw new InvalidLenghtException();
        }
        if (!StringUtils.isAlphaSpace(title)) {
            throw new InvalidCharacterException();
        }
    }

    public static void validateRating(Double rating) throws InvalidRatingException {
        if (rating == null || movieRatingGreaterThanFive(rating) || movieRatingLessThanZero(rating)) {
            throw new InvalidRatingException();
        }
    }

    private static boolean movieRatingGreaterThanFive(Double rating) {
        return rating.compareTo(5.0) >= 1;
    }

    private static boolean movieRatingLessThanZero(Double rating) {
        return rating.compareTo(0.0) < 0;
    }
}
