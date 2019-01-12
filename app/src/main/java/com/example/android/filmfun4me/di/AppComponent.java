package com.example.android.filmfun4me.di;


import javax.inject.Singleton;

import dagger.Component;

/**
 * Created by gobov on 12/22/2017.
 */

@Singleton
@Component(modules = {AppModule.class, NetworkModule.class})
public interface AppComponent {

    ListComponent plus(ListModule listModule);

    DetailComponent plus(DetailModule detailModule);

}
