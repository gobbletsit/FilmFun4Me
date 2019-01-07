package com.example.android.filmfun4me.activity.activity.detail.view;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bignerdranch.expandablerecyclerview.Adapter.ExpandableRecyclerAdapter;
import com.bignerdranch.expandablerecyclerview.Model.ParentObject;
import com.example.android.filmfun4me.R;
import com.example.android.filmfun4me.activity.activity.detail.presenter.DetailPresenter;
import com.example.android.filmfun4me.data.Review;
import com.example.android.filmfun4me.data.ReviewChild;
import com.example.android.filmfun4me.data.ReviewParent;

import java.util.List;

public class ListReviewExpandableAdapter extends ExpandableRecyclerAdapter<ReviewParentViewHolder, ReviewChildViewHolder> {

    private LayoutInflater layoutInflater;

    public ListReviewExpandableAdapter(Context context, List<ParentObject> parentItemList) {
        super(context, parentItemList);
        this.layoutInflater = LayoutInflater.from(context);
    }

    @Override
    public ReviewParentViewHolder onCreateParentViewHolder(ViewGroup viewGroup) {
        View view = layoutInflater.inflate(R.layout.review_list_item_parent, viewGroup, false);
        return new ReviewParentViewHolder(view);
    }

    @Override
    public ReviewChildViewHolder onCreateChildViewHolder(ViewGroup viewGroup) {
        View view = layoutInflater.inflate(R.layout.review_list_item_child, viewGroup, false);
        return new ReviewChildViewHolder(view);
    }

    @Override
    public void onBindParentViewHolder(ReviewParentViewHolder reviewParentViewHolder, int i, Object o) {
        ReviewParent reviewParent = (ReviewParent) o;
        reviewParentViewHolder.reviewTitleTextView.setText("by " + reviewParent.getReviewTitle());
    }

    @Override
    public void onBindChildViewHolder(ReviewChildViewHolder reviewChildViewHolder, int i, Object o) {
        ReviewChild reviewChild = (ReviewChild) o;
        reviewChildViewHolder.reviewContentTextView.setText(reviewChild.getReviewContent());
    }
}
