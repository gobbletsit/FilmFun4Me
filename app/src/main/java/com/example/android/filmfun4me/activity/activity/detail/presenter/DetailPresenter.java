package com.example.android.filmfun4me.activity.activity.detail.presenter;

import android.view.View;
import android.widget.LinearLayout;

import com.example.android.filmfun4me.activity.activity.detail.view.DetailEpisodeItemView;
import com.example.android.filmfun4me.activity.activity.detail.view.DetailReviewItemView;
import com.example.android.filmfun4me.activity.activity.detail.view.DetailSeasonItemView;
import com.example.android.filmfun4me.activity.activity.detail.view.DetailVIdeoItemView;
import com.example.android.filmfun4me.activity.activity.detail.view.DetailView;
import com.example.android.filmfun4me.data.Movie;
import com.example.android.filmfun4me.data.Season;
import com.example.android.filmfun4me.data.TvShow;

import java.util.List;

/**
 * Created by gobov on 12/21/2017.
 */

public interface DetailPresenter {

    void setView(DetailView detailView);

    // Movie
    void showMovieDetails(Movie movie);

    void showMovieVideos(Movie movie);

    void showMovieReviews(Movie movie);

    int getReviewListItemRowsCount();

    void onBindReviewListItemOnPosition(int position, DetailReviewItemView detailReviewItemView);


    // Tv-show
    void showTvShowDetails(TvShow tvShow);

    void showTvVideos(TvShow tvShow);

    void showTvEpisodes(String tvShowId, int seasonNumber);

    int getEpisodeListItemRowsCount();

    void onBindEpisodeListItemOnPosition(int position, DetailEpisodeItemView detailEpisodeItemView);

    int getVideoListItemRowsCount();

    void onBindVideoListItemOnPosition(int position, DetailVIdeoItemView detailVideoItemView);

    int getSeasonListItemRowsCount();

    void onBindSeasonListItemOnPosition(int position, DetailSeasonItemView detailSeasonItemView);

    void onSeasonListItemInteraction(String tvShowId, int seasonNumber);



    // Clicks
    void whenTrailerClicked(View view);

    void destroy();
}
