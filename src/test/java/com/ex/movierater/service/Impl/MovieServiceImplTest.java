package com.ex.movierater.service.Impl;

import com.ex.movierater.exception.InvalidLenghtException;
import com.ex.movierater.exception.InvalidRatingException;
import com.ex.movierater.info.Info;
import com.ex.movierater.info.InfoCode;
import com.ex.movierater.links.LinkProvider;
import com.ex.movierater.model.Movie;
import com.ex.movierater.model.MovieDto;
import com.ex.movierater.model.factory.MovieFactory;
import com.ex.movierater.repository.MovieRepository;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.*;
import org.springframework.data.domain.Sort;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

public class MovieServiceImplTest {

    @Mock
    private MovieRepository movieRepository;

    @Mock
    private MovieFactory movieFactory;

    @Mock
    private LinkProvider linkProvider;

    @InjectMocks
    private MovieServiceImpl movieService;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void persistShouldReturnSuccesfulInfoWhenNewMovieIsAdded() throws Exception {
        //given
        Movie savedMovie = Mockito.mock(Movie.class);
        savedMovie.setTitle("Movie");
        Info expected = Info.succesfulyCreatedInfo("Movie added to database", InfoCode.MOVIE_ADDED, savedMovie);

        shouldReturnSuccesfulInfo(Optional.empty(), expected, savedMovie);

    }

    @Test
    public void shouldReturnSuccesfulPatchedInfoWhenMovieIsAlreadyInDataBase() throws Exception {
        Movie savedMovie = Mockito.mock(Movie.class);
        savedMovie.setTitle("Movie");
        final Info expected = Info.succesfulyPatchedInfo("Movie rating and cast updated", InfoCode.MOVIE_UPDATED, savedMovie);

        shouldReturnSuccesfulInfo(Optional.of(savedMovie), expected, savedMovie);
    }

    private void shouldReturnSuccesfulInfo(Optional optionalReturn, Info expected, Movie savedMovie) {
        //given
        MovieDto movieDto = new MovieDto();
        movieDto.setTitle("Movie");
        movieDto.setRating(4.0);
        Mockito.when(movieRepository.findByTitle(ArgumentMatchers.anyString())).thenReturn(optionalReturn);
        Mockito.when(movieFactory.fromDto(ArgumentMatchers.any(MovieDto.class))).thenReturn(savedMovie);
        Mockito.when(movieRepository.save(ArgumentMatchers.any(Movie.class))).thenReturn(savedMovie);

        //when
        final Info actual = movieService.persist(movieDto);

        //then
        Assert.assertTrue(EqualsBuilder.reflectionEquals(actual, expected, "key"));
    }

    @Test
    public void persistShouldReturnUnSuccesfulInfoWhenTitleTooLong() throws Exception {
        String title = "Movieeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeee eeee";
        final Info expected = Info.unsuccesfulInfo("Title should be 3 to 50 characters long", InfoCode.INVALID_TITLE_LENGHT, new InvalidLenghtException());
        double rating = 3.0;
        persistShouldReturnUnsuccesfulInfoWhenTitleValidationFailes(expected, title, rating);
    }

    @Test
    public void persistShouldReturnUnSuccesfulInfoWhenTitleTooShort() throws Exception {
        String title = "";
        final Info expected = Info.unsuccesfulInfo("Title should be 3 to 50 characters long", InfoCode.INVALID_TITLE_LENGHT, new InvalidLenghtException());
        double rating = 3.0;
        persistShouldReturnUnsuccesfulInfoWhenTitleValidationFailes(expected, title, rating);
    }

    @Test
    public void persistShouldReturnUnsucessfulInfoWhenTitleContainsNonAlphaCharacters() throws Exception {
        String title = "Pitło z luftem 2";
        final Info expected = Info.unsuccesfulInfo("Title can only contain alpha characters", InfoCode.INVALID_CHARACTER, new InvalidLenghtException());
        double rating = 3.0;

        persistShouldReturnUnsuccesfulInfoWhenTitleValidationFailes(expected, title, rating);
    }

    @Test
    public void persistShouldReturnUnsucessfulInfoWhenRatingIsBelowZero() throws Exception {
        String title = "Pitło z luftem";
        final Info expected = Info.unsuccesfulInfo("Rating cannot be null, greater than five or below zero", InfoCode.INVALID_RATING, new InvalidRatingException());
        double rating = -2.2;
        persistShouldReturnUnsuccesfulInfoWhenTitleValidationFailes(expected, title, rating);
    }

