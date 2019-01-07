package com.example.android.filmfun4me.data;

import android.widget.TextView;

public class ReviewChild {

    private String reviewContent;

    public ReviewChild (String reviewContent){
        this.reviewContent = reviewContent;
    }

    public String getReviewContent() {
        return reviewContent;
    }

    public void setReviewContent(String reviewContent) {
        this.reviewContent = reviewContent;
    }
}
