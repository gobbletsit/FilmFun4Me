package com.example.android.filmfun4me.activity.activity.list.model;

import android.content.Context;
import android.content.SharedPreferences;

import com.example.android.filmfun4me.data.Genre;
import com.example.android.filmfun4me.data.GenreWrapper;
import com.example.android.filmfun4me.data.Movie;
import com.example.android.filmfun4me.data.MoviesWrapper;
import com.example.android.filmfun4me.data.TvShow;
import com.example.android.filmfun4me.data.TvShowWrapper;
import com.example.android.filmfun4me.network.MoviesWebService;
import com.example.android.filmfun4me.network.TvShowsWebService;

import java.util.List;

import javax.inject.Inject;

import io.reactivex.Observable;

/**
 * Created by gobov on 12/21/2017.
 */

public class ListInteractorImpl implements ListInteractor {

    private MoviesWebService moviesWebService;

    @Inject
    public ListInteractorImpl(MoviesWebService moviesWebService) {
        this.moviesWebService = moviesWebService;
    }

    @Override
    public Observable<List<Movie>> getListOfMostPopularMovies() {
        return moviesWebService.popularMovies().map(MoviesWrapper::getMovieList);
    }

    @Override
    public Observable<List<Movie>> getListOfHighestRatedMovies() {
        return moviesWebService.highestRatedMovies().map(MoviesWrapper::getMovieList);
    }

    @Override
    public Observable<List<Movie>> getListOfUpcomingMovies() {
        return moviesWebService.upcomingMovies().map(MoviesWrapper::getMovieList);
    }

    @Override
    public Observable<List<Genre>> getListOfAllMovieGenres() {
        return moviesWebService.getListOfAllGenres().map(GenreWrapper::getGenreList);
    }

}
