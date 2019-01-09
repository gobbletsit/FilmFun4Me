package com.example.android.filmfun4me.network;

import com.example.android.filmfun4me.data.GenreWrapper;
import com.example.android.filmfun4me.data.Movie;
import com.example.android.filmfun4me.data.MoviesWrapper;
import com.example.android.filmfun4me.data.ReviewWrapper;
import com.example.android.filmfun4me.data.VideoWrapper;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import retrofit2.http.GET;
import retrofit2.http.Path;


/**
 * Created by gobov on 12/22/2017.
 */

public interface MoviesWebService {

    @GET("3/movie/popular?")
    io.reactivex.Observable<MoviesWrapper> popularMovies();

    @GET("3/movie/top_rated?")
    io.reactivex.Observable<MoviesWrapper> highestRatedMovies();

    @GET("3/movie/upcoming?")
    io.reactivex.Observable<MoviesWrapper> upcomingMovies();

    @GET("3/movie/{movieId}")
    Observable<Movie> getMovieDetails(@Path("movieId")String movieId);

    @GET("3/movie/{movieId}/videos")
    Observable<VideoWrapper> getVideosFromService(@Path("movieId") String movieId);

    @GET("3/movie/{movieId}/reviews")
    Observable<ReviewWrapper> getReviewsFromService(@Path("movieId") String movieId);

    @GET("3/genre/movie/list")
    Observable<GenreWrapper> getListOfAllMovieGenres();

    // will implement search by genre

}
