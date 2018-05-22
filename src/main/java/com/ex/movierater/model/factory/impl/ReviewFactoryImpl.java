package com.ex.movierater.model.factory.impl;

import com.ex.movierater.model.Review;
import com.ex.movierater.model.ReviewDto;
import com.ex.movierater.model.factory.ReviewFactory;
import org.springframework.stereotype.Component;

@Component
public class ReviewFactoryImpl implements ReviewFactory {

    @Override
    public Review fromDto(ReviewDto reviewDto) {
        Review review = new Review();
        review.setAuthor(reviewDto.getAuthor());
        review.setContent(reviewDto.getContent());
        review.setAccepted(reviewDto.isAccepted());

        return review;
    }
}
