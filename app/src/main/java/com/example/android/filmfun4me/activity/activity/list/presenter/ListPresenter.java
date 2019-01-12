package com.example.android.filmfun4me.activity.activity.list.presenter;

import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.SearchView;
import android.widget.TextView;

import com.example.android.filmfun4me.activity.activity.list.view.ListItemView;
import com.example.android.filmfun4me.activity.activity.list.view.ListView;
import com.example.android.filmfun4me.data.Genre;
import com.example.android.filmfun4me.data.Movie;
import com.example.android.filmfun4me.data.TvShow;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by gobov on 12/21/2017.
 */

public interface ListPresenter {

    void setSearchView(ListView listView);

    // Movie
    void setMovieView(ListView listView, int pagerPosition);

    void showMostPopularMovies();

    void showHighestRatedMovies();

    void showUpcomingMovies();

    void showMovieSearchResults(String searchQuery);

    void onBindMovieListItemAtPosition(int position, ListItemView listItemView);
    int getMovieListItemRowsCount();
    void onMovieListItemInteraction(int itemPosition);

    void getMovieGenres();

    // TV show
    void setTvShowView(ListView listView, int pagerPosition);

    void showMostPopularTvShows();

    void showHighestRatedTvShows();

    void showTvSearchResults(String searchQuery);

    void onBindTvShowListItemAtPosition(int position, ListItemView listItemView);
    int getTvShowListItemRowCount();
    void onTvShowListItemInteraction(int itemPosition);

    void getTvGenres();

    // both
    void destroy();

}

