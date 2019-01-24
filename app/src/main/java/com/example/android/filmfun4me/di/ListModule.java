package com.example.android.filmfun4me.di;

import android.support.v7.widget.RecyclerView;

import com.example.android.filmfun4me.activity.activity.list.model.ListInteractor;
import com.example.android.filmfun4me.activity.activity.list.model.ListInteractorImpl;
import com.example.android.filmfun4me.activity.activity.list.presenter.ListPresenter;
import com.example.android.filmfun4me.activity.activity.list.presenter.ListPresenterImpl;
import com.example.android.filmfun4me.activity.activity.list.view.ListItemRecyclerAdapter;
import com.example.android.filmfun4me.network.MoviesWebService;
import com.example.android.filmfun4me.network.TvShowsWebService;

import dagger.Module;
import dagger.Provides;

/**
 * Created by gobov on 12/22/2017.
 */

@Module
public class ListModule {

    @Provides
    ListInteractor providesListInteractor(MoviesWebService moviesWebService, TvShowsWebService tvShowsWebService) {
        return new ListInteractorImpl(moviesWebService, tvShowsWebService);
    }

    // so it stays on activity level instead of application level with @Singleton
    @ListScope
    @Provides
    ListPresenter provideListPresenter(ListInteractor listInteractor) {
        return new ListPresenterImpl(listInteractor);
    }

    @Provides
    RecyclerView.Adapter provideListMovieRecyclerAdapter (ListPresenter listPresenter){
        return new ListItemRecyclerAdapter(listPresenter);
    }

}
