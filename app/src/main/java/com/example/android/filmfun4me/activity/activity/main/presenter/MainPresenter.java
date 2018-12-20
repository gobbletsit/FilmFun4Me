package com.example.android.filmfun4me.activity.activity.main.presenter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.widget.Button;

import com.example.android.filmfun4me.activity.activity.main.view.MainView;

/**
 * Created by gobov on 12/21/2017.
 */

public interface MainPresenter {

    void setView (MainView mainView);

    // Fragment position so it can passed along to activities for use
    /*void goToMostPopularMoviesList(int fragmentPosition);

    void goToHighestRatedMoviesList(int fragmentPosition);

    void goToUpcomingMoviesList(int fragmentPosition);

    void goToMostPopularTvShowsList(int fragmentPosition);

    void goToHighestRatedTvShowsList(int fragmentPosition);*/

}
