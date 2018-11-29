package com.example.android.filmfun4me.di;

import com.example.android.filmfun4me.activity.activity.main.view.MoviesFragment;
import com.example.android.filmfun4me.activity.activity.main.view.TvShowsFragment;

import dagger.Component;
import dagger.Subcomponent;

/**
 * Created by gobov on 12/23/2017.
 */
@MainScope
@Subcomponent(modules = MainModule.class)
public interface MainComponent {

    MoviesFragment inject(MoviesFragment fragment);

    TvShowsFragment inject(TvShowsFragment fragment);

}
