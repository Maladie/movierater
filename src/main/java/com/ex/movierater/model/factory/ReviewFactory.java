package com.ex.movierater.model.factory;

import com.ex.movierater.model.Review;
import com.ex.movierater.model.ReviewDto;

public interface ReviewFactory {
    Review fromDto(ReviewDto reviewDto);
}
