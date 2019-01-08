package com.example.android.filmfun4me.activity.activity.detail.view;

import android.view.View;
import android.widget.TextView;

import com.bignerdranch.expandablerecyclerview.ViewHolder.ChildViewHolder;
import com.example.android.filmfun4me.R;

public class EpisodeChildViewHolder extends ChildViewHolder {

    public TextView episodeOverview;

    public EpisodeChildViewHolder(View itemView) {
        super(itemView);
        episodeOverview = itemView.findViewById(R.id.tv_episode_overview_parent);
    }

}
