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
import com.example.android.filmfun4me.utils.BaseUtils;
import com.squareup.picasso.Picasso;

import java.util.List;

import javax.inject.Inject;

public class ListMovieRecyclerAdapter extends RecyclerView.Adapter<ListMovieRecyclerAdapter.ViewHolder> {

    private ListPresenter listPresenter;

    private List<Movie> movieList;
    private List<Genre> genreList;
    private Context context;


    ListMovieRecyclerAdapter (List<Movie> movieList, List<Genre> genreList, ListPresenter listPresenter){
        this.movieList = movieList;
        this.genreList = genreList;
        this.listPresenter = listPresenter;
    }


    @Override
    public ListMovieRecyclerAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        context = parent.getContext();
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.movie_list_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ListMovieRecyclerAdapter.ViewHolder holder, int position) {

        holder.movie = movieList.get(position);

        holder.currentGenreIds = holder.movie.getGenreIds();

        // pricekaj za ovo
        String genreName = getSingleGenreName(holder.currentGenreIds, genreList);
        holder.tv_genre.setText(genreName);
        holder.tv_title.setText(holder.movie.getTitle());

        Picasso.with(context).load(BaseUtils.getPosterPath(holder.movie.getPosterPath())).into(holder.iv_movie_poster);

    }

    @Override
    public int getItemCount() {
        return movieList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private int[] currentGenreIds;
        private Movie movie;

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
            listPresenter.whenMovieClicked(movie, genreList);
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
