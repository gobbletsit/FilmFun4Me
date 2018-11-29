package com.example.android.filmfun4me.data;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by gobov on 1/23/2018.
 */

public class SeasonWrapper {

    @SerializedName("seasons")
    private List<Season> seasonList;

    public List<Season> getSeasonList() {
        return seasonList;
    }

    public void setGenreList(List<Season> seasonList) {
        this.seasonList = seasonList;
    }
}
