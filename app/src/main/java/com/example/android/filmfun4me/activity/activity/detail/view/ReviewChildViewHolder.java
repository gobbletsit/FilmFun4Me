package com.example.android.filmfun4me.activity.activity.detail.view;

import android.view.View;
import android.widget.TextView;

import com.bignerdranch.expandablerecyclerview.ViewHolder.ChildViewHolder;
import com.example.android.filmfun4me.R;

class ReviewChildViewHolder extends ChildViewHolder {

    public TextView reviewContentTextView;

    public ReviewChildViewHolder(View itemView) {
        super(itemView);
        reviewContentTextView = itemView.findViewById(R.id.child_review_item_content_text_view);
    }
}
