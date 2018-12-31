package com.example.android.filmfun4me.data;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by gobov on 12/21/2017.
 */

public class Movie implements Parcelable {


    @SerializedName("id")
    private String id;

    @SerializedName("release_date")
    private String releaseDate;

    @SerializedName("poster_path")
    private String posterPath;

    @SerializedName("title")
    private String title;

    @SerializedName("vote_average")
    private double voteAverage;

    @SerializedName("overview")
    private String overview;

    @SerializedName("revenue")
    private long revenue;

    @SerializedName("original_language")
    private String language;

    @SerializedName("genre_ids")
    private int[] genreIds;

    @SerializedName("backdrop_path")
    private String backdropPath;

    public Movie() {
    }

    // Has to be read in the same order as it is written in "writeToParcel" method
    protected Movie(Parcel in) {
        id = in.readString();
        releaseDate = in.readString();
        posterPath = in.readString();
        title = in.readString();
        voteAverage = in.readDouble();
        overview = in.readString();
        revenue = in.readLong();
        language = in.readString();
        genreIds = in.createIntArray();
        backdropPath = in.readString();

    }

    public static final Creator<Movie> CREATOR = new Creator<Movie>() {
        @Override
        public Movie createFromParcel(Parcel parcel) {
            return new Movie(parcel);
        }

        @Override
        public Movie[] newArray(int i) {
            return new Movie[i];
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

    public long getRevenue() {
        return revenue;
    }

    public void setRevenue(int revenue) {
        this.revenue = revenue;
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

    public String getBackdropPath() {
        return backdropPath;
    }

    public void setBackdropPath(String backdropPath) {
        this.backdropPath = backdropPath;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(id);
        parcel.writeString(releaseDate);
        parcel.writeString(posterPath);
        parcel.writeString(title);
        parcel.writeDouble(voteAverage);
        parcel.writeString(overview);
        parcel.writeLong(revenue);
        parcel.writeString(language);
        parcel.writeIntArray(genreIds);
        parcel.writeString(backdropPath);
    }
}
