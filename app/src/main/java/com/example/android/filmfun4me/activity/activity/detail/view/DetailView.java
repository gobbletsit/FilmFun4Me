package com.example.android.filmfun4me.activity.activity.detail.view;

import android.view.View;
import android.widget.ImageView;

import com.example.android.filmfun4me.data.Episode;
import com.example.android.filmfun4me.data.Movie;
import com.example.android.filmfun4me.data.Review;
import com.example.android.filmfun4me.data.Season;
import com.example.android.filmfun4me.data.TvShow;
import com.example.android.filmfun4me.data.Video;

import java.util.List;

/**
 * Created by gobov on 12/21/2017.
 */

public interface DetailView {

    void showDetails(Movie movie);

    void showTvDetails(TvShow tvShow);

    void showVideos(List<Video> videos);

    void showReviews(List<Review> reviews);

    void showSeasonList(List<Season> seasonList);

    void showEpisodeList(List<Episode> episodeList);

    void onTrailerClicked(View v);

}
