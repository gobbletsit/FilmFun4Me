package com.example.android.filmfun4me.data;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by gobov on 1/12/2018.
 */

public class TvShowWrapper {

    @SerializedName("results")
    private List<TvShow> tvShowList;

    public List<TvShow> getTvShowList() {
        return tvShowList;
    }

    public void setTvShowList(List<TvShow> tvShowList) {
        this.tvShowList = tvShowList;
    }

}
