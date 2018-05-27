package com.ex.movierater.controller;

import com.ex.movierater.info.Info;
import com.ex.movierater.model.ReviewDto;
import com.ex.movierater.service.ReviewService;
import org.springframework.web.bind.annotation.*;

@RestController
public class Reviews {

    private ReviewService reviewService;

    public Reviews(ReviewService reviewService) {
        this.reviewService = reviewService;
    }

    @GetMapping(value = "/movies/{title}/reviews", produces = "application/json")
    public Info getReviewsForTitle(@PathVariable String title) {
        return reviewService.getReviews(title);
    }

    @GetMapping(value = "/movies/{title}/reviews/{author}", produces = "application/json")
    public Info getReviewsForTitleByAuthor(@PathVariable String title, @PathVariable String author) {
        return reviewService.getReview(title, author);
    }

    @PostMapping(value = "/movies/{title}/reviews", produces = "application/json", consumes = "application/json")
    public Info addReview(@PathVariable String title, @RequestBody ReviewDto reviewDto) {
        reviewDto.setTitle(title);
        return reviewService.addReview(reviewDto);

    }
}
