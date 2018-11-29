package com.example.android.filmfun4me.activity.activity.detail.view;

import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.example.android.filmfun4me.R;
import com.example.android.filmfun4me.data.Movie;
import com.example.android.filmfun4me.data.TvShow;

import java.util.ArrayList;

public class DetailActivity extends AppCompatActivity {

    public static final String DETAIL_FRAG = "DETAIL_FRAG";

    public static final String POSITION = "int_position";

    ArrayList<String> genreNamesListMovie;
    ArrayList<String> genreNamesListTv;

    int themeColor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        Intent listIntent = getIntent();
        Bundle extras = listIntent.getExtras();


        int position;

        if (extras != null) {
            position = extras.getInt(POSITION);

            if (position == 0) {
                Movie movie = extras.getParcelable("movie");
                genreNamesListMovie = extras.getStringArrayList("genreNames");
                themeColor = extras.getInt("theme_color_movie");
                if (movie != null) {
                    FragmentManager manager = getSupportFragmentManager();

                    DetailFragment fragment = DetailFragment.newInstance(movie, genreNamesListMovie, themeColor);

                    FragmentTransaction transaction = manager.beginTransaction();
                    transaction.replace(R.id.root_activity_detail, fragment, DETAIL_FRAG);
                    transaction.commit();
                }


            } else {
                TvShow tvShow = extras.getParcelable("tv_show");
                genreNamesListTv = extras.getStringArrayList("showGenreNames");
                themeColor = extras.getInt("theme_color_tv");
                if (tvShow != null) {
                    FragmentManager manager = getSupportFragmentManager();

                    DetailFragment fragment = DetailFragment.newInstanceTv(tvShow, genreNamesListTv, themeColor);

                    FragmentTransaction transaction = manager.beginTransaction();
                    transaction.replace(R.id.root_activity_detail, fragment, DETAIL_FRAG);
                    transaction.commit();
                }

            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
