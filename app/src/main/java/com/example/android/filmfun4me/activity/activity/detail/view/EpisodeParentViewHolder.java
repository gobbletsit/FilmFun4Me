package com.example.android.filmfun4me.activity.activity.detail.view;

import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.bignerdranch.expandablerecyclerview.ViewHolder.ParentViewHolder;
import com.example.android.filmfun4me.R;

import butterknife.BindView;
import butterknife.ButterKnife;

class EpisodeParentViewHolder extends ParentViewHolder {

    @BindView(R.id.imv_episode_poster_parent)
    ImageView imv_episode_poster;
    @BindView(R.id.tv_season_and_episode_number)
    TextView tv_season_and_episode_number;
    @BindView(R.id.tv_episode_title_parent)
    TextView tv_episode_title;
    @BindView(R.id.tv_air_date)
    TextView tv_episode_air_date;
    @BindView(R.id.ib_expand_arrow)
    ImageButton ib_expand_arrow;

    EpisodeParentViewHolder(View itemView) {
        super(itemView);
        ButterKnife.bind(this,itemView);
    }
}
