package com.example.android.filmfun4me.data;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by gobov on 1/5/2018.
 */

public class ReviewWrapper {

    @SerializedName("results")
    private List<Review> reviewList;

    public List<Review> getReviewList() {
        return reviewList;
    }

    public void setReviewList(List<Review> reviewList) {
        this.reviewList = reviewList;
    }
}
