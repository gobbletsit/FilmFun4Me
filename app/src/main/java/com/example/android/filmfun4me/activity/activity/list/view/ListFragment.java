package com.example.android.filmfun4me.activity.activity.list.view;


import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.android.filmfun4me.BaseApplication;
import com.example.android.filmfun4me.R;
import com.example.android.filmfun4me.activity.activity.detail.view.DetailActivity;
import com.example.android.filmfun4me.activity.activity.list.presenter.ListPresenter;
import com.example.android.filmfun4me.data.Genre;
import com.example.android.filmfun4me.data.Movie;
import com.example.android.filmfun4me.data.TvShow;
import com.example.android.filmfun4me.utils.BaseUtils;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;


public class ListFragment extends Fragment implements ListView {

    @Inject
    ListPresenter listPresenter;

    private static final String KEY_MOVIE = "movie";
    private static final String KEY_THEME_COLOR_MOVIE = "theme_color_movie";
    private static final String KEY_GENRE_NAMES_LIST = "genreNames";

    RecyclerView recyclerView;
    RecyclerView.Adapter adapter;
    LayoutInflater layoutInflater;

    private String genreName;

    private List<Movie> movieList = new ArrayList<>(40);

    private List<Genre> genreList = new ArrayList<>(40);

    // To store for a single movie ans pass it to detail activity
    private ArrayList<String> singleGenreNamesList = new ArrayList<>(20);

    private GridLayoutManager layoutManager;

    private int themeColor;


    public ListFragment() {
        // Required empty public constructor
    }


    public static ListFragment newInstance() {
        return new ListFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setRetainInstance(true);

        ((BaseApplication) getActivity().getApplication()).createListComponent().inject(this);

        //fragmentPosition = getArguments().getInt("position");

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_list, container, false);

        recyclerView = view.findViewById(R.id.rec_list_activity);
        layoutInflater = getActivity().getLayoutInflater();

        // WITH NUMBER OF COLUMNS
        layoutManager = new GridLayoutManager(getActivity(), 2);

        recyclerView.setLayoutManager(layoutManager);
        adapter = new CustomAdapter();
        recyclerView.setAdapter(adapter);

        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        if (isNetworkAvailable()) {
            listPresenter.setMovieView(this);
            listPresenter.setListColors(recyclerView, layoutManager, themeColor);
        }

    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        listPresenter.destroy();
        genreList.clear();
        movieList.clear();
        singleGenreNamesList.clear();

    }

    @Override
    public void setUpMovieView(List<Movie> movieList) {
        this.movieList.clear();
        this.movieList.addAll(movieList);
        adapter.notifyDataSetChanged();

    }


    @Override
    public void onMovieClicked(Movie movie, List<Genre> genreList, ArrayList<String> singleGenreNamesList) {

        Intent startDetailIntent = new Intent(getActivity(), DetailActivity.class);
        startDetailIntent.putExtra(KEY_MOVIE, movie);
        startDetailIntent.putExtra(KEY_THEME_COLOR_MOVIE, themeColor);
        startDetailIntent.putStringArrayListExtra(KEY_GENRE_NAMES_LIST, singleGenreNamesList);
        startActivity(startDetailIntent);
    }



    @Override
    public void showLoading() {

    }

    @Override
    public void loadingErrorMessage(String error) {
        Toast.makeText(getActivity(), error, Toast.LENGTH_LONG).show();
    }

    @Override
    public void loadUpAllGenreList(List<Genre> genreList) {
        this.genreList.clear();
        this.genreList.addAll(genreList);
    }


    @Override
    public String getSingleGenreName(int[] currentGenreIds, List<Genre> genreList) {

        int singleGenreId;

        String genreName = "";

        for (int i = 0; i < currentGenreIds.length; i++) {
            singleGenreId = currentGenreIds[i];

            for (int a = 0; a < genreList.size(); a++) {
                int preciseGenreId = genreList.get(a).getGenreId();

                if (preciseGenreId == singleGenreId) {
                    genreName = genreList.get(a).getGenreName();
                }
            }
        }
        return genreName;
    }

    @Override
    public void setViewColors(RecyclerView recyclerView, DividerItemDecoration dividerItemDecoration, int themeColor) {
        this.themeColor = themeColor;
        recyclerView.addItemDecoration(dividerItemDecoration);
    }


    private class CustomAdapter extends RecyclerView.Adapter<CustomAdapter.ViewHolder> {


        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = layoutInflater.inflate(R.layout.movie_list_item, parent, false);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(ViewHolder holder, int position) {

            holder.movie = movieList.get(position);

            holder.currentGenreIds = holder.movie.getGenreIds();

            genreName = getSingleGenreName(holder.currentGenreIds, genreList);
            holder.tv_title.setText(holder.movie.getTitle());
            holder.tv_title.setTextColor(themeColor);

            Picasso.with(getActivity()).load(BaseUtils.getPosterPath(holder.movie.getPosterPath())).into(holder.iv_movie_poster);

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

            public ViewHolder(View itemView) {
                super(itemView);
                this.container = itemView.findViewById(R.id.list_item_container);
                this.tv_title = itemView.findViewById(R.id.tv_movie_title);
                this.iv_movie_poster = itemView.findViewById(R.id.imv_poster);
                this.container.setOnClickListener(this);
            }

            @Override
            public void onClick(View view) {
                listPresenter.whenMovieClicked(movie, genreList, singleGenreNamesList);
            }
        }
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        ((BaseApplication) getActivity().getApplication()).releaseListComponent();
        singleGenreNamesList.clear();
    }

    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager = (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();

        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }


}
