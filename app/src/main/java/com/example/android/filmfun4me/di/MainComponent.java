package com.example.android.filmfun4me.di;

import com.example.android.filmfun4me.activity.activity.main.view.MainFragment;

import dagger.Subcomponent;

/**
 * Created by gobov on 12/23/2017.
 */
@MainScope
@Subcomponent(modules = MainModule.class)
public interface MainComponent {

    MainFragment inject(MainFragment fragment);

}
