package com.example.android.filmfun4me.activity.activity.detail.view;

import android.view.View;
import android.widget.ImageView;

import com.bignerdranch.expandablerecyclerview.Model.ParentObject;
import com.example.android.filmfun4me.data.Episode;
import com.example.android.filmfun4me.data.Movie;
import com.example.android.filmfun4me.data.Review;
import com.example.android.filmfun4me.data.Season;
import com.example.android.filmfun4me.data.TvShow;
import com.example.android.filmfun4me.data.Video;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by gobov on 12/21/2017.
 */

public interface DetailView {

    // movie
    void showMovieDetails(Movie movie);

    void showReviews();

    // tv
    void showTvDetails(TvShow tvShow);

    void showSeasonList();

    void showEpisodeList();

    void showEpisodes(ArrayList<ParentObject> parentObjects);

    // both
    void showVideos();

    void onTrailerClicked(String videoUrl);

    void showLoading();

    void onLoadingFinished();

    void loadingErrorMessage(String error);

}
