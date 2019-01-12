package com.example.android.filmfun4me.activity.activity.list.presenter;

import android.util.Log;

import com.example.android.filmfun4me.activity.activity.list.model.ListInteractor;
import com.example.android.filmfun4me.activity.activity.list.view.ListItemView;
import com.example.android.filmfun4me.activity.activity.list.view.ListView;
import com.example.android.filmfun4me.data.Genre;
import com.example.android.filmfun4me.data.Movie;
import com.example.android.filmfun4me.data.TvShow;
import com.example.android.filmfun4me.utils.RxUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.schedulers.Schedulers;
import io.reactivex.subjects.PublishSubject;

/**
 * Created by gobov on 12/21/2017.
 */

public class ListPresenterImpl implements ListPresenter {

    private static final String TAG = ListPresenterImpl.class.getSimpleName();

    private ListView view;

    private ListInteractor listInteractor;

    private Disposable disposableSubscription;
    private Disposable disposableGenres;

    private List<Movie> movieList = new ArrayList<>(40);
    private List<TvShow> tvShowList = new ArrayList<>(40);
    private List<Genre> genreList = new ArrayList<>(40);

    private PublishSubject publishSubject;

    public ListPresenterImpl(ListInteractor listInteractor) {
        this.listInteractor = listInteractor;
    }

    @Override
    public void setSearchView(ListView listView) {
        this.view = listView;
    }

    @Override
    public void setMovieView(ListView listView, int pagerPosition) {
        this.view = listView;
        getMovieGenres();
        if (pagerPosition == 0) {
            showMostPopularMovies();
        } else if (pagerPosition == 1) {
            showHighestRatedMovies();
        } else {
            showUpcomingMovies();
        }
    }

    @Override
    public void showMostPopularMovies() {
        showLoading();
        disposableSubscription = listInteractor.getListOfMostPopularMovies()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(this::onMovieFetchSuccess, this::onMovieFetchFailed);
    }

    @Override
    public void showHighestRatedMovies() {
        showLoading();
        disposableSubscription = listInteractor.getListOfHighestRatedMovies()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(this::onMovieFetchSuccess, this::onMovieFetchFailed);
    }

    @Override
    public void showUpcomingMovies() {
        showLoading();
        disposableSubscription = listInteractor.getListOfUpcomingMovies()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(this::onMovieFetchSuccess, this::onMovieFetchFailed);
    }

    @Override
    public void showMovieSearchResults(String query) {
        if (!query.contentEquals("")){
            //if (publishSubject == null) {
            publishSubject = PublishSubject.create();
            publishSubject
                    .debounce(300, TimeUnit.MILLISECONDS)
                    .distinctUntilChanged()
                    .switchMap(searchValue -> listInteractor.getListOfSearchedMovies(query)
                            .subscribeOn(Schedulers.io())
                            .observeOn(AndroidSchedulers.mainThread()))
                    .subscribeWith(new DisposableObserver<List<Movie>>() {
                        @Override
                        public void onNext(List<Movie> response) {
                            movieList.clear();
                            movieList = response;
                            if (isViewAttached()){
                                view.setUpMovieSearchView();
                            }
                        }
                        @Override
                        public void onError(Throwable e) {
                            onSearchFetchFailed(e);
                        }
                        @Override
                        public void onComplete() {
                            //On complete
                        }
                    });
            //}
            publishSubject.onNext(query);
        }

    }

    public void onBindMovieListItemAtPosition(int position, ListItemView listItemView) {
        Movie movie = movieList.get(position);
        listItemView.setItemTitle(movie.getTitle());
        listItemView.setItemPoster(movie.getPosterPath());
        listItemView.setGenreName(getSingleItemAppendedGenres(movie.getGenreIds()));
    }

    public int getMovieListItemRowsCount() {
        return movieList.size();
    }

    @Override
    public void onMovieListItemInteraction(int itemPosition) {
        Movie movie = movieList.get(itemPosition);
        view.onMovieClicked(movie, getSingleItemAppendedGenres(movie.getGenreIds()));
    }

    private void onMovieFetchSuccess(List<Movie> movieList) {
        this.movieList.clear();
        this.movieList.addAll(movieList);
        if (isViewAttached()) {
            view.setUpMovieView();
        }
    }

    private void onMovieFetchFailed(Throwable e) {
        Log.e(TAG, "onMovieFetchFailed: = " + e);
        view.loadingErrorMessage(e.getMessage());
    }

    @Override
    public void setTvShowView(ListView listView, int pagerPosition) {
        this.view = listView;
        getTvGenres();
        if (pagerPosition == 0) {
            showMostPopularTvShows();
        } else {
            showHighestRatedTvShows();
        }
    }

