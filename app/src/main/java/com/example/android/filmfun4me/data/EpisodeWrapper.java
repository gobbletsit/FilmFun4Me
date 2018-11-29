package com.example.android.filmfun4me.data;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by gobov on 1/28/2018.
 */

public class EpisodeWrapper {

    @SerializedName("episodes")
    private List<Episode> episodeList;

    public List<Episode> getEpisodeList() {
        return episodeList;
    }

    public void setEpisodeList(List<Episode> episodeList) {
        this.episodeList = episodeList;
    }
}
