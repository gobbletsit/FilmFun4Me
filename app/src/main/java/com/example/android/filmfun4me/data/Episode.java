package com.example.android.filmfun4me.data;

import android.os.Parcel;
import android.os.Parcelable;

import com.bignerdranch.expandablerecyclerview.Model.ParentObject;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by gobov on 1/27/2018.
 */

public class Episode implements Parcelable, ParentObject {

    @SerializedName("name")
    private String name;

    @SerializedName("overview")
    private String overview;

    @SerializedName("air_date")
    private String airDate;

    @SerializedName("vote_average")
    private double voteAverage;

    @SerializedName("still_path")
    private String posterPath;

    @SerializedName("episode_number")
    private int episodeNumber;

    @SerializedName("season_number")
    private int seasonNumber;

    private List<Object> childrenList;

    public Episode() {
    }

    // Has to be read in the same order as it is written in "writeToParcel" method
    protected Episode(Parcel in) {
        this.name = in.readString();
        this.overview = in.readString();
        this.airDate = in.readString();
        this.voteAverage = in.readDouble();
        this.posterPath = in.readString();
    }

    public static final Creator<Episode> CREATOR = new Creator<Episode>() {
        @Override
        public Episode createFromParcel(Parcel in) {
            return new Episode(in);
        }

        @Override
        public Episode[] newArray(int i) {
            return new Episode[i];
        }
    };

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getOverview() {
        return overview;
    }

    public void setOverview(String overview) {
        this.overview = overview;
    }

    public String getAirDate() {
        return airDate;
    }

    public void setAirDate(String airDate) {
        this.airDate = airDate;
    }

    public double getVoteAverage() {
        return voteAverage;
    }

    public void setVoteAverage(double voteAverage) {
        this.voteAverage = voteAverage;
    }

    public String getPosterPath() {
        return posterPath;
    }

    public void setPosterPath(String posterPath) {
        this.posterPath = posterPath;
    }

    public int getEpisodeNumber() {
        return episodeNumber;
    }

    public void setEpisodeNumber(int episodeNumber) {
        this.episodeNumber = episodeNumber;
    }

    public int getSeasonNumber() {
        return seasonNumber;
    }

    public void setSeasonNumber(int seasonNumber) {
        this.seasonNumber = seasonNumber;
    }

    @Override
    public int describeContents() {
        return 0;
    }


    @Override
    public void writeToParcel(Parcel dest, int flags) {

        dest.writeString(name);
        dest.writeString(overview);
        dest.writeString(airDate);
        dest.writeDouble(voteAverage);
        dest.writeString(posterPath);
    }

    // expandable recycler adapter
    @Override
    public List<Object> getChildObjectList() {
        return childrenList;
    }

    // expandable recycler adapter
    @Override
    public void setChildObjectList(List<Object> list) {
        this.childrenList = list;
    }
}
