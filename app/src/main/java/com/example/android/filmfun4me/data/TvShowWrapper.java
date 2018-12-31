package com.example.android.filmfun4me.data;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by gobov on 1/12/2018.
 */

public class TvShowWrapper {

    @SerializedName("results")
    private List<TvShow> tvShowList;

    @SerializedName("id")
    private String id;

    @SerializedName("first_air_date")
    private String releaseDate;

    @SerializedName("poster_path")
    private String posterPath;

    @SerializedName("name")
    private String title;

    @SerializedName("vote_average")
    private double voteAverage;

    @SerializedName("overview")
    private String overview;

    @SerializedName("original_language")
    private String language;

    @SerializedName("genre_ids")
    private int[] genreIds;

    @SerializedName("number_of_seasons")
    private int number_of_seasons;

    @SerializedName("seasons")
    private List<Season> seasonList;

    @SerializedName("backdrop_path")
    private String backdropPath;

    public List<TvShow> getTvShowList() {
        return tvShowList;
    }

    public void setTvShowList(List<TvShow> tvShowList) {
        this.tvShowList = tvShowList;
    }

    // For details query
    public TvShow getTvShow() {
        return new TvShow(id, releaseDate, posterPath, title, voteAverage, overview, language, genreIds, number_of_seasons, backdropPath);
    }

    public List<Season> getSeasonList() {
        return seasonList;
    }
}
