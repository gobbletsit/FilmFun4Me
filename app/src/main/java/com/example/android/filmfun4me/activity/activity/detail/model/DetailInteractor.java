package com.example.android.filmfun4me.activity.activity.detail.model;

import com.example.android.filmfun4me.data.Episode;
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

    io.reactivex.Observable<TvShow> getSingleTvShow(String id);

    io.reactivex.Observable<List<Video>> getVideoList(String id);

    io.reactivex.Observable<List<Review>> getReviewList(String id);

    io.reactivex.Observable<List<Season>> getSeasonList(String id);

    io.reactivex.Observable<List<Episode>> getEpisodeList(String id, int seasonNumber);


}
