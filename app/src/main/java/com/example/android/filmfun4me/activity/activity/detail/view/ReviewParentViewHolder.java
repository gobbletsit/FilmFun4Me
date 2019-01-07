package com.example.android.filmfun4me.activity.activity.detail.view;

import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import com.bignerdranch.expandablerecyclerview.ViewHolder.ParentViewHolder;
import com.example.android.filmfun4me.R;

class ReviewParentViewHolder extends ParentViewHolder {

    TextView reviewTitleTextView;
    public ImageButton dropdownButton;

    ReviewParentViewHolder(View itemView) {
        super(itemView);

        reviewTitleTextView = itemView.findViewById(R.id.parent_review_item_title_text_view);
        dropdownButton = itemView.findViewById(R.id.parent_review_item_expand_arrow);
    }
}
