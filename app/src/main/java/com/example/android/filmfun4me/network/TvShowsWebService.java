package com.example.android.filmfun4me.network;

import com.example.android.filmfun4me.data.EpisodeWrapper;
import com.example.android.filmfun4me.data.GenreWrapper;
import com.example.android.filmfun4me.data.MoviesWrapper;
import com.example.android.filmfun4me.data.ReviewWrapper;
import com.example.android.filmfun4me.data.TvShow;
import com.example.android.filmfun4me.data.TvShowWrapper;
import com.example.android.filmfun4me.data.VideoWrapper;

import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

/**
 * Created by gobov on 1/12/2018.
 */

public interface TvShowsWebService {

    @GET("3/tv/popular")
    io.reactivex.Observable<TvShowWrapper> getPopularTvShows();

    @GET("3/tv/top_rated")
    io.reactivex.Observable<TvShowWrapper> getHighestRatedTvShows();

    @GET("3/search/tv?")
    Observable<TvShowWrapper> getListOfTvShowsBySearch(@Query("query") String searchQuery);

    @GET("3/tv/{tv_id}")
    io.reactivex.Observable<TvShow> getSingleTvShow(@Path("tv_id") String tvId);

    @GET("3/genre/tv/list")
    io.reactivex.Observable<GenreWrapper> getListOfAllTvGenres();

    @GET("3/tv/{tv_id}/videos")
    io.reactivex.Observable<VideoWrapper> getTvVideosFromService(@Path("tv_id") String tvId);

    @GET("3/tv/{tv_id}/reviews")
    io.reactivex.Observable<ReviewWrapper> getTvReviewsFromService(@Path("tv_id") String tvId);

    @GET("3/tv/{tv_id}/season/{season_number}")
    io.reactivex.Observable<EpisodeWrapper> getEpisodesFromService(@Path("tv_id") String tvId, @Path("season_number") int seasonNumber);

}
