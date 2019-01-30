package com.example.android.filmfun4me.activity.activity.detail.model;

import com.example.android.filmfun4me.data.Episode;
import com.example.android.filmfun4me.data.Movie;
import com.example.android.filmfun4me.data.Review;
import com.example.android.filmfun4me.data.Season;
import com.example.android.filmfun4me.data.TvShow;
import com.example.android.filmfun4me.data.Video;

import io.reactivex.Observable;

import java.util.List;

/**
 * Created by gobov on 12/21/2017.
 */

public interface DetailInteractor {

    Observable<Movie> getSingleMovie(String id);

    Observable<TvShow> getSingleTvShow(String id);

    Observable<List<Video>> getMovieVideoList(String id);

    Observable<List<Video>> getTvShowVideoList(String id);

    Observable<List<Review>> getMovieReviewList(String id);

    Observable<List<Episode>> getTvShowEpisodeList(String id, int seasonNumber);

}
