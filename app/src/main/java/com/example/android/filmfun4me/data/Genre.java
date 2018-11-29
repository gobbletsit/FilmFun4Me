package com.example.android.filmfun4me.data;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

/**
 * Created by gobov on 1/9/2018.
 */

public class Genre implements Parcelable {

    @SerializedName("id")
    private int genreId;

    @SerializedName("name")
    private String genreName;

    // Has to be read in the same order as it is written in "writeToParcel" method
    protected Genre(Parcel in) {
        genreId = in.readInt();
        genreName = in.readString();
    }

    public static final Creator CREATOR = new Creator() {
        @Override
        public Object createFromParcel(Parcel source) {
            return new Genre(source);
        }

        @Override
        public Object[] newArray(int i) {
            return new Genre[i];
        }
    };

    public int getGenreId() {
        return genreId;
    }

    public void setGenreId(int genreId) {
        this.genreId = genreId;
    }

    public String getGenreName() {
        return genreName;
    }

    public void setGenreName(String genreName) {
        this.genreName = genreName;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(genreId);
        dest.writeString(genreName);
    }
}
