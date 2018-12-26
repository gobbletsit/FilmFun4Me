package com.example.android.filmfun4me.activity.activity.list.view;

import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.RecyclerView;
import android.widget.TextView;

import com.example.android.filmfun4me.data.Genre;
import com.example.android.filmfun4me.data.Movie;
import com.example.android.filmfun4me.data.TvShow;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by gobov on 12/21/2017.
 */

public interface ListView {

    // Set view methods
    void setUpMovieView();

    void setUpTvShowView();


    // Click methods
    void onMovieClicked(Movie movie, ArrayList<String> singleGenreNamesList);

    void onTvShowClicked(TvShow tvShow, ArrayList<String> singleGenreNamesList);


    // Genre methods
    void loadUpAllGenreList(List<Genre> genreList);


    void showLoading();

    void loadingErrorMessage(String error);


}
