package com.example.android.filmfun4me.data;

import com.example.android.filmfun4me.activity.activity.list.view.ListActivity;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by gobov on 12/29/2017.
 */

public class VideoWrapper {

    @SerializedName("results")
    private List<Video> videoList;

    public List<Video> getVideoList() {
        return videoList;
    }

    public void setVideoList(List<Video> videoList) {
        this.videoList = videoList;
    }
}
