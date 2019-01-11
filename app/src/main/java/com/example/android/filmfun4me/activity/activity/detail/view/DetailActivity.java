package com.example.android.filmfun4me.activity.activity.detail.view;

import android.content.Intent;
import android.net.Uri;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;

import com.example.android.filmfun4me.R;
import com.example.android.filmfun4me.data.Movie;
import com.example.android.filmfun4me.data.TvShow;
import com.example.android.filmfun4me.utils.Constants;

import java.util.ArrayList;

public class DetailActivity extends AppCompatActivity implements Callback {

    public static final String DETAIL_MOVIE_FRAG = "DETAIL_MOVIE_FRAG";
    public static final String DETAIL_TV_SHOW_FRAG = "DETAIL_TV_SHOW_FRAG";

    int selectedButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        Intent listIntent = getIntent();
        Bundle extras = listIntent.getExtras();

        int selectedButton;

        if (extras != null) {
            selectedButton = extras.getInt(Constants.SELECTED_BUTTON);
        } else if (savedInstanceState != null){
            selectedButton = savedInstanceState.getInt(Constants.SELECTED_BUTTON);
        } else {
            selectedButton = Constants.BUTTON_MOVIES;
        }

        // need to make this cleaner!
        if (selectedButton == Constants.BUTTON_MOVIES && extras != null) {
            Movie movie = extras.getParcelable(Constants.KEY_MOVIE);
            String singleMovieGenres = extras.getString(Constants.KEY_SINGLE_MOVIE_GENRES);
            if (movie != null) {
                switchToMovieDetailFragment(movie, singleMovieGenres);
                setTitle("Movie details");
            }
        } else if (selectedButton == Constants.BUTTON_TV_SHOWS && extras != null ){
            TvShow tvShow = extras.getParcelable(Constants.KEY_TV_SHOW);
            String singleTvShowGenres = extras.getString(Constants.KEY_SINGLE_TV_SHOW_GENRES);
            if (tvShow != null) {
                switchToTvShowDetailFragment(tvShow, singleTvShowGenres);
                setTitle("TV show details");
            }
        }
    }

    private void switchToMovieDetailFragment(Movie movie, String singleMovieGenres){
        FragmentManager manager = getSupportFragmentManager();
        DetailMovieFragment fragment = DetailMovieFragment.newInstance(movie, singleMovieGenres);
        FragmentTransaction transaction = manager.beginTransaction();
        transaction.replace(R.id.root_activity_detail, fragment, DETAIL_MOVIE_FRAG);
        transaction.commit();
    }

    private void switchToTvShowDetailFragment(TvShow tvShow, String singleTvShowGenres){
        FragmentManager manager = getSupportFragmentManager();
        DetailTvShowFragment fragment = DetailTvShowFragment.newInstance(tvShow, singleTvShowGenres);
        FragmentTransaction transaction = manager.beginTransaction();
        transaction.replace(R.id.root_activity_detail, fragment, DETAIL_TV_SHOW_FRAG);
        transaction.commit();
    }

    @Override
    public void onTrailerClick(String videoUrl) {
        Intent playVideoIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(videoUrl));
        startActivity(playVideoIntent);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(Constants.SELECTED_BUTTON, selectedButton);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                this.onBackPressed();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
