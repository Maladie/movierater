package com.ex.movierater.service;

import com.ex.movierater.info.Info;

public interface ReviewService {

    Info getReviews(String title);

    Info getReview(String title, String author);
}
