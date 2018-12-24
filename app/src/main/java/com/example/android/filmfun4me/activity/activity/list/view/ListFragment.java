package com.example.android.filmfun4me.activity.activity.list.view;


import android.content.Context;
import android.content.Intent;
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
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.OvershootInterpolator;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.android.filmfun4me.BaseApplication;
import com.example.android.filmfun4me.R;
import com.example.android.filmfun4me.activity.activity.detail.view.DetailActivity;
import com.example.android.filmfun4me.activity.activity.list.presenter.ListPresenter;
import com.example.android.filmfun4me.data.Genre;
import com.example.android.filmfun4me.data.Movie;
import com.example.android.filmfun4me.utils.BaseUtils;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import jp.wasabeef.recyclerview.animators.adapters.ScaleInAnimationAdapter;


public class ListFragment extends Fragment implements ListView {

    private static final String TAG = ListFragment.class.getSimpleName();

    @Inject
    ListPresenter listPresenter;

    private static final String KEY_MOVIE = "movie";
    private static final String KEY_THEME_COLOR_MOVIE = "theme_color_movie";
    private static final String KEY_GENRE_NAMES_LIST = "genreNames";

    RecyclerView recyclerView;
    RecyclerView.Adapter customAdapter;
    LayoutInflater layoutInflater;

    private String genreName;

    private List<Movie> movieList = new ArrayList<>(40);

    private List<Genre> genreList = new ArrayList<>(40);

    // To store for a single movie and pass it to detail activity
    private ArrayList<String> singleGenreNamesList = new ArrayList<>(20);

    private LinearLayoutManager layoutManager;

    private int themeColor;

    private static final String PAGER_POSITION = "pager_position";
    private int pagerPosition;

    private ScaleInAnimationAdapter scaleInAnimationAdapter;

    public ListFragment() {
        // Required empty public constructor
    }


    public static ListFragment newInstance(int position) {
        ListFragment listFragment = new ListFragment();
        Bundle args = new Bundle();
        args.putInt(PAGER_POSITION, position);
        listFragment.setArguments(args);
        return listFragment;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (savedInstanceState != null){
            pagerPosition = (int) savedInstanceState.get(PAGER_POSITION);
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setRetainInstance(true);

        ((BaseApplication) getActivity().getApplication()).createListComponent().inject(this);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_list, container, false);

        recyclerView = view.findViewById(R.id.rec_list_activity);
        layoutInflater = getActivity().getLayoutInflater();

        customAdapter = new CustomAdapter();

        scaleInAnimationAdapter = new ScaleInAnimationAdapter(customAdapter);
        scaleInAnimationAdapter.setDuration(400);
        scaleInAnimationAdapter.setInterpolator(new OvershootInterpolator());
        // disable the first scroll mode
        scaleInAnimationAdapter.setFirstOnly(false);
        recyclerView.setAdapter(scaleInAnimationAdapter);

        // layoutManager = new GridLayoutManager(getActivity(), 2);
        layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);

        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(recyclerView.getContext(), layoutManager.getOrientation());
        dividerItemDecoration.setDrawable(ContextCompat.getDrawable(getActivity(), R.drawable.recycler_divider_with_spacing));
        recyclerView.addItemDecoration(dividerItemDecoration);

        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        if (getArguments() != null && getArguments().containsKey(PAGER_POSITION)){
            pagerPosition = (int) getArguments().get(PAGER_POSITION);

            if (isNetworkAvailable()) {
                listPresenter.setMovieView(this, pagerPosition);
            }
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
        scaleInAnimationAdapter.notifyDataSetChanged();

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

    @Override
    public void setViewColors(RecyclerView recyclerView, DividerItemDecoration dividerItemDecoration, int themeColor) {
        // nothing
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

            // dont need this for now
            // genreName = getSingleGenreName(holder.currentGenreIds, genreList);
            holder.tv_title.setText(holder.movie.getTitle());

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

            ViewHolder(View itemView) {
                super(itemView);
                this.container = itemView.findViewById(R.id.list_item_container);
                this.tv_title = itemView.findViewById(R.id.tv_movie_title);
                this.iv_movie_poster = itemView.findViewById(R.id.imv_poster);
                this.container.setOnClickListener(this);
            }

            @Override
            public void onClick(View view) {
                listPresenter.whenMovieClicked(movie, genreList);
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

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(PAGER_POSITION,pagerPosition);
    }
}
