package com.example.android.filmfun4me.activity.activity.detail.view;

import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.bignerdranch.expandablerecyclerview.ViewHolder.ParentViewHolder;
import com.example.android.filmfun4me.R;

import butterknife.BindView;
import butterknife.ButterKnife;

public class EpisodeParentViewHolder extends ParentViewHolder {


    public ImageView imv_episode_poster;
    public TextView tv_season_and_episode_number;
    public TextView tv_episode_title;
    public TextView tv_episode_air_date;
    public ImageButton ib_expand_arrow;

    public EpisodeParentViewHolder(View itemView) {
        super(itemView);
        //ButterKnife.bind(this,itemView);
        imv_episode_poster = itemView.findViewById(R.id.imv_episode_poster_parent);
        tv_season_and_episode_number = itemView.findViewById(R.id.tv_season_and_episode_number);
        tv_episode_title = itemView.findViewById(R.id.tv_episode_title_parent);
        tv_episode_air_date = itemView.findViewById(R.id.tv_air_date);
        ib_expand_arrow = itemView.findViewById(R.id.ib_expand_arrow);

    }
}
