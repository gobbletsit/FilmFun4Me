package com.example.android.filmfun4me.data;

import android.os.Parcel;
import android.os.Parcelable;

import com.example.android.filmfun4me.utils.BaseUtils;
import com.google.gson.annotations.SerializedName;

/**
 * Created by gobov on 12/29/2017.
 */

public class Video implements Parcelable {

    private static final String YOU_TUBE = "YouTube";

    @SerializedName("key")
    private String key;
    private String id;
    private String site;
    private int size;

    // Has to be read in the same order as it is written in "writeToParcel" method
    private Video(Parcel in) {
        this.id = in.readString();
        this.key = in.readString();
        this.site = in.readString();
        this.size = in.readInt();
    }

    public static final Creator<Video> CREATOR = new Creator<Video>() {
        @Override
        public Video createFromParcel(Parcel source) {
            return new Video(source);
        }

        @Override
        public Video[] newArray(int size) {
            return new Video[size];
        }
    };

    public static String getUrl(Video video) {
        if (YOU_TUBE.equalsIgnoreCase(video.getSite())) {
            return String.format(BaseUtils.YOUTUBE_VIDEO_URL, video.getKey());
        } else {
            return "";
        }
    }

    public static String getThumbnailUrl(Video video) {
        if (YOU_TUBE.equalsIgnoreCase(video.getSite())) {
            return String.format(BaseUtils.YOUTUBE_THUMBNAIL_URL, video.getKey());
        } else {
            return "";
        }
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getSite() {
        return site;
    }

    public void setSite(String site) {
        this.site = site;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(key);
        dest.writeString(id);
        dest.writeString(site);
        dest.writeInt(size);
    }
}
