package com.example.android.filmfun4me.activity.activity.detail.model;

import com.example.android.filmfun4me.data.Episode;
import com.example.android.filmfun4me.data.Movie;
import com.example.android.filmfun4me.data.Review;
import com.example.android.filmfun4me.data.Season;
import com.example.android.filmfun4me.data.TvShow;
import com.example.android.filmfun4me.data.Video;

import java.util.List;
import java.util.Observable;

/**
 * Created by gobov on 12/21/2017.
 */

public interface DetailInteractor {

    io.reactivex.Observable<Movie> getSingleMovie(String id);

    io.reactivex.Observable<TvShow> getSingleTvShow(String id);

    io.reactivex.Observable<List<Video>> getMovieVideoList(String id);

    io.reactivex.Observable<List<Video>> getTvShowVideoList(String id);

    io.reactivex.Observable<List<Review>> getMovieReviewList(String id);

    public io.reactivex.Observable<List<Review>> getTvShowReviewList(String id);

    io.reactivex.Observable<List<Season>> getTvShowSeasonList(String id);

    io.reactivex.Observable<List<Episode>> getTvShowEpisodeList(String id, int seasonNumber);


}
