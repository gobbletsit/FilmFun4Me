package com.example.android.filmfun4me.data;

import android.os.IBinder;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by gobov on 1/12/2018.
 */

public class TvShow implements Parcelable {

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

    @SerializedName("backdrop_path")
    private String backdropPath;

    @SerializedName("seasons")
    private List<Season> seasonList;

    // Has to be read in the same order as it is written in "writeToParcel" method
    protected TvShow(Parcel in) {
        id = in.readString();
        posterPath = in.readString();
        title = in.readString();
        genreIds = in.createIntArray();
    }

    public static final Creator<TvShow> CREATOR = new Creator<TvShow>() {
        @Override
        public TvShow createFromParcel(Parcel parcel) {
            return new TvShow(parcel);
        }

        @Override
        public TvShow[] newArray(int i) {
            return new TvShow[i];
        }
    };

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getReleaseDate() {
        return releaseDate;
    }

    public void setReleaseDate(String releaseDate) {
        this.releaseDate = releaseDate;
    }

    public String getPosterPath() {
        return posterPath;
    }

    public void setPosterPath(String posterPath) {
        this.posterPath = posterPath;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public double getVoteAverage() {
        return voteAverage;
    }

    public void setVoteAverage(double voteAverage) {
        this.voteAverage = voteAverage;
    }

    public String getOverview() {
        return overview;
    }

    public void setOverview(String overview) {
        this.overview = overview;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public int[] getGenreIds() {
        return genreIds;
    }

    public void setGenreIds(int[] genreIds) {
        this.genreIds = genreIds;
    }


    public int getNumberOfSeasons() {
        return number_of_seasons;
    }

    public void setNumberOfSeasons(int numberOfSeasons) {
        this.number_of_seasons = numberOfSeasons;
    }

    public String getBackdropPath() {
        return backdropPath;
    }

    public void setBackdropPath(String backdropPath) {
        this.backdropPath = backdropPath;
    }

    public List<Season> getSeasonList() {
        return seasonList;
    }

    public void setSeasonList(List<Season> seasonList) {
        this.seasonList = seasonList;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(id);
        parcel.writeString(posterPath);
        parcel.writeString(title);
        parcel.writeIntArray(genreIds);
    }
}
