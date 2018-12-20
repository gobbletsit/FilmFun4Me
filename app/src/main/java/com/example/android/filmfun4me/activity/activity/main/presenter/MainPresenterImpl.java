package com.example.android.filmfun4me.activity.activity.main.presenter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.widget.Button;

import com.example.android.filmfun4me.activity.activity.list.view.ListActivity;
import com.example.android.filmfun4me.activity.activity.main.view.MainView;

/**
 * Created by gobov on 12/21/2017.
 */

public class MainPresenterImpl implements MainPresenter {

    private MainView mainView;

    @Override
    public void setView(MainView mainView) {
        this.mainView = mainView;
    }

    /*@Override
    public void goToMostPopularMoviesList(int fragmentPosition) {
        mainView.onPopularButtonClick(fragmentPosition);
    }

    @Override
    public void goToHighestRatedMoviesList(int fragmentPosition) {
        mainView.onHighestRatedButtonClick(fragmentPosition);
    }

    @Override
    public void goToUpcomingMoviesList(int fragmentPosition) {
        mainView.onUpcomingButtonClick(fragmentPosition);
    }

    @Override
    public void goToMostPopularTvShowsList(int fragmentPosition) {
        mainView.onPopularButtonClick(fragmentPosition);
    }

    @Override
    public void goToHighestRatedTvShowsList(int fragmentPosition) {
        mainView.onHighestRatedButtonClick(fragmentPosition);
    }*/
}
