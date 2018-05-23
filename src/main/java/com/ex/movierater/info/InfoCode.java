package com.ex.movierater.info;

public enum InfoCode {

    OK(200000L),
    MOVIE_REMOVED(200001L),
    MOVIE_UPDATED(200001L),
    MOVIE_ADDED(201000L),
    INVALID_RATING(400001L),
    INVALID_TITLE_LENGHT(4000002L),
    INVALID_CHARACTER(4000003L),
    MOVIES_NOT_FOUND(404000L),
    REVIEWS_NOT_FOUND(404001L);

    private long value;

    InfoCode(Long value) {
        this.value = value;
    }

    public long getValue() {
        return value;
    }
}
