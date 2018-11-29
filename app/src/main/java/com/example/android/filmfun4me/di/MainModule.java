package com.example.android.filmfun4me.di;

import com.example.android.filmfun4me.activity.activity.main.presenter.MainPresenter;
import com.example.android.filmfun4me.activity.activity.main.presenter.MainPresenterImpl;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

/**
 * Created by gobov on 12/23/2017.
 */

@Module
public class  MainModule {

    @Provides
    MainPresenter providesMainPresenter() {
        return new MainPresenterImpl();
    }

}
