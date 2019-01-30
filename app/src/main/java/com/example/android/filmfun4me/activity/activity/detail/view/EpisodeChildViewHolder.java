package com.example.android.filmfun4me.activity.activity.detail.view;

import android.view.View;
import android.widget.TextView;

import com.bignerdranch.expandablerecyclerview.ViewHolder.ChildViewHolder;
import com.example.android.filmfun4me.R;

class EpisodeChildViewHolder extends ChildViewHolder {

    TextView episodeOverview;

    EpisodeChildViewHolder(View itemView) {
        super(itemView);
        episodeOverview = itemView.findViewById(R.id.tv_episode_overview_parent);
    }

}
