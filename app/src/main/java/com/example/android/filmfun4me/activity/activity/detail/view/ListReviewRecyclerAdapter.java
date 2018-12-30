package com.example.android.filmfun4me.activity.activity.detail.view;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.android.filmfun4me.R;
import com.example.android.filmfun4me.activity.activity.detail.presenter.DetailPresenter;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ListReviewRecyclerAdapter extends RecyclerView.Adapter<ListReviewRecyclerAdapter.ViewHolder> {

    private DetailPresenter detailPresenter;

    public ListReviewRecyclerAdapter(DetailPresenter detailPresenter){
        this.detailPresenter = detailPresenter;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.review_list_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        detailPresenter.onBindReviewListItemOnPosition(position, holder);
    }

    @Override
    public int getItemCount() {
        return detailPresenter.getReviewListItemRowsCount();
    }

    class ViewHolder extends RecyclerView.ViewHolder implements DetailReviewItemView {

        @BindView(R.id.tv_review_author) TextView tvReviewAuthor;
        @BindView(R.id.tv_review_content) TextView tvReviewContent;

        ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        @Override
        public void setReviewAuthorName(String authorName) {
            tvReviewAuthor.setText(authorName);
        }

        @Override
        public void setReviewContent(String reviewContent) {
            tvReviewContent.setText(reviewContent);
        }
    }
}
