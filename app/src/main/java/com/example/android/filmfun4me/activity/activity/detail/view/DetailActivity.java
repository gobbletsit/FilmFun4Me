package com.example.android.filmfun4me.activity.activity.detail.view;

import android.content.Intent;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.example.android.filmfun4me.R;
import com.example.android.filmfun4me.data.Movie;
import com.example.android.filmfun4me.data.TvShow;
import com.example.android.filmfun4me.utils.Constants;

import java.util.ArrayList;

public class DetailActivity extends AppCompatActivity {

    public static final String DETAIL_FRAG = "DETAIL_FRAG";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        Intent listIntent = getIntent();
        Bundle extras = listIntent.getExtras();

        int selectedButton;

        if (extras != null) {
            selectedButton = extras.getInt(Constants.SELECTED_BUTTON);

            // need to make this cleaner!
            if (selectedButton == 0) {
                Movie movie = extras.getParcelable(Constants.KEY_MOVIE);
                ArrayList<String> genreNamesListMovie = extras.getStringArrayList(Constants.KEY_GENRE_NAMES_LIST_MOVIE);
                if (movie != null) {
                    switchToMovieDetailFragment(movie, genreNamesListMovie);
                    if (movie.getTitle()!= null){
                        setTitle(movie.getTitle());
                    }
                }
            } else {
                TvShow tvShow = extras.getParcelable(Constants.KEY_TV_SHOW);
                ArrayList<String> genreNamesListTv = extras.getStringArrayList(Constants.KEY_GENRE_NAMES_LIST_TV_SHOW);
                if (tvShow != null) {
                    switchToTvShowDetailFragment(tvShow, genreNamesListTv);
                    if (tvShow.getTitle()!= null){
                        setTitle(tvShow.getTitle());
                    }
                }
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    private void switchToMovieDetailFragment(Movie movie, ArrayList<String> genreNamesListMovie){
        FragmentManager manager = getSupportFragmentManager();
        DetailFragment fragment = DetailFragment.newInstance(movie, genreNamesListMovie);
        FragmentTransaction transaction = manager.beginTransaction();
        transaction.replace(R.id.root_activity_detail, fragment, DETAIL_FRAG);
        transaction.commit();
    }

    private void switchToTvShowDetailFragment(TvShow tvShow, ArrayList<String> genreNamesListTv){
        FragmentManager manager = getSupportFragmentManager();
        DetailFragment fragment = DetailFragment.newInstanceTv(tvShow, genreNamesListTv);
        FragmentTransaction transaction = manager.beginTransaction();
        transaction.replace(R.id.root_activity_detail, fragment, DETAIL_FRAG);
        transaction.commit();
    }
}
