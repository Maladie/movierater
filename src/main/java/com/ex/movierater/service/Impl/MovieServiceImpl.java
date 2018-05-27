package com.ex.movierater.service.Impl;

import com.ex.movierater.examples.JsonExamples;
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
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.hateoas.Link;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
public class MovieServiceImpl implements MovieService {

    private MovieRepository movieRepository;

    private MovieFactory movieFactory;

    private LinkProvider linkProvider;

    private JsonExamples jsonExamples;

    protected final Logger log = org.slf4j.LoggerFactory.getLogger(getClass());

    @Autowired
    public MovieServiceImpl(MovieRepository movieRepository, MovieFactory movieFactory, LinkProvider linkProvider, JsonExamples jsonExamples) {
        this.movieRepository = movieRepository;
        this.movieFactory = movieFactory;
        this.linkProvider = linkProvider;
        this.jsonExamples = jsonExamples;
    }

    @Override
    public Info persist(MovieDto movieDto) {
        String title = movieDto.getTitle();
        Double rating = movieDto.getRating();
        try {
            validateTitle(title);
            validateRating(rating);
        } catch (InvalidLenghtException e) {
            log.error("Invalid title lenght", e);
            return Info.unsuccesfulInfo("Title should be 3 to 50 characters long", InfoCode.INVALID_TITLE_LENGHT, e);
        } catch (InvalidCharacterException e) {
            log.error("Nonalpha character", e);
            return Info.unsuccesfulInfo("Title can only contain alpha characters", InfoCode.INVALID_CHARACTER, e);
        } catch (InvalidRatingException e) {
            log.error("Incorect rating", e);
            return Info.unsuccesfulInfo("Rating cannot be null, greater than five or below zero", InfoCode.INVALID_RATING, e);
        }
        Optional<Movie> movieByTitle = movieRepository.findByTitle(title);
        if (movieByTitle.isPresent()) {
            log.info("Updating movie ", movieByTitle.get());
            return update(movieDto, movieByTitle.get());
        }
        Movie movie = movieFactory.fromDto(movieDto);
        Movie savedMovie = movieRepository.save(movie);
        linkProvider.generateLinkForMovie(savedMovie);
        log.info("Added movie ", savedMovie);
        return Info.succesfulyCreatedInfo("Movie added to database. Combine provided self link with Http GET method to get movie, DELETE method to remove", InfoCode.MOVIE_ADDED, savedMovie);
    }

    @Override
    public Info allMoviesByRating() {
        Sort sort = new Sort(Sort.Direction.DESC, "totalRating");
        List<Movie> allMovies = movieRepository.findAll(sort);
        if (allMovies.isEmpty()) {
            log.info("Movies not found");
            return Info.notFound("No movies found. Be first to add a movie. Combine PUT method with Content-Type: application/json Header and provided movies link. Example request body in object field.",
                    InfoCode.MOVIES_NOT_FOUND, jsonExamples.getRequestBodyExample("movieexample.json"), linkProvider.getLinkToMovies());
        }
        allMovies.forEach(movie -> linkProvider.generateLinkForMovie(movie));
        log.info("Returning all movies sorted by rating");
        return Info.succesfulInfo("All movies sorted by rating. Combine provided self link of your choice with Http GET method to get movie, DELETE method to remove. GET with review link to get Review.",
                InfoCode.OK, allMovies, linkProvider.getLinkToMovies());
    }

    @Override
    public Info delete(String title) {
        Optional<Movie> movieByTitle = movieRepository.findByTitle(title);
        if (movieByTitle.isPresent()) {
            movieRepository.delete(movieByTitle.get());
            log.info("Movie succesfully removed", movieByTitle.get());
            return Info.succesfulInfo("Movie sucesfuly removed from the database. Combine provided link with GET method to go back to movies or PUT method with Content-Type: application/json Header to add or update movie. Example request body in object field.",
                    InfoCode.MOVIE_REMOVED, jsonExamples.getRequestBodyExample("movieexample.json"), linkProvider.getLinkToMovies());
        }
        return Info.notFound("Movie not found. Combine provided link with GET method to go back to movies or PUT method with Content-Type: application/json Header to add or update movie. Example request body in object field.",
                InfoCode.MOVIES_NOT_FOUND, jsonExamples.getRequestBodyExample("movieexample.json"), linkProvider.getLinkToMovies());
    }

    @Override
    public Info get(String title) {
        Optional<Movie> movieByTitle = movieRepository.findByTitle(title);
        if (movieByTitle.isPresent()) {
            final Movie movie = movieByTitle.get();
            linkProvider.generateLinkForMovie(movie);
            final Link link = linkProvider.generateLinkForReviews(title);
            movie.add(link);
            log.info("Movie found", title);
            return Info.succesfulInfo("Movie found. Combine provided self link with Http GET method to get movie, DELETE method to remove. Combine movies link with GET to go back to movies. GET with review link to get Review",
                    InfoCode.OK, movie, linkProvider.getLinkToMovies());
        }
        log.info("Movie not found", title);
        return Info.notFound("Movie not found. Combine provided link with GET method to go back to movies or PUT method with Content-Type: application/json Header to add or update movie. Example request body in object field.",
                InfoCode.MOVIES_NOT_FOUND, jsonExamples.getRequestBodyExample("movieexample.json"), linkProvider.getLinkToMovies());
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
        log.info("Calculating new total score");
        calculateNewTotalScore(movieDto, movie);

        Movie savedMovie = movieRepository.save(movie);
        linkProvider.generateLinkForMovie(savedMovie);
        return Info.succesfulyPatchedInfo("Movie rating and cast updated. Combine movies link with GET to go back to movies. GET with review link to get Review", InfoCode.MOVIE_UPDATED, savedMovie);

    }

    private void calculateNewTotalScore(MovieDto movieDto, Movie movie) {
        MovieUtil.calculateNewTotalRating(movieDto, movie);
    }
}
