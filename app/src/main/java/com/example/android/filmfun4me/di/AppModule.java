package com.example.android.filmfun4me.di;

import android.app.Application;
import android.content.Context;
import android.content.res.Resources;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

/**
 * Created by gobov on 12/22/2017.
 */

@Module
public class AppModule {

    private Context context;

    public AppModule(Application application) {
        context = application;
    }

    @Provides
    @Singleton
    public Context providesContext() {
        return context;
    }

    @Provides
    @Singleton
    public Resources providesResources(Context context) {
        return context.getResources();
    }

}
