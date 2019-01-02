package com.example.android.filmfun4me.activity.activity.detail.presenter;

import android.view.View;
import android.widget.LinearLayout;

import com.example.android.filmfun4me.activity.activity.detail.view.DetailEpisodeItemView;
import com.example.android.filmfun4me.activity.activity.detail.view.DetailReviewItemView;
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
    void showTvShowDetails(String id);

    void showTvVideos(TvShow tvShow);

    void showSeasonList(TvShow tvShow);

    void showTvEpisodes(TvShow tvShow, int seasonNumber);

    void setSeasons(List<Season> seasonList, LinearLayout seasonButtonLinearLayout, TvShow tvShow);

    int getEpisodeListItemRowsCount();

    void onBindEpisodeListItemOnPosition(int position, DetailEpisodeItemView detailEpisodeItemView);

    int getVideoListItemRowsCount();

    void onBindVideoListItemOnPosition(int position, DetailVIdeoItemView detailVideoItemView);


    // Clicks
    void whenTrailerClicked(View view);

    void destroy();
}
