package com.ex.movierater.messaging.impl;

import com.ex.movierater.messaging.ReviewReceiver;
import com.ex.movierater.model.ReviewDto;
import com.ex.movierater.service.ReviewService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class RabbitReviewReceiver implements ReviewReceiver {

    private ReviewService reviewService;

    private ObjectMapper mapper;

    protected final Logger log = org.slf4j.LoggerFactory.getLogger(getClass());

    @Autowired
    public RabbitReviewReceiver(ReviewService reviewService) {
        this.reviewService = reviewService;
        mapper = new ObjectMapper();
    }

    @RabbitListener(queues = "#{queue2.name}")
    public void receive(String reviewJson) throws IOException {
        log.info("Verified review received", reviewJson);
        final ReviewDto reviewDto = mapper.readValue(reviewJson, ReviewDto.class);

        reviewService.updateReview(reviewDto);
    }
}
