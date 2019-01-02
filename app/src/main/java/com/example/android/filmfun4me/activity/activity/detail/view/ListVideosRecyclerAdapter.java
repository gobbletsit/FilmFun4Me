package com.example.android.filmfun4me.activity.activity.detail.view;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.android.filmfun4me.data.Video;

public class ListVideosRecyclerAdapter extends RecyclerView.Adapter<ListVideosRecyclerAdapter.ViewHolder> {

    private Context context;

    @Override
    public ListVideosRecyclerAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        context = parent.getContext();
        return null;
    }

    @Override
    public void onBindViewHolder(ListVideosRecyclerAdapter.ViewHolder holder, int position) {
        // onBindItemAtPosition
    }

    @Override
    public int getItemCount() {
        return 0;
        // getRowsItemCount
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        ImageView videoImageView;
        TextView videoTitleTextView;

        ViewHolder(View itemView) {
            super(itemView);
        }
    }
}
