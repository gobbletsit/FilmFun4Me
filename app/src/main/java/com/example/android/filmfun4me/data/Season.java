package com.example.android.filmfun4me.data;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

/**
 * Created by gobov on 1/23/2018.
 */

public class Season implements Parcelable {

    @SerializedName("air_date")
    private String airDate;

    @SerializedName("episode_count")
    private int episodeCount;

    @SerializedName("id")
    private int id;

    @SerializedName("season_number")
    private int seasonNumber;

    public Season() {
    }

    // Has to be read in the same order as it is written in "writeToParcel" method
    protected Season(Parcel in) {
        airDate = in.readString();
        episodeCount = in.readInt();
        id = in.readInt();
        seasonNumber = in.readInt();
    }


    public static final Creator<Season> CREATOR = new Creator<Season>() {
        @Override
        public Season createFromParcel(Parcel source) {
            return new Season(source);
        }

        @Override
        public Season[] newArray(int i) {
            return new Season[i];
        }
    };


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {

        dest.writeString(airDate);
        dest.writeInt(episodeCount);
        dest.writeInt(id);
        dest.writeInt(seasonNumber);
    }

    public String getAirDate() {
        return airDate;
    }

    public void setAirDate(String airDate) {
        this.airDate = airDate;
    }

    public int getEpisodeCount() {
        return episodeCount;
    }

    public void setEpisodeCount(int episode_count) {
        this.episodeCount = episode_count;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getSeasonNumber() {
        return seasonNumber;
    }

    public void setSeasonNumber(int seasonNumber) {
        this.seasonNumber = seasonNumber;
    }
}