    @Override
    public void showMostPopularTvShows() {
        showLoading();
        disposableSubscription = listInteractor.getListOfMostPopularTvShows()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(this::onTvShowFetchSuccess, this::onTvShowFetchFailed);
    }

    @Override
    public void showHighestRatedTvShows() {
        showLoading();
        disposableSubscription = listInteractor.getListOfHighestRatedTvShows()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(this::onTvShowFetchSuccess, this::onTvShowFetchFailed);
    }

    @Override
    public void showTvSearchResults(String searchQuery) {
        publishSubject = PublishSubject.create();
        publishSubject
                .debounce(300, TimeUnit.MILLISECONDS)
                .distinctUntilChanged()
                .switchMap(searchValue -> listInteractor.getListOfSearchedTvShows(searchQuery)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread()))
                .subscribeWith(new DisposableObserver<List<TvShow>>() {
                    @Override
                    public void onNext(List<TvShow> response) {
                        tvShowList.clear();
                        tvShowList = response;
                        if (isViewAttached()){
                            view.setUpTvSearchView();
                        }
                    }
                    @Override
                    public void onError(Throwable e) {
                        onSearchFetchFailed(e);
                    }
                    @Override
                    public void onComplete() {
                        //On complete
                    }
                });
        //}
        publishSubject.onNext(searchQuery);
    }

    @Override
    public void onBindTvShowListItemAtPosition(int position, ListItemView listItemView) {
        TvShow tvShow = tvShowList.get(position);
        listItemView.setItemTitle(tvShow.getTitle());
        listItemView.setItemPoster(tvShow.getPosterPath());
        listItemView.setGenreName(getSingleItemAppendedGenres(tvShow.getGenreIds()));
    }

    @Override
    public int getTvShowListItemRowCount() {
        return tvShowList.size();
    }

    @Override
    public void onTvShowListItemInteraction(int itemPosition) {
        TvShow tvShow = tvShowList.get(itemPosition);
        view.onTvShowClicked(tvShow, getSingleItemAppendedGenres(tvShow.getGenreIds()));
    }

    private void onTvShowFetchSuccess(List<TvShow> tvShowList) {
        this.tvShowList.clear();
        this.tvShowList.addAll(tvShowList);
        if (isViewAttached()) {
            view.setUpTvShowView();
        }
    }

    private void onTvShowFetchFailed(Throwable e) {
        Log.e(TAG, "onTvShowFetchFailed: " + e);
        view.loadingErrorMessage(e.getMessage());
    }

    // GENRE
    @Override
    public void getMovieGenres() {
        disposableGenres = listInteractor.getListOfAllMovieGenres()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(this::onGenreListFetchSuccess, this::onGenreListFetchFailed);
    }

    @Override
    public void getTvGenres() {
        disposableGenres = listInteractor.getListOfAllTvGenres()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(this::onGenreListFetchSuccess, this::onGenreListFetchFailed);
    }

    private void onGenreListFetchSuccess(List<Genre> genreList) {
        this.genreList.clear();
        this.genreList.addAll(genreList);
    }

    private void onGenreListFetchFailed(Throwable e) {
        Log.e(TAG, "onGenreFetchFailed: " + e);
        view.loadingErrorMessage(e.getMessage());
    }

    private void onSearchFetchFailed(Throwable e){
        Log.e(TAG, "onSearchFetchFailed: " + e);
        view.loadingErrorMessage(e.toString());
    }

    private String getSingleItemAppendedGenres(int[] currentGenreIds) {
        String appendedGenres = "";
        StringBuilder stringBuilder = new StringBuilder();
        for (int i = 0; i < currentGenreIds.length; i++){
            int genreId = currentGenreIds[i];
            for (int a = 0; a < genreList.size(); a++){
                if (genreId == genreList.get(a).getGenreId()){
                    if (i != currentGenreIds.length -1){
                        appendedGenres = stringBuilder.append(genreList.get(a).getGenreName()).append(", ").toString();
                    } else {
                        appendedGenres = stringBuilder.append(genreList.get(a).getGenreName()).toString();
                    }
                }
            }
        }
        return appendedGenres;
    }

    private boolean isViewAttached() {
        return view != null;
    }

    private void showLoading() {
        if (isViewAttached()) {
            view.showLoading();
        }
    }

    @Override
    public void destroy() {
        view = null;
        RxUtils.unsubscribe(disposableSubscription);
        RxUtils.unsubscribe(disposableGenres);
    }
}
