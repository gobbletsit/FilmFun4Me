package com.example.android.filmfun4me.activity.activity.detail.presenter;

import android.content.Context;
import android.graphics.Paint;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.android.filmfun4me.R;
import com.example.android.filmfun4me.activity.activity.detail.model.DetailInteractor;
import com.example.android.filmfun4me.activity.activity.detail.view.DetailEpisodeItemView;
import com.example.android.filmfun4me.activity.activity.detail.view.DetailReviewItemView;
import com.example.android.filmfun4me.activity.activity.detail.view.DetailSeasonItemView;
import com.example.android.filmfun4me.activity.activity.detail.view.DetailVIdeoItemView;
import com.example.android.filmfun4me.activity.activity.detail.view.DetailView;
import com.example.android.filmfun4me.data.Episode;
import com.example.android.filmfun4me.data.Movie;
import com.example.android.filmfun4me.data.Review;
import com.example.android.filmfun4me.data.Season;
import com.example.android.filmfun4me.data.TvShow;
import com.example.android.filmfun4me.data.Video;
import com.example.android.filmfun4me.utils.RxUtils;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by gobov on 12/21/2017.
 */

public class DetailPresenterImpl implements DetailPresenter {

    private DetailView detailView;
    private DetailInteractor detailInteractor;

    private Disposable videoSubscription;
    private Disposable reviewSubscription;
    private Disposable episodeSubscription;
    private Disposable singleShowSubscription;
    private Disposable singleMovieSubscription;

    private ArrayList<Episode> episodeList = new ArrayList<>(40);
    private List<Review> reviewList = new ArrayList<>(40);
    private List<Video> videoList = new ArrayList<>(40);
    private List<Season> seasonList = new ArrayList<>(40);

    public DetailPresenterImpl(DetailInteractor detailInteractor) {
        this.detailInteractor = detailInteractor;
    }

    @Override
    public void showMovieDetails(Movie movie) {
        singleMovieSubscription = detailInteractor.getSingleMovie(movie.getId())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(this::onGetMovieSuccess, throwable -> onGetMovieFailure());
        showMovieVideos(movie);
        showMovieReviews(movie);
    }

    @Override
    public void showTvShowDetails(TvShow tvShow) {
        singleShowSubscription = detailInteractor.getSingleTvShow(tvShow.getId())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(this::onGetTvShowSuccess, throwable -> onGetTvShowFailure());
        showTvVideos(tvShow);
    }

    @Override
    public void showMovieVideos(Movie movie) {
        videoSubscription = detailInteractor.getMovieVideoList(movie.getId())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(this::onGetVideosSuccess, t -> onGetVideoFailure());
    }

    @Override
    public void showMovieReviews(Movie movie) {
        reviewSubscription = detailInteractor.getMovieReviewList(movie.getId())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(this::onGetReviewSuccess, throwable -> onGetReviewFailure());
    }

    @Override
    public int getReviewListItemRowsCount() {
        return reviewList.size();
    }

    @Override
    public void onBindReviewListItemOnPosition(int position, DetailReviewItemView detailReviewItemView) {
        Review review = reviewList.get(position);
        detailReviewItemView.setReviewAuthorName(review.getReviewAuthor());
        detailReviewItemView.setReviewContent(review.getReviewContent());
    }

    @Override
    public void showTvEpisodes(String tvShowId, int seasonNumber) {
        episodeSubscription = detailInteractor.getTvShowEpisodeList(tvShowId, seasonNumber)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(this::onGetEpisodesSuccess, throwable -> onGetEpisodesFailure());
    }

    @Override
    public void whenTrailerClicked(View view) {
        String videoUrl = (String) view.getTag();
        if (videoUrl != null){
            detailView.onTrailerClicked(videoUrl);
        }
    }

    @Override
    public void showTvVideos(TvShow tvShow) {
        videoSubscription = detailInteractor.getTvShowVideoList(tvShow.getId())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(this::onGetVideosSuccess, t -> onGetVideoFailure());
    }


    @Override
    public void setView(DetailView detailView) {
        this.detailView = detailView;
    }

    @Override
    public void destroy() {
        detailView = null;
        RxUtils.unsubscribe(videoSubscription);
        RxUtils.unsubscribe(reviewSubscription);
        RxUtils.unsubscribe(episodeSubscription);
        RxUtils.unsubscribe(singleShowSubscription);
        RxUtils.unsubscribe(singleMovieSubscription);

    }

    private boolean isViewAttached() {
        return detailView != null;
    }

    private void onGetMovieSuccess(Movie movie){
        detailView.showMovieDetails(movie);
    }

    private void onGetMovieFailure(){
        // nothing for now
    }

    private void onGetTvShowSuccess(TvShow tvShow) {
        detailView.showTvDetails(tvShow);
        seasonList.clear();
        seasonList.addAll(tvShow.getSeasonList());
        detailView.showSeasonList();
    }

    private void onGetTvShowFailure() {
        // NOTHING
    }


    // VIDEOS (TRAILERS)
    private void onGetVideosSuccess(List<Video> videoList) {
        this.videoList.clear();
        this.videoList.addAll(videoList);
        if (isViewAttached()){
            detailView.showVideos();
        }
    }

    private void onGetVideoFailure() {
        //NOTHING
    }


    // REVIEWS
    private void onGetReviewSuccess(List<Review> reviewList) {
        this.reviewList.clear();
        this.reviewList.addAll(reviewList);
        if (isViewAttached() && reviewList.size() != 0) {
            detailView.showReviews();
        }
    }

    private void onGetReviewFailure() {
        // NOTHING
    }

    // EPISODES
    private void onGetEpisodesSuccess(List<Episode> episodeList) {
        this.episodeList.clear();
        this.episodeList.addAll(episodeList);
        if (isViewAttached()) {
            detailView.showEpisodeList();
        }
    }

    private void onGetEpisodesFailure() {
        // NOTHING
    }


    @Override
    public int getEpisodeListItemRowsCount() {
        return episodeList.size();
    }

    @Override
    public void onBindEpisodeListItemOnPosition(int position, DetailEpisodeItemView detailEpisodeItemView) {
        Episode episode = episodeList.get(position);
        detailEpisodeItemView.setEpisodeTitle(episode.getName());
        detailEpisodeItemView.setEpisodeOverview(episode.getOverview());
        detailEpisodeItemView.setEpisodePoster(episode.getPosterPath());
    }

    @Override
    public int getVideoListItemRowsCount() {
        return videoList.size();
    }

    @Override
    public void onBindVideoListItemOnPosition(int position, DetailVIdeoItemView detailVideoItemView) {
        Video video = videoList.get(position);
        if (Video.getUrl(video) != null) {
            detailVideoItemView.setImageViewVideoTag(Video.getUrl(video));
        }

        if (Video.getThumbnailUrl(video) != null) {
            detailVideoItemView.setImageViewVideoThumbnailUrl(Video.getThumbnailUrl(video));
        }

        if (video.getTitle()!= null){
            detailVideoItemView.setVideoTitle(video.getTitle());
        }
    }

    @Override
    public int getSeasonListItemRowsCount() {
        return seasonList.size();
    }

    @Override
    public void onBindSeasonListItemOnPosition(int position, DetailSeasonItemView detailSeasonItemView) {
        Season season = seasonList.get(position);
        detailSeasonItemView.setSeasonButtonNumber(String.valueOf(season.getSeasonNumber()));
    }

    @Override
    public void onSeasonListItemInteraction(String tvShowId, int position) {
        Season season = seasonList.get(position);
        showTvEpisodes(tvShowId, season.getSeasonNumber());
    }
}
