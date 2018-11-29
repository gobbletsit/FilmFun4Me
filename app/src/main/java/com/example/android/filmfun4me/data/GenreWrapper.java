package com.example.android.filmfun4me.data;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by gobov on 1/9/2018.
 */

public class GenreWrapper {

    @SerializedName("genres")
    private List<Genre> genreList;

    public List<Genre> getGenreList() {
        return genreList;
    }

    public void setGenreList(List<Genre> genreList) {
        this.genreList = genreList;
    }
}
