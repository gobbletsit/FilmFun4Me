package com.example.android.filmfun4me;

import android.app.Application;
import android.os.StrictMode;

import com.example.android.filmfun4me.di.AppComponent;
import com.example.android.filmfun4me.di.DaggerAppComponent;
import com.example.android.filmfun4me.di.AppModule;
import com.example.android.filmfun4me.di.DetailComponent;
import com.example.android.filmfun4me.di.DetailModule;
import com.example.android.filmfun4me.di.ListComponent;
import com.example.android.filmfun4me.di.ListModule;
import com.example.android.filmfun4me.di.NetworkModule;

/**
 * Created by gobov on 12/21/2017.
 */

public class BaseApplication extends Application {

    private AppComponent appComponent;
    private ListComponent listComponent;
    private MainComponent mainComponent;
    private DetailComponent detailComponent;

    @Override
    public void onCreate() {
        super.onCreate();

        StrictMode.enableDefaults();
        appComponent = createAppComponent();
    }

    private AppComponent createAppComponent() {

        return DaggerAppComponent.builder()
                .appModule(new AppModule(this))
                .networkModule(new NetworkModule()).build();
    }


    // CREATE COMPONENTS
    public ListComponent createListComponent(){
        listComponent = appComponent.plus(new ListModule());
        return listComponent;
    }

    public MainComponent createMainComponent(){
        mainComponent = appComponent.plus(new MainModule());
        return mainComponent;
    }

    public DetailComponent createDetailComponent(){
        detailComponent = appComponent.plus(new DetailModule());
        return detailComponent;
    }


    // RELEASE COMPONENTS
    public void releaseListComponent(){
        listComponent = null;
    }

    public void releaseMainComponent(){
        mainComponent = null;
    }

    public void releaseDetailComponent() {detailComponent = null;}


    // GET COMPONENTS
    public ListComponent getListComponent() {
        return listComponent;
    }

    public MainComponent getMainComponent(){
        return mainComponent;
    }

    public DetailComponent getDetailComponent(){ return detailComponent;}
}
