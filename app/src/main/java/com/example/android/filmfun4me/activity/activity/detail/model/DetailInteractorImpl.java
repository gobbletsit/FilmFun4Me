package com.example.android.filmfun4me.activity.activity.detail.model;

import android.content.Context;
import android.content.SharedPreferences;

import com.example.android.filmfun4me.data.Episode;
import com.example.android.filmfun4me.data.EpisodeWrapper;
import com.example.android.filmfun4me.data.Review;
import com.example.android.filmfun4me.data.ReviewWrapper;
import com.example.android.filmfun4me.data.Season;
import com.example.android.filmfun4me.data.TvShow;
import com.example.android.filmfun4me.data.TvShowWrapper;
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

    // Prefs name
    private static final String SELECTED_SHARED = "selectedShared";

    private static final String KEY_FRAGMENT_POSITION = "position";

    private int fragPosition;

    private MoviesWebService moviesWebService;
    private TvShowsWebService tvShowsWebService;

    private SharedPreferences sharedPrefs;

    public DetailInteractorImpl(MoviesWebService moviesWebService, Context context, TvShowsWebService tvShowsWebService) {
        this.moviesWebService = moviesWebService;
        this.tvShowsWebService = tvShowsWebService;

        // So it can be used to determine which list to get
        sharedPrefs = context.getApplicationContext().getSharedPreferences(SELECTED_SHARED, Context.MODE_PRIVATE);
    }

    @Override
    public Observable<TvShow> getSingleTvShow(String id) {
        return tvShowsWebService.getSingleTvShow(id).map(TvShowWrapper::getTvShow);
    }

    @Override
    public Observable<List<Video>> getVideoList(String id) {

        if (sharedPrefs != null && sharedPrefs.contains(KEY_FRAGMENT_POSITION)) {
            fragPosition = sharedPrefs.getInt(KEY_FRAGMENT_POSITION, 0);

            if (fragPosition == 0) {
                return moviesWebService.getVideosFromService(id).map(VideoWrapper::getVideoList);
            } else {
                return tvShowsWebService.getTvVideosFromService(id).map(VideoWrapper::getVideoList);
            }
        }
        return null;

    }

    @Override
    public Observable<List<Review>> getReviewList(String id) {

        if (sharedPrefs != null && sharedPrefs.contains(KEY_FRAGMENT_POSITION)) {
            fragPosition = sharedPrefs.getInt(KEY_FRAGMENT_POSITION, 0);

            if (fragPosition == 0) {
                return moviesWebService.getReviewsFromService(id).map(ReviewWrapper::getReviewList);
            } else {
                return tvShowsWebService.getTvReviewsFromService(id).map(ReviewWrapper::getReviewList);
            }
        }
        return null;

    }


    @Override
    public Observable<List<Season>> getSeasonList(String id) {
        return tvShowsWebService.getSingleTvShow(id).map(TvShowWrapper::getSeasonList);
    }


    @Override
    public Observable<List<Episode>> getEpisodeList(String id, int seasonNumber) {
        return tvShowsWebService.getEpisodesFromService(id, seasonNumber).map(EpisodeWrapper::getEpisodeList);
    }


}
