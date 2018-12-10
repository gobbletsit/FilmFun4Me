package com.example.android.filmfun4me.activity.activity.detail.view;

import android.content.Intent;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.example.android.filmfun4me.R;
import com.example.android.filmfun4me.data.Movie;
import com.example.android.filmfun4me.data.TvShow;

import java.util.ArrayList;

public class DetailActivity extends AppCompatActivity {

    public static final String DETAIL_FRAG = "DETAIL_FRAG";

    public static final String KEY_FRAGMENT_POSITION = "position";

    private static final String KEY_MOVIE = "movie";
    private static final String KEY_TV_SHOW = "tv_show";
    private static final String KEY_THEME_COLOR_MOVIE = "theme_color_movie";
    private static final String KEY_THEME_COLOR_TV = "theme_color_tv";
    private static final String KEY_GENRE_NAMES_LIST_MOVIE = "genreNames";
    private static final String KEY_GENRE_NAMES_LIST_TV_SHOW = "showGenreNames";

    int themeColor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        Intent listIntent = getIntent();
        Bundle extras = listIntent.getExtras();


        int fragmentPosition;

        if (extras != null) {
            fragmentPosition = extras.getInt(KEY_FRAGMENT_POSITION);

            if (fragmentPosition == 0) {
                Movie movie = extras.getParcelable(KEY_MOVIE);
                ArrayList<String> genreNamesListMovie = extras.getStringArrayList(KEY_GENRE_NAMES_LIST_MOVIE);
                themeColor = extras.getInt(KEY_THEME_COLOR_MOVIE);
                if (movie != null) {
                    switchToMovieDetailFragment(movie, genreNamesListMovie, themeColor);
                }
            } else {
                TvShow tvShow = extras.getParcelable(KEY_TV_SHOW);
                ArrayList<String> genreNamesListTv = extras.getStringArrayList(KEY_GENRE_NAMES_LIST_TV_SHOW);
                themeColor = extras.getInt(KEY_THEME_COLOR_TV);
                if (tvShow != null) {
                    switchToTvShowDetailFragment(tvShow, genreNamesListTv, themeColor);
                }
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    private void switchToMovieDetailFragment(Movie movie, ArrayList<String> genreNamesListMovie, int themeColor){
        FragmentManager manager = getSupportFragmentManager();
        DetailFragment fragment = DetailFragment.newInstance(movie, genreNamesListMovie, themeColor);
        FragmentTransaction transaction = manager.beginTransaction();
        transaction.replace(R.id.root_activity_detail, fragment, DETAIL_FRAG);
        transaction.commit();
    }

    private void switchToTvShowDetailFragment(TvShow tvShow, ArrayList<String> genreNamesListTv, int themeColor){
        FragmentManager manager = getSupportFragmentManager();
        DetailFragment fragment = DetailFragment.newInstanceTv(tvShow, genreNamesListTv, themeColor);
        FragmentTransaction transaction = manager.beginTransaction();
        transaction.replace(R.id.root_activity_detail, fragment, DETAIL_FRAG);
        transaction.commit();
    }
}
