package com.example.android.filmfun4me.utils;

/**
 * Created by gobov on 12/28/2017.
 */

public class BaseUtils {

    private static final String BASE_POSTER_PATH = "http://image.tmdb.org/t/p/w342";

    private static final String BASE_BACKDROP_PATH = "http://image.tmdb.org/t/p/w1280";

    public static final String YOUTUBE_VIDEO_URL = "http://www.youtube.com/watch?v=%1$s";

    public static final String YOUTUBE_THUMBNAIL_URL = "http://img.youtube.com/vi/%1$s/0.jpg";

    public static String getPosterPath(String posterPath) {
        return BASE_POSTER_PATH + posterPath;
    }

    public static String getBackdropPath (String backdropPath){return BASE_BACKDROP_PATH + backdropPath;}}
