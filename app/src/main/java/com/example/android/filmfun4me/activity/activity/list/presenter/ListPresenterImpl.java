package com.example.android.filmfun4me.activity.activity.list.presenter;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.example.android.filmfun4me.R;
import com.example.android.filmfun4me.activity.activity.list.model.ListInteractor;
import com.example.android.filmfun4me.activity.activity.list.view.ListView;
import com.example.android.filmfun4me.data.Genre;
import com.example.android.filmfun4me.data.Movie;
import com.example.android.filmfun4me.data.TvShow;
import com.example.android.filmfun4me.utils.RxUtils;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by gobov on 12/21/2017.
 */

public class ListPresenterImpl implements ListPresenter {

    // Prefs name
    private static final String SELECTED_SHARED = "selectedShared";

    // Prefs movie button tag
    private static final String SELECTED_BUTTON_MOVIE = "selectedButton";

    private ListView view;

    private ListInteractor listInteractor;

    private Disposable disposableSubscription;
    private Disposable disposableGenres;

    private Context mContext;


    public ListPresenterImpl(ListInteractor listInteractor, Context context) {
        this.listInteractor = listInteractor;
        this.mContext = context;
    }

    @Override
    public void setMovieView(ListView listView, int pagerPosition) {
        this.view = listView;

        if (pagerPosition == 0){
            showMostPopularMovies();
        } else if (pagerPosition == 1){
            showHighestRatedMovies();
        } else {
            showUpcomingMovies();
        }

        getAllMovieGenres();
    }

    @Override
    public void setTvShowView(ListView listView, int pagerPosition) {
        this.view = listView;
        if (pagerPosition == 0){
            showMostPopularTvShows();
        } else {
            showHighestRatedTvShows();
        }
    }

    // Show methods
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

    // Genre methods
    @Override
    public void getAllMovieGenres() {
        disposableGenres = listInteractor.getListOfAllMovieGenres()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(this::onGenreListFetchSuccess, this::onGenreListFetchFailed);
    }


    // Success and failure methods for RX
    private void onMovieFetchSuccess(List<Movie> movieList) {
        if (isViewAttached()) {
            view.setUpMovieView(movieList);
        }

    }

    private void onMovieFetchFailed(Throwable e) {
        view.loadingErrorMessage(e.getMessage());
    }


    private void onGenreListFetchSuccess(List<Genre> genreList) {
        view.loadUpAllGenreList(genreList);
    }

    private void onGenreListFetchFailed(Throwable e) {
        view.loadingErrorMessage(e.getMessage());
    }

    @Override
    public void whenMovieClicked(Movie movie, List<Genre> genreList) {
        int[] currentGenreIds = movie.getGenreIds();

        view.onMovieClicked(movie, getSingleItemGenreList(currentGenreIds, genreList));
    }

    // To compare to all the genres and get the match when found
    private ArrayList<String> getSingleItemGenreList(int[] currentGenreIds, List<Genre> genreList) {

        ArrayList<String> singleGenreNamesList = new ArrayList<>(10);
        // Going through the single item genre list ids
        for (int singleGenreId : currentGenreIds) {
            compareGenreIdsAndLoadGenreList(genreList, singleGenreNamesList, singleGenreId);
        }
        return singleGenreNamesList;
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

    // Success and failure methods for RX
    private void onTvShowFetchSuccess(List<TvShow> tvShowList) {
        if (isViewAttached()) {
            view.setUpTvShowView(tvShowList);
        }
    }

    private void onTvShowFetchFailed(Throwable e) {
        view.loadingErrorMessage(e.getMessage());
    }

    private void compareGenreIdsAndLoadGenreList(List<Genre> genreList, ArrayList<String> singleGenreNamesList, int singleGenreId){
        // Going through list of all possible genres
        for (int a = 0; a < genreList.size(); a++) {
            int preciseGenreId = genreList.get(a).getGenreId();
            // Comparing results
            if (preciseGenreId == singleGenreId) {
                // If the match is found, add to single genre list
                singleGenreNamesList.add(genreList.get(a).getGenreName());
                break;
            }
        }
    }
}
