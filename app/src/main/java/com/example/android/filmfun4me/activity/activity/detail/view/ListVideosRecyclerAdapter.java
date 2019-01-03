package com.example.android.filmfun4me.activity.activity.detail.view;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.android.filmfun4me.R;
import com.example.android.filmfun4me.activity.activity.detail.presenter.DetailPresenter;
import com.example.android.filmfun4me.data.Video;
import com.squareup.picasso.Picasso;

public class ListVideosRecyclerAdapter extends RecyclerView.Adapter<ListVideosRecyclerAdapter.ViewHolder> {

    private Context context;
    private DetailPresenter detailPresenter;

    public ListVideosRecyclerAdapter (DetailPresenter detailPresenter){
        this.detailPresenter = detailPresenter;
    }

    @Override
    public ListVideosRecyclerAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        context = parent.getContext();
        View view = LayoutInflater.from(context).inflate(R.layout.video_list_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ListVideosRecyclerAdapter.ViewHolder holder, int position) {
        detailPresenter.onBindVideoListItemOnPosition(position, holder);
    }

    @Override
    public int getItemCount() {
        return detailPresenter.getVideoListItemRowsCount();
    }

    class ViewHolder extends RecyclerView.ViewHolder implements DetailVIdeoItemView, View.OnClickListener {

        ImageView videoImageView;
        TextView videoTitleTextView;
        ViewGroup listVideoItemContainer;

        ViewHolder(View itemView) {
            super(itemView);
            videoImageView = itemView.findViewById(R.id.image_view_video_poster);
            videoTitleTextView = itemView.findViewById(R.id.text_view_video_title);
            listVideoItemContainer = itemView.findViewById(R.id.video_list_item_container);
            listVideoItemContainer.setOnClickListener(this);
        }

        @Override
        public void setImageViewVideoTag(String videoUrl) {
            videoImageView.setTag(videoUrl);
            listVideoItemContainer.setTag(videoUrl);
        }

        @Override
        public void setImageViewVideoThumbnailUrl(String videoThumbnailUrl) {
            Picasso.with(context).load(videoThumbnailUrl).into(videoImageView);
        }

        @Override
        public void setVideoTitle(String videoTitle) {
            videoTitleTextView.setText(videoTitle);
        }

        @Override
        public void onClick(View v) {
            detailPresenter.whenTrailerClicked(v);
        }
    }
}
