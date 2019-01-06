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

    @GET("3/discover/movie?sort_by=popularity.desc")
    io.reactivex.Observable<MoviesWrapper> popularMovies();

    @GET("3/discover/movie/?certification_country=US&certification=R&sort_by=vote_average.desc")
    io.reactivex.Observable<MoviesWrapper> highestRatedMovies();

    @GET("3/discover/movie?primary_release_date.gte=2014-09-15&primary_release_date.lte=2014-10-22")
    io.reactivex.Observable<MoviesWrapper> upcomingMovies();

    // need to implement this, and pass on movieId instead of Movie parcelable(refactor)
    @GET("3/movie/{movieId}")
    Observable<Movie> getMovieDetails(@Path("movieId")String movieId);

    @GET("3/movie/{movieId}/videos")
    Observable<VideoWrapper> getVideosFromService(@Path("movieId") String movieId);

    @GET("3/movie/{movieId}/reviews")
    Observable<ReviewWrapper> getReviewsFromService(@Path("movieId") String movieId);

    @GET("3/genre/movie/list")
    Observable<GenreWrapper> getListOfAllMovieGenres();

}
