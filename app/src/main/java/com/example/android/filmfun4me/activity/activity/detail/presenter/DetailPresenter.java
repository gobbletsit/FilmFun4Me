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

    void setDetailView(DetailView detailView);

    // Movie
    void showMovieDetails(Movie movie);

    void showMovieVideos(Movie movie);

    void showMovieReviews(Movie movie);

    void onBindReviewListItemOnPosition(int position, DetailReviewItemView detailReviewItemView);
    int getReviewListItemRowsCount();

    // Tv-show
    void showTvShowDetails(TvShow tvShow);

    void showTvVideos(TvShow tvShow);

    void showTvEpisodes(String tvShowId, int seasonNumber);

    void onBindEpisodeListItemOnPosition(int position, DetailEpisodeItemView detailEpisodeItemView);
    int getEpisodeListItemRowsCount();

    void onBindSeasonListItemOnPosition(int position, DetailSeasonItemView detailSeasonItemView);
    int getSeasonListItemRowsCount();
    void onSeasonListItemInteraction(String tvShowId, int seasonNumber);

    // Both
    void onBindVideoListItemOnPosition(int position, DetailVIdeoItemView detailVideoItemView);
    int getVideoListItemRowsCount();

    void whenTrailerClicked(View view);

    void destroy();
}
