package com.example.android.filmfun4me.activity.activity.list.presenter;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.example.android.filmfun4me.R;
import com.example.android.filmfun4me.activity.activity.list.model.ListInteractor;
import com.example.android.filmfun4me.activity.activity.list.view.ListItemView;
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

    private ListView view;

    private ListInteractor listInteractor;

    private Disposable disposableSubscription;
    private Disposable disposableGenres;

    private List<Movie> movieList = new ArrayList<>(40);
    private List<TvShow> tvShowList = new ArrayList<>(40);
    private List<Genre> genreList = new ArrayList<>(40);


    public ListPresenterImpl(ListInteractor listInteractor) {
        this.listInteractor = listInteractor;
    }

    @Override
    public void setMovieView(ListView listView, int pagerPosition) {
        this.view = listView;
        getAllMovieGenres();
        if (pagerPosition == 0){
            showMostPopularMovies();
        } else if (pagerPosition == 1){
            showHighestRatedMovies();
        } else {
            showUpcomingMovies();
        }
    }

    @Override
    public void setTvShowView(ListView listView, int pagerPosition) {
        this.view = listView;
        getAllMovieGenres();
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
        this.movieList.clear();
        this.movieList.addAll(movieList);
        if (isViewAttached()) {
            view.setUpMovieView();
        }

    }

    public void onBindMovieListItemAtPosition(int position, ListItemView listItemView){
        Movie movie = movieList.get(position);
        String genreName = getSingleGenreName(movie.getGenreIds());
        listItemView.setItemTitle(movie.getTitle());
        listItemView.setItemPoster(movie.getPosterPath());
        listItemView.setGenreName(genreName);
    }

    @Override
    public void onBindTvShowListItemAtPosition(int position, ListItemView listItemView) {
        TvShow tvShow = tvShowList.get(position);
        String genreName = getSingleGenreName(tvShow.getGenreIds());
        listItemView.setItemTitle(tvShow.getTitle());
        listItemView.setItemPoster(tvShow.getPosterPath());
        listItemView.setGenreName(genreName);
    }

    public int getMovieListItemRowsCount(){
        return movieList.size();
    }

    @Override
    public int getTvShowListItemRowCount() {
        return tvShowList.size();
    }

    @Override
    public void onMovieListItemInteraction(int itemPosition) {
        Movie movie = movieList.get(itemPosition);
        int[] currentGenreIds = movie.getGenreIds();

        view.onMovieClicked(movie, getSingleItemGenreList(currentGenreIds, genreList));
    }

    @Override
    public void onTvShowListItemInteraction(int itemPosition) {
        TvShow tvShow = tvShowList.get(itemPosition);
        int[] currentGenreIds = tvShow.getGenreIds();

        view.onTvShowClicked(tvShow, getSingleItemGenreList(currentGenreIds, genreList));
    }

    private void onMovieFetchFailed(Throwable e) {
        view.loadingErrorMessage(e.getMessage());
    }


    private void onGenreListFetchSuccess(List<Genre> genreList) {
        this.genreList.clear();
        this.genreList.addAll(genreList);
    }

    private void onGenreListFetchFailed(Throwable e) {
        view.loadingErrorMessage(e.getMessage());
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
        this.tvShowList.clear();
        this.tvShowList.addAll(tvShowList);
        if (isViewAttached()) {
            view.setUpTvShowView();
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

    private String getSingleGenreName(int[] currentGenreIds) {

        int singleGenreId;

        String genreName = "";

        for (int currentGenreId : currentGenreIds) {
            singleGenreId = currentGenreId;

            for (int a = 0; a < genreList.size(); a++) {
                int preciseGenreId = genreList.get(a).getGenreId();

                if (preciseGenreId == singleGenreId) {
                    genreName = genreList.get(a).getGenreName();
                }
            }
        }
        return genreName;
    }
}
