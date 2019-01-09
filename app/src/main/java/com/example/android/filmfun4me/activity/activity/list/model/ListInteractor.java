package com.example.android.filmfun4me.activity.activity.list.model;

import com.example.android.filmfun4me.data.Genre;
import com.example.android.filmfun4me.data.Movie;
import com.example.android.filmfun4me.data.TvShow;

import java.util.List;

import io.reactivex.Observable;


/**
 * Created by gobov on 12/21/2017.
 */

public interface ListInteractor {

    Observable<List<Movie>> getListOfMostPopularMovies();

    Observable<List<Movie>> getListOfHighestRatedMovies();

    Observable<List<Movie>> getListOfUpcomingMovies();

    Observable<List<Movie>> getListOfSearchedMovies(String searchQuery);

    // List of all possible genres
    Observable<List<Genre>> getListOfAllMovieGenres();

    Observable<List<TvShow>> getListOfMostPopularTvShows();

    Observable<List<TvShow>> getListOfHighestRatedTvShows();

    Observable<List<Genre>> getListOfAllTvGenres();

}
