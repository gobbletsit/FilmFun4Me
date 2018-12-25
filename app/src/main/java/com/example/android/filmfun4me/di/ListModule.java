package com.example.android.filmfun4me.di;

import android.content.Context;
import android.content.SharedPreferences;

import com.example.android.filmfun4me.activity.activity.list.model.ListInteractor;
import com.example.android.filmfun4me.activity.activity.list.model.ListInteractorImpl;
import com.example.android.filmfun4me.activity.activity.list.presenter.ListPresenter;
import com.example.android.filmfun4me.activity.activity.list.presenter.ListPresenterImpl;
import com.example.android.filmfun4me.network.MoviesWebService;
import com.example.android.filmfun4me.network.TvShowsWebService;

import dagger.Module;
import dagger.Provides;

/**
 * Created by gobov on 12/22/2017.
 */

@Module
public class ListModule {

    // Context needed
    @Provides
    ListInteractor providesListInteractor(MoviesWebService moviesWebService, TvShowsWebService tvShowsWebService) {
        return new ListInteractorImpl(moviesWebService, tvShowsWebService);
    }

    @Provides
    ListPresenter provideListPresenter(ListInteractor listInteractor, Context context) {
        return new ListPresenterImpl(listInteractor, context);
    }

}
