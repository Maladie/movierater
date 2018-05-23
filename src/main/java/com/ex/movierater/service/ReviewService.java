package com.ex.movierater.service;

import com.ex.movierater.info.Info;
import com.ex.movierater.model.ReviewDto;

public interface ReviewService {

    Info getReviews(String title);

    Info getReview(String title, String author);

    Info addReview(ReviewDto reviewDto);
}
