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

    // Prefs name
    private static final String SELECTED_SHARED_MOVIE = "selectedShared";

    // Prefs movie button tag
    private static final String SELECTED_BUTTON_MOVIE = "selectedButton";

    // Prefs tv-show button tag
    private static final String SELECTED_BUTTON_TV_SHOW = "selectedButtonTv";

    private MoviesWebService moviesWebService;

    private SharedPreferences prefs;


    @Inject
    public ListInteractorImpl(MoviesWebService moviesWebService, Context context) {
        this.moviesWebService = moviesWebService;

        // So it can be used for determining which list to get
        prefs = context.getApplicationContext().getSharedPreferences(SELECTED_SHARED_MOVIE, Context.MODE_PRIVATE);
    }

    @Override
    public Observable<List<Movie>> getListOfMovies() {

        // if pager position
        int selectedButton = prefs.getInt(SELECTED_BUTTON_MOVIE, 0);
        if (selectedButton == 0) {
            return moviesWebService.popularMovies().map(MoviesWrapper::getMovieList);
        } else if (selectedButton == 1) {
            return moviesWebService.highestRatedMovies().map(MoviesWrapper::getMovieList);
        } else {
            return moviesWebService.upcomingMovies().map(MoviesWrapper::getMovieList);
        }

    }


    @Override
    public Observable<List<Genre>> getListOfAllMovieGenres() {
        return moviesWebService.getListOfAllGenres().map(GenreWrapper::getGenreList);
    }

}
