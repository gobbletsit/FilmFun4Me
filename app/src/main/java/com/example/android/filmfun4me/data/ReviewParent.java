package com.example.android.filmfun4me.data;

import com.bignerdranch.expandablerecyclerview.Model.ParentObject;

import java.util.List;

public class ReviewParent implements ParentObject {

    private String reviewTitle;
    private String reviewContent;

    private List<Object> childrenList;

    public ReviewParent(String reviewTitle, String reviewContent) {
        this.reviewTitle = reviewTitle;
        this.reviewContent = reviewContent;
    }

    public String getReviewTitle() {
        return reviewTitle;
    }

    public String getReviewContent() {
        return reviewContent;
    }

    @Override
    public List<Object> getChildObjectList() {
        return childrenList;
    }

    @Override
    public void setChildObjectList(List<Object> list) {
        this.childrenList = list;
    }
}
