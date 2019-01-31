package com.example.android.filmfun4me.activity.activity.detail.view;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.android.filmfun4me.R;
import com.example.android.filmfun4me.activity.activity.detail.presenter.DetailPresenter;
import com.squareup.picasso.Picasso;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ListReviewRecyclerAdapter extends RecyclerView.Adapter<ListReviewRecyclerAdapter.ViewHolder> {

    private DetailPresenter detailPresenter;
    private Context context;

    ListReviewRecyclerAdapter(DetailPresenter detailPresenter) {
        this.detailPresenter = detailPresenter;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // to use for picasso
        context = parent.getContext();
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

    class ViewHolder extends RecyclerView.ViewHolder implements DetailReviewItemView, View.OnClickListener {

        @BindView(R.id.review_item_container)
        ViewGroup reviewItemContainer;
        @BindView(R.id.tv_review_author)
        TextView tvReviewAuthor;
        @BindView(R.id.tv_review_content)
        TextView tvReviewContent;
        @BindView(R.id.ib_expand_content)
        ImageButton ibExpandReview;
        @BindView(R.id.ib_contract_content)
        ImageButton ibContractReview;

        ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            reviewItemContainer.setOnClickListener(this);
            ibExpandReview.setOnClickListener(this);
            ibContractReview.setOnClickListener(this);
        }

        @Override
        public void setReviewAuthorName(String authorName) {
            tvReviewAuthor.setText(authorName);
        }

        @Override
        public void setReviewContent(String reviewContent) {
            tvReviewContent.setText(reviewContent);
            setArrowDownIfExpandable(tvReviewContent);
        }

        @Override
        public void onClick(View v) {
            if (tvReviewContent.getMaxLines() == 3) {
                Picasso.with(context).load(android.R.drawable.arrow_up_float).into(ibExpandReview);
                tvReviewContent.setMaxLines(100);
                ibContractReview.setVisibility(View.VISIBLE);
            } else {
                Picasso.with(context).load(android.R.drawable.arrow_down_float).into(ibExpandReview);
                tvReviewContent.setMaxLines(3);
                ibContractReview.setVisibility(View.GONE);
            }
        }

        private void setArrowDownIfExpandable(TextView textView) {
            ViewTreeObserver vto = textView.getViewTreeObserver();
            vto.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
                @Override
                public void onGlobalLayout() {
                    Layout l = textView.getLayout();
                    if (l != null) {
                        int lines = l.getLineCount();
                        if (lines > 0)
                            if (l.getEllipsisCount(lines - 1) > 0)
                                Picasso.with(context).load(android.R.drawable.arrow_down_float).into(ibExpandReview);
                    }
                }
            });
        }
    }
}
