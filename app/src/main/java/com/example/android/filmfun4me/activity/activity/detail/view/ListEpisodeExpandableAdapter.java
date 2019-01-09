package com.example.android.filmfun4me.activity.activity.detail.view;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bignerdranch.expandablerecyclerview.Adapter.ExpandableRecyclerAdapter;
import com.bignerdranch.expandablerecyclerview.Model.ParentObject;
import com.example.android.filmfun4me.R;
import com.example.android.filmfun4me.data.Episode;
import com.example.android.filmfun4me.data.EpisodeChild;
import com.example.android.filmfun4me.utils.BaseUtils;
import com.example.android.filmfun4me.utils.DateUtils;
import com.squareup.picasso.Picasso;

import java.util.List;

public class ListEpisodeExpandableAdapter extends ExpandableRecyclerAdapter<EpisodeParentViewHolder, EpisodeChildViewHolder> {

    private LayoutInflater layoutInflater;
    private Context context;

    public ListEpisodeExpandableAdapter(Context context, List<ParentObject> parentItemList) {
        super(context, parentItemList);
        this.layoutInflater = LayoutInflater.from(context);
        this.context = context;
    }

    @Override
    public EpisodeParentViewHolder onCreateParentViewHolder(ViewGroup viewGroup) {
        View view = layoutInflater.inflate(R.layout.episode_list_item_parent, viewGroup, false);
        return new EpisodeParentViewHolder(view);
    }

    @Override
    public EpisodeChildViewHolder onCreateChildViewHolder(ViewGroup viewGroup) {
        View view = layoutInflater.inflate(R.layout.episode_list_item_child, viewGroup, false);
        return new EpisodeChildViewHolder(view);
    }

    @Override
    public void onBindParentViewHolder(EpisodeParentViewHolder episodeParentViewHolder, int i, Object o) {
        Episode episode = (Episode)o;
        String posterPath = episode.getPosterPath();
        if (posterPath != null){
            Picasso.with(context).load(BaseUtils.getPosterPath(posterPath)).into(episodeParentViewHolder.imv_episode_poster);
        } else {
            Picasso.with(context).load(R.drawable.poster_not_available).into(episodeParentViewHolder.imv_episode_poster);
        }
        episodeParentViewHolder.tv_season_and_episode_number.setText(formatSeasonAndEpisodeString(String.valueOf(episode.getSeasonNumber()), String.valueOf(episode.getEpisodeNumber())));
        episodeParentViewHolder.tv_episode_title.setText(episode.getName());
        episodeParentViewHolder.tv_episode_air_date.setText(DateUtils.formatDate(episode.getAirDate(), getClass().getSimpleName()));

    }

    @Override
    public void onBindChildViewHolder(EpisodeChildViewHolder episodeChildViewHolder, int i, Object o) {
        EpisodeChild episodeChild = (EpisodeChild) o;
        episodeChildViewHolder.episodeOverview.setText(episodeChild.getEpisodeOverview());
    }

    private String formatSeasonAndEpisodeString(String seasonNumber, String episodeNumber){
        return "SO" + seasonNumber + " EP" + episodeNumber;
    }
}
