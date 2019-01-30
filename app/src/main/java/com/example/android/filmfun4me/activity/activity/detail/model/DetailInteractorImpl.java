package com.example.android.filmfun4me.activity.activity.detail.model;

import com.example.android.filmfun4me.data.Episode;
import com.example.android.filmfun4me.data.EpisodeWrapper;
import com.example.android.filmfun4me.data.Movie;
import com.example.android.filmfun4me.data.Review;
import com.example.android.filmfun4me.data.ReviewWrapper;
import com.example.android.filmfun4me.data.TvShow;
import com.example.android.filmfun4me.data.Video;
import com.example.android.filmfun4me.data.VideoWrapper;
import com.example.android.filmfun4me.network.MoviesWebService;
import com.example.android.filmfun4me.network.TvShowsWebService;

import java.util.List;

import io.reactivex.Observable;

/**
 * Created by gobov on 12/21/2017.
 */

public class DetailInteractorImpl implements DetailInteractor {

    private MoviesWebService moviesWebService;
    private TvShowsWebService tvShowsWebService;

    public DetailInteractorImpl(MoviesWebService moviesWebService, TvShowsWebService tvShowsWebService) {
        this.moviesWebService = moviesWebService;
        this.tvShowsWebService = tvShowsWebService;
    }

    @Override
    public Observable<Movie> getSingleMovie(String id) {
        return moviesWebService.getMovieDetails(id);
    }

    @Override
    public Observable<TvShow> getSingleTvShow(String id) {
        return tvShowsWebService.getSingleTvShow(id);
    }

    @Override
    public Observable<List<Video>> getMovieVideoList(String id) {
        return moviesWebService.getVideosFromService(id).map(VideoWrapper::getVideoList);
    }

    @Override
    public Observable<List<Video>> getTvShowVideoList(String id) {
        return tvShowsWebService.getTvVideosFromService(id).map(VideoWrapper::getVideoList);
    }

    @Override
    public Observable<List<Review>> getMovieReviewList(String id) {
        return moviesWebService.getReviewsFromService(id).map(ReviewWrapper::getReviewList);
    }

    @Override
    public Observable<List<Episode>> getTvShowEpisodeList(String id, int seasonNumber) {
        return tvShowsWebService.getEpisodesFromService(id, seasonNumber).map(EpisodeWrapper::getEpisodeList);
    }
}
