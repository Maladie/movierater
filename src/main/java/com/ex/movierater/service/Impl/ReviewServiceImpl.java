package com.ex.movierater.service.Impl;

import com.ex.movierater.info.Info;
import com.ex.movierater.repository.MovieRepository;
import com.ex.movierater.service.ReviewService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ReviewServiceImpl implements ReviewService {

    private MovieRepository movieRepository;

    @Autowired
    public ReviewServiceImpl(MovieRepository movieRepository) {
        this.movieRepository = movieRepository;
    }

    @Override
    public Info getReviews(String title) {
        return null;
    }

    @Override
    public Info getReview(String title, String author) {
        return null;
    }
}
