package com.example.android.filmfun4me.di;

import com.example.android.filmfun4me.activity.activity.main.presenter.MainPresenterImpl;
import com.example.android.filmfun4me.di.AppModule;
import com.example.android.filmfun4me.di.ListComponent;
import com.example.android.filmfun4me.di.ListModule;
import com.example.android.filmfun4me.di.MainComponent;
import com.example.android.filmfun4me.di.MainModule;
import com.example.android.filmfun4me.di.NetworkModule;

import javax.inject.Singleton;

import dagger.Component;
import dagger.Provides;

/**
 * Created by gobov on 12/22/2017.
 */

@Singleton
@Component(modules = {AppModule.class, NetworkModule.class})
public interface AppComponent {

    ListComponent plus(ListModule listModule);

    MainComponent plus(MainModule mainModule);

    DetailComponent plus(DetailModule detailModule);

}
