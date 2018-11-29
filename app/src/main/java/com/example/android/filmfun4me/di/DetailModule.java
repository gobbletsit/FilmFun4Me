package com.example.android.filmfun4me.di;

import android.content.Context;

import com.example.android.filmfun4me.activity.activity.detail.model.DetailInteractor;
import com.example.android.filmfun4me.activity.activity.detail.model.DetailInteractorImpl;
import com.example.android.filmfun4me.activity.activity.detail.presenter.DetailPresenter;
import com.example.android.filmfun4me.activity.activity.detail.presenter.DetailPresenterImpl;
import com.example.android.filmfun4me.network.MoviesWebService;
import com.example.android.filmfun4me.network.TvShowsWebService;

import dagger.Module;
import dagger.Provides;

/**
 * Created by gobov on 12/29/2017.
 */

@Module
public class DetailModule {

    // Context needed
    @DetailScope
    @Provides
    DetailInteractor providesDetailInteractor(MoviesWebService moviesWebService, Context context, TvShowsWebService tvShowsWebService) {
        return new DetailInteractorImpl(moviesWebService, context, tvShowsWebService);
    }

    @DetailScope
    @Provides
    DetailPresenter providesDetailPresenter(DetailInteractor detailInteractor, Context context) {
        return new DetailPresenterImpl(detailInteractor, context);
    }
}
