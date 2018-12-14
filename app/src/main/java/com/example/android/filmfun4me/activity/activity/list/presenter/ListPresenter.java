package com.example.android.filmfun4me.activity.activity.list.presenter;

import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.TextView;

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

    // Movies
    void showPopularMovies();

    void setMovieView(ListView listView);

    // Genres
    void getAllMovieGenres();

    // Clicks
    void whenMovieClicked(Movie movie, List<Genre> genreList);

    void setListColors(RecyclerView recyclerView, LinearLayoutManager layoutManager, int themeColor);

    void destroy();

}

