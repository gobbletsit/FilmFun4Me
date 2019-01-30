package com.example.android.filmfun4me.activity.activity.detail.view;

import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;

import com.example.android.filmfun4me.BaseApplication;
import com.example.android.filmfun4me.NetworkLostReceiver;
import com.example.android.filmfun4me.NetworkRegainedReceiver;
import com.example.android.filmfun4me.R;
import com.example.android.filmfun4me.data.Movie;
import com.example.android.filmfun4me.data.TvShow;
import com.example.android.filmfun4me.utils.Constants;

public class DetailActivity extends AppCompatActivity implements OnTrailerClickCallback {

    public static boolean isDetailActive;

    int selectedButton;

    private NetworkRegainedReceiver networkRegainedReceiver;
    private NetworkLostReceiver networkLostReceiver;
    private boolean isReceiverRegistered;

    private Movie movie;
    private TvShow tvShow;
    private String singleItemGenres;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        Intent listIntent = getIntent();
        Bundle extras = listIntent.getExtras();

        if (extras != null) {
            selectedButton = extras.getInt(Constants.SELECTED_BUTTON);
        } else if (savedInstanceState != null) {
            selectedButton = savedInstanceState.getInt(Constants.SELECTED_BUTTON);
        } else {
            selectedButton = Constants.BUTTON_MOVIES;
        }

        if (selectedButton == Constants.BUTTON_MOVIES && extras != null) {
            movie = extras.getParcelable(Constants.KEY_MOVIE);
            //String singleMovieGenres = extras.getString(Constants.KEY_SINGLE_MOVIE_GENRES);
            singleItemGenres = extras.getString(Constants.KEY_SINGLE_MOVIE_GENRES);
            if (movie != null) {
                switchToMovieDetailFragment(movie, singleItemGenres);
                setTitle(getResources().getString(R.string.movie_details_label));
            }
        } else if (selectedButton == Constants.BUTTON_TV_SHOWS && extras != null) {
            tvShow = extras.getParcelable(Constants.KEY_TV_SHOW);
            //String singleTvShowGenres = extras.getString(Constants.KEY_SINGLE_TV_SHOW_GENRES);
            singleItemGenres = extras.getString(Constants.KEY_SINGLE_TV_SHOW_GENRES);
            if (tvShow != null) {
                switchToTvShowDetailFragment(tvShow, singleItemGenres);
                setTitle(getResources().getString(R.string.tv_details_label));
            }
        }
    }

    @Override
    protected void onResume() {
        // for receiver to know if activity running
        isDetailActive = true;
        networkLostReceiver = new NetworkLostReceiver();
        networkLostReceiver.setDetailActivityHandler(this);
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(ConnectivityManager.CONNECTIVITY_ACTION);
        registerReceiver(networkLostReceiver, intentFilter);
        super.onResume();
    }

    private void switchToMovieDetailFragment(Movie movie, String singleMovieGenres) {
        FragmentManager manager = getSupportFragmentManager();
        DetailMovieFragment fragment = DetailMovieFragment.newInstance(movie, singleMovieGenres);
        FragmentTransaction transaction = manager.beginTransaction();
        transaction.replace(R.id.root_activity_detail, fragment);
        transaction.commit();
    }

    private void switchToTvShowDetailFragment(TvShow tvShow, String singleTvShowGenres) {
        FragmentManager manager = getSupportFragmentManager();
        DetailTvShowFragment fragment = DetailTvShowFragment.newInstance(tvShow, singleTvShowGenres);
        FragmentTransaction transaction = manager.beginTransaction();
        transaction.replace(R.id.root_activity_detail, fragment);
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

    // for receiver to trigger when connection re-established
    public void reload() {
        if (selectedButton == Constants.BUTTON_MOVIES) {
            switchToMovieDetailFragment(movie, singleItemGenres);
        } else if (selectedButton == Constants.BUTTON_TV_SHOWS) {
            switchToTvShowDetailFragment(tvShow, singleItemGenres);
        }
    }

    // called from NetworkLostReceiver only if there is no connection
    public void registerNetworkRegainedReceiver() {
        networkRegainedReceiver = new NetworkRegainedReceiver();
        networkRegainedReceiver.setDetailActivityHandler(this);
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(ConnectivityManager.CONNECTIVITY_ACTION);
        registerReceiver(networkRegainedReceiver, intentFilter);
        // so we can unregister
        isReceiverRegistered = true;
    }

    @Override
    protected void onPause() {
        isDetailActive = false;
        unregisterReceiver(networkLostReceiver);
        if (isReceiverRegistered) {
            unregisterReceiver(networkRegainedReceiver);
            isReceiverRegistered = false;
        }
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
