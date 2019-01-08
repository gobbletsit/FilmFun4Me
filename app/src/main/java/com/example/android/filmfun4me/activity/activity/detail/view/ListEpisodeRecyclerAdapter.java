package com.example.android.filmfun4me.activity.activity.detail.view;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.android.filmfun4me.R;
import com.example.android.filmfun4me.activity.activity.detail.presenter.DetailPresenter;
import com.example.android.filmfun4me.utils.BaseUtils;
import com.squareup.picasso.Picasso;

public class ListEpisodeRecyclerAdapter extends RecyclerView.Adapter<ListEpisodeRecyclerAdapter.ItemViewHolder> {

    private Context context;
    private DetailPresenter detailPresenter;

    public ListEpisodeRecyclerAdapter (DetailPresenter detailPresenter){
        this.detailPresenter = detailPresenter;
    }

    @Override
    public ItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        context = parent.getContext();
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.episode_list_item, parent, false);
        return new ItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ItemViewHolder holder, int position) {
        detailPresenter.onBindEpisodeListItemOnPosition(position, holder);
    }

    @Override
    public int getItemCount() {
        return detailPresenter.getEpisodeListItemRowsCount();
    }

    class ItemViewHolder extends RecyclerView.ViewHolder implements DetailEpisodeItemView {

        private TextView tv_episode_title;
        private TextView tv_episode_overview;
        private ImageView iv_episode_poster;

        ItemViewHolder(View itemView) {
            super(itemView);
            this.tv_episode_title = itemView.findViewById(R.id.tv_episode_title);
            this.tv_episode_overview = itemView.findViewById(R.id.tv_episode_overview);
            this.iv_episode_poster = itemView.findViewById(R.id.imv_episode_poster);
        }

        @Override
        public void setEpisodeTitle(String episodeTitle) {
            tv_episode_title.setText(episodeTitle);
        }

        @Override
        public void setEpisodeOverview(String episodeOverview) {
            tv_episode_overview.setText(episodeOverview);
        }

        @Override
        public void setEpisodePoster(String episodePosterPath) {
            if (episodePosterPath != null){
                Picasso.with(context).load(BaseUtils.getPosterPath(episodePosterPath)).into(iv_episode_poster);
            } else {
                Picasso.with(context).load(R.drawable.poster_not_available).into(iv_episode_poster);
            }

        }
    }
}
