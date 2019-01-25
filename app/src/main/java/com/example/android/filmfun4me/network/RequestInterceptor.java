package com.example.android.filmfun4me.network;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import com.example.android.filmfun4me.BuildConfig;
import com.example.android.filmfun4me.activity.activity.list.view.ListActivity;
import com.example.android.filmfun4me.activity.activity.list.view.ListFragment;

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

    private static final String API_KEY_TAG = "api_key";
    Context context;

    @Inject
    public RequestInterceptor(Context context) {
        this.context = context;
    }

    @Override
    public Response intercept(Chain chain) throws IOException {

        Request request = chain.request();

        HttpUrl httpUrl = request.url();

        // getting the key from the gradle properties file
        HttpUrl url = httpUrl.newBuilder().addQueryParameter(API_KEY_TAG, BuildConfig.MOVIE_CONSUMER_KEY).build();


        //finalRequest = request.newBuilder().url(url).build();
        Request finalRequest;
        if (isNetworkAvailable(context)){
            finalRequest = request.newBuilder().header("Cache-Control", "public, max-age=" + 5).url(url).build();
        } else {                                                                                                       // 3 DAYS AGO, IF NOT DISCARD
            finalRequest = request.newBuilder().removeHeader("Pragma").header("Cache-Control", "public, only-if-cached, max-stale=" + 60 * 60 * 24 * 3).build();
        }

        return chain.proceed(finalRequest);
    }

    private boolean isNetworkAvailable(Context context) {
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();

        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }
}