    @Test
    public void persistShouldReturnUnsucessfulInfoWhenRatingIsGreaterThanFive() throws Exception {
        String title = "Pitło z luftem";
        final Info expected = Info.unsuccesfulInfo("Rating cannot be null, greater than five or below zero", InfoCode.INVALID_RATING, new InvalidRatingException());
        double rating = 44;
        persistShouldReturnUnsuccesfulInfoWhenTitleValidationFailes(expected, title, rating);
    }


    private void persistShouldReturnUnsuccesfulInfoWhenTitleValidationFailes(Info expected, String title, double rating) {
        //given
        MovieDto movieDto = new MovieDto();
        movieDto.setTitle(title);
        movieDto.setRating(rating);

        //when
        final Info actual = movieService.persist(movieDto);

        //then
        Assert.assertTrue(EqualsBuilder.reflectionEquals(actual, expected, "key", "object"));
    }

    @Test
    public void allMoviesByRatingShouldReturnSuccesfulInfoWhenMoviesFound() throws Exception {
        //given
        Movie movie = Mockito.mock(Movie.class);
        final List<Movie> allMovies = List.of(movie);
        Info expected = Info.succesfulInfo("All movies sorted by rating", InfoCode.OK, allMovies);

        allMoviesByRatingTest(allMovies, expected);
    }

    @Test
    public void allMoviesByRatingShouldReturnUnsuccesfulInfoWhenMoviesNotFound() throws Exception {
        //given
        Info expected = Info.notFound("No movies found", InfoCode.MOVIES_NOT_FOUND, null);

        allMoviesByRatingTest(Collections.emptyList(), expected);
    }

    private void allMoviesByRatingTest(List<Movie> movies, Info expected) {
        //given
        Mockito.when(movieRepository.findAll(ArgumentMatchers.any(Sort.class))).thenReturn(movies);

        //when
        final Info actual = movieService.allMoviesByRating();

        //then
        Assert.assertTrue(EqualsBuilder.reflectionEquals(actual, expected, "key"));
    }

    @Test
    public void deleteShouldReturnSuccesfulInfoWhenMovieIsDeleted() throws Exception {
        //given
        Movie movieToDelete = Mockito.mock(Movie.class);
        final Info expected = Info.succesfulInfo("Movie sucesfuly removed from the database", InfoCode.MOVIE_REMOVED, null);

        deleteTest(Optional.of(movieToDelete), expected);
    }

    @Test
    public void deleteShouldReturnUnuccesfulInfoWhenMovieNotFound() throws Exception {
        //given
        final Info expected = Info.notFound("Movie not found", InfoCode.MOVIES_NOT_FOUND, null);

        deleteTest(Optional.empty(), expected);
    }

    private void deleteTest(Optional optionalMovie, Info expected) {
        Mockito.when(movieRepository.findByTitle(ArgumentMatchers.anyString())).thenReturn(optionalMovie);

        //when
        final Info actual = movieService.delete(ArgumentMatchers.anyString());

        //then
        Assert.assertTrue(EqualsBuilder.reflectionEquals(actual, expected, "key", "object"));
    }

    @Test
    public void getShouldReturnSuccesfulInfoWhenMovieFound() throws Exception {
        //given
        Movie movie = Mockito.mock(Movie.class);
        final Info expected = Info.succesfulInfo("Movie found", InfoCode.OK, null);

        getTest(Optional.of(movie), expected);
    }

    @Test
    public void getShouldReturnUnsuccesfulInfoWhenMovieNotFound() throws Exception {
        //given
        final Info expected = Info.notFound("Movie not found", InfoCode.MOVIES_NOT_FOUND, null);

        getTest(Optional.empty(), expected);
    }

    private void getTest(Optional optionalMovie, Info expected) {
        Mockito.when(movieRepository.findByTitle(ArgumentMatchers.anyString())).thenReturn(optionalMovie);

        //when
        final Info actual = movieService.get(ArgumentMatchers.anyString());

        //then
        Assert.assertTrue(EqualsBuilder.reflectionEquals(actual, expected, "key", "object"));
    }

}