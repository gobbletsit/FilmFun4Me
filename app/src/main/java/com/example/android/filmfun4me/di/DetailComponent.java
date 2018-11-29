package com.example.android.filmfun4me.di;

import com.example.android.filmfun4me.activity.activity.detail.view.DetailFragment;

import dagger.Component;
import dagger.Module;
import dagger.Subcomponent;

/**
 * Created by gobov on 12/29/2017.
 */
@DetailScope
@Subcomponent(modules = {DetailModule.class})
public interface DetailComponent {

    void inject(DetailFragment detailFragment);

}
