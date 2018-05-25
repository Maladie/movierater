package com.ex.movierater.messaging;

import com.ex.movierater.model.ReviewDto;

public interface ReviewPublisher {
    void publish(ReviewDto reviewDto);
}
