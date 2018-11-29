package com.example.android.filmfun4me.network;

import com.example.android.filmfun4me.BuildConfig;

import java.io.IOException;

import javax.inject.Inject;

import okhttp3.HttpUrl;
import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by gobov on 12/22/2017.
 */

public class RequestInterceptor implements Interceptor {

    public static final String API_KEY_TAG = "api_key";

    @Inject
    RequestInterceptor() {
    }

    @Override
    public Response intercept(Chain chain) throws IOException {

        Request request = chain.request();

        HttpUrl httpUrl = request.url();

        // GETTING THE KEY FROM GRADLE PROPERTIES FILE
        HttpUrl url = httpUrl.newBuilder().addQueryParameter(API_KEY_TAG, BuildConfig.MOVIE_CONSUMER_KEY).build();

        Request finalRequest = request.newBuilder().url(url).build();

        return chain.proceed(finalRequest);
    }
}
