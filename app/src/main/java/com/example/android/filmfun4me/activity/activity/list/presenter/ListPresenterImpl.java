package com.example.android.filmfun4me.activity.activity.list.presenter;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

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

    private SharedPreferences sharedPreferences;

    private Context mContext;


    public ListPresenterImpl(ListInteractor listInteractor, Context context) {
        this.listInteractor = listInteractor;
        this.mContext = context;

        // So it can be used for determining which list to get
        sharedPreferences = context.getApplicationContext().getSharedPreferences(SELECTED_SHARED, Context.MODE_PRIVATE);
    }


    // Show methods
    @Override
    public void showPopularMovies() {
        showLoading();
        disposableSubscription = listInteractor.getListOfMovies()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(this::onMovieFetchSuccess, this::onMovieFetchFailed);
    }


    // Set view methods
    @Override
    public void setMovieView(ListView listView) {
        this.view = listView;
        showPopularMovies();
        getAllMovieGenres();
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


    // Click methods
    @Override
    public void whenMovieClicked(Movie movie, List<Genre> genreList, ArrayList<String> singleGenreNamesList) {
        int[] currentGenreIds = movie.getGenreIds();

        setUpSingleItemGenreList(currentGenreIds, genreList, singleGenreNamesList);

        view.onMovieClicked(movie, genreList, singleGenreNamesList);
    }


    // To compare to all the genres and get the match if it's found
    // OVO JE ODVRATNO MOJ TI, REFACTOR POD HITNO!!!!!!!
    private void setUpSingleItemGenreList(int[] currentGenreIds, List<Genre> genreList, ArrayList<String> singleGenreNamesList) {

        if (singleGenreNamesList.size() != 0){
            singleGenreNamesList.clear();
        }
        // Going through the single item genre list ids
        for (int i = 0; i < currentGenreIds.length; i++) {
            int singleGenreId = currentGenreIds[i];

            // Going through all possible genre list
            // u novu metodu
            // method for genre identifying
            for (int a = 0; a < genreList.size(); a++) {
                int preciseGenreId = genreList.get(a).getGenreId();

                // Comparing results
                if (preciseGenreId == singleGenreId) {

                    // If the match is found, add to single genre list
                    singleGenreNamesList.add(genreList.get(a).getGenreName());
                }
            }
        }
    }

    @Override
    public void setListColors(RecyclerView recyclerView, LinearLayoutManager layoutManager, int themeColor) {

        int selectedButton = sharedPreferences.getInt(SELECTED_BUTTON_MOVIE, 0);

        DividerItemDecoration itemDecoration = new DividerItemDecoration(
                recyclerView.getContext(),
                layoutManager.getOrientation()
        );

        if (selectedButton == 0) {
            itemDecoration.setDrawable(ContextCompat.getDrawable(mContext, R.drawable.divider_popular));
            themeColor = ContextCompat.getColor(mContext, R.color.colorPopular);
        } else if (selectedButton == 1) {
            itemDecoration.setDrawable(ContextCompat.getDrawable(mContext, R.drawable.divider_highest_rated));
            themeColor = ContextCompat.getColor(mContext, R.color.colorHighestRated);
        } else {
            itemDecoration.setDrawable(ContextCompat.getDrawable(mContext, R.drawable.divider_upcoming));
            themeColor = ContextCompat.getColor(mContext, R.color.colorUpcoming);
        }

        view.setViewColors(recyclerView, itemDecoration, themeColor);

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
