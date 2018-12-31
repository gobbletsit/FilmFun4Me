package com.example.android.filmfun4me.activity.activity.detail.presenter;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Paint;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.android.filmfun4me.R;
import com.example.android.filmfun4me.activity.activity.detail.model.DetailInteractor;
import com.example.android.filmfun4me.activity.activity.detail.view.DetailEpisodeView;
import com.example.android.filmfun4me.activity.activity.detail.view.DetailReviewItemView;
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

    private Context mContext;

    private DetailView detailView;
    private DetailInteractor detailInteractor;

    private Disposable videoSubscription;
    private Disposable reviewSubscription;
    private Disposable seasonSubscription;
    private Disposable episodeSubscription;
    private Disposable singleShowSubscription;

    private ArrayList<Episode> episodeList = new ArrayList<>(40);
    private List<Review> reviewList = new ArrayList<>(40);

    public DetailPresenterImpl(DetailInteractor detailInteractor, Context context) {
        this.detailInteractor = detailInteractor;
        this.mContext = context;
    }

    @Override
    public void showMovieDetails(Movie movie) {
        if (isViewAttached()) {
            detailView.showDetails(movie);
        }

    }

    @Override
    public void showTvShowDetails(String id) {
        singleShowSubscription = detailInteractor.getSingleTvShow(id)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(this::onGetTvShowSuccess, throwable -> onGetTvShowFailure());
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
    public void showTvEpisodes(TvShow tvShow, int seasonNumber) {
        episodeSubscription = detailInteractor.getTvShowEpisodeList(tvShow.getId(), seasonNumber)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(this::onGetEpisodesSuccess, throwable -> onGetEpisodesFailure());
    }

    @Override
    public void whenTrailerClicked(View view) {
        detailView.onTrailerClicked(view);
    }

    @Override
    public void showSeasonList(TvShow tvShow) {
        seasonSubscription = detailInteractor.getTvShowSeasonList(tvShow.getId())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(this::onGetSeasonListSuccess, throwable -> onGetSeasonListFailure());

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
        RxUtils.unsubscribe(seasonSubscription);
        RxUtils.unsubscribe(episodeSubscription);
        RxUtils.unsubscribe(singleShowSubscription);

    }

    private boolean isViewAttached() {
        return detailView != null;
    }

    private void onGetTvShowSuccess(TvShow tvShow) {
        detailView.showTvDetails(tvShow);
    }

    private void onGetTvShowFailure() {
        // NOTHING
    }


    // VIDEOS (TRAILERS)
    private void onGetVideosSuccess(List<Video> videoList) {
        if (isViewAttached()) {
            detailView.showVideos(videoList);
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


    // SEASONS
    private void onGetSeasonListSuccess(List<Season> seasonList) {
        detailView.showSeasonList(seasonList);
    }

    private void onGetSeasonListFailure() {
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
    public void setSeasons(List<Season> seasonList, LinearLayout seasonButtonLinearLayout, TvShow tvShow) {
        for (int i = 0; i < seasonList.size(); i++) {
            Season currentSeason = seasonList.get(i);

            // Creating programmatically because of unknown number of seasons
            // ovo bi po nekoj fori trebalo ic u view jer je glup i moze ovo radit
            // refactor ovo obavezno, mora i moze bit ljepse
            TextView textView[] = new TextView[seasonList.size()];
            textView[i] = new TextView(mContext);
            textView[i].setText(String.valueOf(currentSeason.getSeasonNumber()));
            seasonButtonLinearLayout.addView(textView[i]);
            textView[i].setPaintFlags(textView[i].getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
            textView[i].setTextColor(mContext.getResources().getColor(R.color.colorSeasonsAndReviews));
            textView[i].setPadding(16, 6, 12, 6);
            textView[i].setTextSize(mContext.getResources().getDimension(R.dimen.seasonTextSize));
            textView[i].setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    showTvEpisodes(tvShow, currentSeason.getSeasonNumber());
                }
            });
        }
    }

    @Override
    public int getEpisodeListItemRowsCount() {
        return episodeList.size();
    }

    @Override
    public void onBindEpisodeListItemOnPosition(int position, DetailEpisodeView detailEpisodeView) {
        Episode episode = episodeList.get(position);
        detailEpisodeView.setEpisodeTitle(episode.getName());
        detailEpisodeView.setEpisodeOverview(episode.getOverview());
        detailEpisodeView.setEpisodePoster(episode.getPosterPath());
    }
}
