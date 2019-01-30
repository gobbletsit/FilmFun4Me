package com.example.android.filmfun4me.activity.activity.list.view;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.android.filmfun4me.R;
import com.example.android.filmfun4me.activity.activity.list.presenter.ListPresenter;
import com.example.android.filmfun4me.utils.BaseUtils;
import com.squareup.picasso.Picasso;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ListItemRecyclerAdapter extends RecyclerView.Adapter<ListItemRecyclerAdapter.ViewHolder> {

    private ListPresenter listPresenter;
    private Context context;

    public ListItemRecyclerAdapter(ListPresenter listPresenter) {
        this.listPresenter = listPresenter;
    }

    @Override
    public ListItemRecyclerAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        context = parent.getContext();
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.movie_list_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ListItemRecyclerAdapter.ViewHolder holder, int position) {
        listPresenter.onBindListItemAtPosition(position, holder);
    }

    @Override
    public int getItemCount() {
        return listPresenter.getListItemRowsCount();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, ListItemView {

        @BindView(R.id.list_item_container)
        ViewGroup container;
        @BindView(R.id.tv_movie_title)
        TextView tv_title;
        @BindView(R.id.imv_poster)
        ImageView iv_movie_poster;
        @BindView(R.id.tv_list_genre)
        TextView tv_genre;

        ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            this.container.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            listPresenter.onListItemInteraction(getAdapterPosition());
        }

        @Override
        public void setItemTitle(String itemTitle) {
            tv_title.setText(itemTitle);
        }

        @Override
        public void setGenreName(String genreName) {
            tv_genre.setText(genreName);
        }

        @Override
        public void setItemPoster(String posterPath) {
            Picasso.with(context).load(BaseUtils.getPosterPath(posterPath)).into(iv_movie_poster);
        }
    }
}

