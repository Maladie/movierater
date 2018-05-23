package com.ex.movierater.service.Impl;

import com.ex.movierater.exception.InvalidCharacterException;
import com.ex.movierater.exception.InvalidLenghtException;
import com.ex.movierater.exception.InvalidRatingException;
import com.ex.movierater.info.Info;
import com.ex.movierater.info.InfoCode;
import com.ex.movierater.links.LinkProvider;
import com.ex.movierater.model.Movie;
import com.ex.movierater.model.MovieDto;
import com.ex.movierater.model.factory.MovieFactory;
import com.ex.movierater.repository.MovieRepository;
import com.ex.movierater.service.MovieService;
import com.ex.movierater.util.MovieUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
public class MovieServiceImpl implements MovieService {

    private MovieRepository movieRepository;

    private MovieFactory movieFactory;

    private LinkProvider linkProvider;

    @Autowired
    public MovieServiceImpl(MovieRepository movieRepository, MovieFactory movieFactory, LinkProvider linkProvider) {
        this.movieRepository = movieRepository;
        this.movieFactory = movieFactory;
        this.linkProvider = linkProvider;
    }

    @Override
    public Info persist(MovieDto movieDto) {
        String title = movieDto.getTitle();
        Double rating = movieDto.getRating();
        try {
            validateTitle(title);
            validateRating(rating);
        } catch (InvalidLenghtException e) {
            return Info.unsuccesfulInfo("Title should be 3 to 50 characters long", InfoCode.INVALID_TITLE_LENGHT, e);
        } catch (InvalidCharacterException e) {
            return Info.unsuccesfulInfo("Title can only contain alpha characters", InfoCode.INVALID_CHARACTER, e);
        } catch (InvalidRatingException e) {
            return Info.unsuccesfulInfo("Rating cannot be null, greater than five or below zero", InfoCode.INVALID_RATING, e);
        }
        Optional<Movie> movieByTitle = movieRepository.findByTitle(title);
        if (movieByTitle.isPresent()) {
            return update(movieDto, movieByTitle.get());
        }
        Movie movie = movieFactory.fromDto(movieDto);
        Movie savedMovie = movieRepository.save(movie);
        linkProvider.generateLinkForMovie(savedMovie);
        return Info.succesfulyCreatedInfo("Movie added to database", InfoCode.MOVIE_ADDED, savedMovie);
    }

    @Override
    public Info allMoviesByRating() {
        Sort sort = new Sort(Sort.Direction.DESC, "totalRating");
        List<Movie> allMovies = movieRepository.findAll(sort);
        if (allMovies.isEmpty()) {
            return Info.notFound("No movies found", InfoCode.MOVIES_NOT_FOUND, null);
        }
        allMovies.forEach(movie -> linkProvider.generateLinkForMovie(movie));
        return Info.succesfulInfo("All movies sorted by rating", InfoCode.OK, allMovies);
    }

    @Override
    public Info delete(String title) {
        Optional<Movie> movieByTitle = movieRepository.findByTitle(title);
        if (movieByTitle.isPresent()) {
            movieRepository.delete(movieByTitle.get());
            return Info.succesfulInfo("Movie sucesfuly removed from the database", InfoCode.MOVIE_REMOVED, title);
        }
        return Info.notFound("Movie not found", InfoCode.MOVIES_NOT_FOUND, title);
    }

    @Override
    public Info get(String title) {
        Optional<Movie> movieByTitle = movieRepository.findByTitle(title);
        if (movieByTitle.isPresent()) {
            linkProvider.generateLinkForMovie(movieByTitle.get());
            return Info.succesfulInfo("Movie found", InfoCode.OK, movieByTitle);
        }
        return Info.notFound("Movie not found", InfoCode.MOVIES_NOT_FOUND, title);
    }

    private void validateRating(double rating) throws InvalidRatingException {
        MovieUtil.validateRating(rating);
    }

    private void validateTitle(String title) throws InvalidLenghtException, InvalidCharacterException {
        MovieUtil.validateTitle(title);
    }

    private Info update(MovieDto movieDto, Movie movie) {
        Set actorsNew = movieDto.getActors();

        if (actorsNew != null) {
            Set<String> actors = movie.getActors();
            actorsNew.addAll(actors);
            movie.setActors(actorsNew);
        }
        calculateNewTotalScore(movieDto, movie);

        Movie savedMovie = movieRepository.save(movie);
        linkProvider.generateLinkForMovie(movie);
        return Info.succesfulyPatchedInfo("Movie rating and cast updated", InfoCode.MOVIE_UPDATED, savedMovie);

    }

    private void calculateNewTotalScore(MovieDto movieDto, Movie movie) {
        MovieUtil.calculateNewTotalRating(movieDto, movie);
    }
}
