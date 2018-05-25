package com.ex.movierater.controller;

import com.ex.movierater.info.Info;
import com.ex.movierater.model.ReviewDto;
import com.ex.movierater.service.ReviewService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
public class Reviews {

    private ReviewService reviewService;

    public Reviews(ReviewService reviewService) {
        this.reviewService = reviewService;
    }

    @GetMapping(value = "/movies/{title}/reviews", produces = "application/json")
    public ResponseEntity<Info> getReviewsForTitle(@PathVariable String title) {
        Info info = reviewService.getReviews(title);
        if (info.getHttpStatusCode() == 404) {
            return new ResponseEntity<>(info, HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(info, HttpStatus.OK);
    }

    @GetMapping(value = "/movies/{title}/reviews/{author}", produces = "application/json")
    public ResponseEntity<Info> getReviewsForTitleByAuthor(@PathVariable String title, @PathVariable String author) {
        Info info = reviewService.getReview(title, author);
        if (info.getHttpStatusCode() == 404) {
            return new ResponseEntity<>(info, HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(info, HttpStatus.OK);
    }

    @PostMapping(value = "/movies/reviews/", produces = "application/json", consumes = "application/json")
    public ResponseEntity<Info> addReview(@RequestBody ReviewDto reviewDto) {
        Info info = reviewService.addReview(reviewDto);
        if (info.getHttpStatusCode() == 400 || info.getHttpStatusCode() == 406) {
            return new ResponseEntity<>(info, HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(info, HttpStatus.ACCEPTED);
    }
}
