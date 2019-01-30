package com.example.android.filmfun4me.di;

import android.content.Context;
import android.icu.util.TimeUnit;

import com.example.android.filmfun4me.BuildConfig;
import com.example.android.filmfun4me.network.MoviesWebService;
import com.example.android.filmfun4me.network.RequestInterceptor;
import com.example.android.filmfun4me.network.TvShowsWebService;
import com.squareup.okhttp.OkHttpClient;

import java.io.File;

import javax.inject.Singleton;


import dagger.Module;
import dagger.Provides;
import io.reactivex.plugins.RxJavaPlugins;
import okhttp3.Cache;
import okhttp3.Interceptor;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;


/**
 * Created by gobov on 12/22/2017.
 */

@Module
public class NetworkModule {

    private static final String BASE_URL = "http://api.themoviedb.org/";

    private static final int CONNECT_TIMEOUT_IN_MS = 30000;

    @Provides
    @Singleton
    Interceptor providesRequestInterceptor(Context context) {
        return new RequestInterceptor(context);
    }

    @Provides
    @Singleton
    Cache providesCache(File cacheFile) {
        return new Cache(cacheFile, 10 * 1000 * 1000);
    }

    @Provides
    @Singleton
    File providesCacheFile(Context context) {
        return new File(context.getCacheDir(), "responses");
    }

    @Provides
    @Singleton
    okhttp3.OkHttpClient providesOkHttpClient(com.example.android.filmfun4me.network.RequestInterceptor requestInterceptor, Cache cache) {

        HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor();

        loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);

        return new okhttp3.OkHttpClient.Builder()
                .connectTimeout(CONNECT_TIMEOUT_IN_MS, java.util.concurrent.TimeUnit.MILLISECONDS)
                .addNetworkInterceptor(loggingInterceptor)
                .addInterceptor(requestInterceptor)
                .cache(cache)
                .build();
    }

    @Provides
    @Singleton
    Retrofit providesRetrofit(okhttp3.OkHttpClient okHttpClient) {

        return new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .client(okHttpClient)
                .build();
    }

    @Singleton
    @Provides
    MoviesWebService providesMoviesWebService(Retrofit retrofit) {
        return retrofit.create(MoviesWebService.class);
    }

    @Singleton
    @Provides
    TvShowsWebService providesTvShowsWebService(Retrofit retrofit) {
        return retrofit.create(TvShowsWebService.class);
    }


}
