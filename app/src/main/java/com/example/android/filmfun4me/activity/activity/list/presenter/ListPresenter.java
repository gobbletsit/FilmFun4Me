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

    // Movie
    void setMovieView(ListView listView, int pagerPosition);

    void showMostPopularMovies();

    void showHighestRatedMovies();

    void showUpcomingMovies();

    void showMovieSearchResults(String searchQuery);

    void getMovieGenres();

    // TV show
    void setTvShowView(ListView listView, int pagerPosition);

    void showMostPopularTvShows();

    void showHighestRatedTvShows();

    void showTvSearchResults(String searchQuery);

    void getTvGenres();

    // both
    void setSearchView(ListView listView);

    void onBindListItemAtPosition(int position, ListItemView listItemView);

    int getListItemRowsCount();

    void onListItemInteraction(int itemPosition);

    void destroy();

}

