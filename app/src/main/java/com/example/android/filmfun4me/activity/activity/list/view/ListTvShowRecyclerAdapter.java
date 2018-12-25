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
import com.example.android.filmfun4me.data.Genre;
import com.example.android.filmfun4me.data.Movie;
import com.example.android.filmfun4me.data.TvShow;
import com.example.android.filmfun4me.utils.BaseUtils;
import com.squareup.picasso.Picasso;

import java.util.List;

public class ListTvShowRecyclerAdapter extends RecyclerView.Adapter<ListTvShowRecyclerAdapter.ViewHolder> {

    private List<TvShow> tvShowList;
    private List<Genre> genreList;
    private Context context;


    ListTvShowRecyclerAdapter (List<TvShow> tvShowList, List<Genre> genreList){
        this.tvShowList = tvShowList;
        this.genreList = genreList;
    }


    @Override
    public ListTvShowRecyclerAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        context = parent.getContext();
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.movie_list_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ListTvShowRecyclerAdapter.ViewHolder holder, int position) {

        holder.tvShow = tvShowList.get(position);

        holder.currentGenreIds = holder.tvShow.getGenreIds();

        // pricekaj za ovo
        String genreName = getSingleGenreName(holder.currentGenreIds, genreList);
        holder.tv_genre.setText(genreName);

        holder.tv_title.setText(holder.tvShow.getTitle());

        Picasso.with(context).load(BaseUtils.getPosterPath(holder.tvShow.getPosterPath())).into(holder.iv_movie_poster);

    }

    @Override
    public int getItemCount() {
        return tvShowList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private int[] currentGenreIds;
        private TvShow tvShow;

        private ViewGroup container;
        private TextView tv_title;
        private ImageView iv_movie_poster;
        private TextView tv_genre;

        ViewHolder(View itemView) {
            super(itemView);
            this.container = itemView.findViewById(R.id.list_item_container);
            this.tv_title = itemView.findViewById(R.id.tv_movie_title);
            this.iv_movie_poster = itemView.findViewById(R.id.imv_poster);
            this.tv_genre = itemView.findViewById(R.id.tv_list_genre);
            this.container.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            // listPresenter.whenMovieClicked(tvShow, genreList);
        }
    }

    private String getSingleGenreName(int[] currentGenreIds, List<Genre> genreList) {

        int singleGenreId;

        String genreName = "";

        for (int currentGenreId : currentGenreIds) {
            singleGenreId = currentGenreId;

            for (int a = 0; a < genreList.size(); a++) {
                int preciseGenreId = genreList.get(a).getGenreId();

                if (preciseGenreId == singleGenreId) {
                    genreName = genreList.get(a).getGenreName();
                }
            }
        }
        return genreName;
    }
}


