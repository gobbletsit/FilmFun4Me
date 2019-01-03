package com.example.android.filmfun4me.activity.activity.detail.view;


import android.app.ActionBar;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.NavUtils;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.android.filmfun4me.BaseApplication;
import com.example.android.filmfun4me.R;
import com.example.android.filmfun4me.activity.activity.detail.presenter.DetailPresenter;
import com.example.android.filmfun4me.data.Episode;
import com.example.android.filmfun4me.data.Movie;
import com.example.android.filmfun4me.data.Review;
import com.example.android.filmfun4me.data.Season;
import com.example.android.filmfun4me.data.TvShow;
import com.example.android.filmfun4me.data.Video;
import com.example.android.filmfun4me.utils.BaseUtils;
import com.example.android.filmfun4me.utils.Constants;
import com.example.android.filmfun4me.utils.DateUtils;
import com.squareup.picasso.Picasso;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;

public class DetailMovieFragment extends Fragment implements DetailView {

    private static final String TAG = DetailMovieFragment.class.getSimpleName();

    @Inject
    DetailPresenter detailPresenter;

    // view binding
    @BindView(R.id.tv_detail_movie_title) TextView tvDetailMovieTitle;
    @BindView(R.id.tv_detail_release_date) TextView tvDetailReleaseDate;
    @BindView(R.id.tv_detail_overview) TextView tvDetailOverview;
    @BindView(R.id.tv_detail_rating) TextView tvDetailRating;
    @BindView(R.id.tv_detail_lang) TextView tvDetailLang;
    @BindView(R.id.tv_review_label) TextView tvReviewLabel;
    @BindView(R.id.tv_detail_genre) TextView tvGenre;

    @BindView(R.id.tv_drop_rev_button) TextView reviewButtonTextView;

    @BindView(R.id.image_view_poster) ImageView ivPoster;

    @BindView(R.id.recycler_movie_videos) RecyclerView recyclerViewVideos;
    @BindView(R.id.recycler_detail_reviews) RecyclerView recyclerViewReviews;

    private LinearLayoutManager videoListLayoutManager;
    private LinearLayoutManager reviewListLayoutManager;

    private ListVideosRecyclerAdapter listVideosRecyclerAdapter;

    // genre list
    ArrayList<String> listNames;

    private Callback callback;

    public DetailMovieFragment() {
        // Required empty public constructor
    }


    public static DetailMovieFragment newInstance(Movie movie, ArrayList<String> genreNamesList) {
        DetailMovieFragment fragment = new DetailMovieFragment();
        Bundle args = new Bundle();
        args.putParcelable(Constants.KEY_MOVIE, movie);
        args.putStringArrayList(Constants.KEY_GENRE_NAMES_LIST_MOVIE, genreNamesList);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        callback = (Callback) context;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
        ((BaseApplication) getActivity().getApplication()).createDetailComponent().inject(this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_movie_details, container, false);

        ButterKnife.bind(this, view);

        reviewListLayoutManager = new LinearLayoutManager(getActivity());
        videoListLayoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false);
        recyclerViewReviews.setLayoutManager(reviewListLayoutManager);
        recyclerViewVideos.setLayoutManager(videoListLayoutManager);

        reviewButtonTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (recyclerViewReviews.getVisibility() == View.VISIBLE) {
                    reviewButtonTextView.setText("+");
                    recyclerViewReviews.setVisibility(View.GONE);
                } else if (recyclerViewReviews.getVisibility() == View.GONE){
                    reviewButtonTextView.setText("-");
                    recyclerViewReviews.setVisibility(View.VISIBLE);
                }
            }
        });

        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        if (getArguments() != null && getArguments().containsKey(Constants.KEY_MOVIE)) {
            Movie movie = (Movie) getArguments().get(Constants.KEY_MOVIE);
            if (movie != null) {
                detailPresenter.setView(this);
                detailPresenter.showMovieDetails(movie);
            }
        }
    }

    @Override
    public void showMovieDetails(Movie movie) {

        String releaseDate = " " + DateUtils.formatDate(movie.getReleaseDate(), TAG);

        tvDetailMovieTitle.setText(movie.getTitle());
        tvDetailReleaseDate.setText(releaseDate);
        tvDetailOverview.setText(movie.getOverview());
        tvDetailRating.setText(String.valueOf(movie.getVoteAverage()) + "/10");
        tvDetailLang.setText(movie.getLanguage());

        Picasso.with(getActivity()).load(BaseUtils.getBackdropPath(movie.getBackdropPath())).into(ivPoster);

        listNames = getArguments().getStringArrayList(Constants.KEY_GENRE_NAMES_LIST_MOVIE);
        if (listNames != null){
            tvGenre.setText(getAppendedGenreNames(listNames));
        }

        ListReviewRecyclerAdapter customReviewAdapter = new ListReviewRecyclerAdapter(detailPresenter);
        // recycler view
        reviewListLayoutManager = new LinearLayoutManager(getActivity());
        recyclerViewReviews.setLayoutManager(reviewListLayoutManager);
        recyclerViewReviews.setAdapter(customReviewAdapter);

        listVideosRecyclerAdapter = new ListVideosRecyclerAdapter(detailPresenter);
        videoListLayoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false);
        recyclerViewVideos.setLayoutManager(videoListLayoutManager);
        recyclerViewVideos.setAdapter(listVideosRecyclerAdapter);

        DividerItemDecoration itemDecoration = new DividerItemDecoration(
                recyclerViewReviews.getContext(),
                reviewListLayoutManager.getOrientation()
        );

        itemDecoration.setDrawable(ContextCompat.getDrawable(getActivity(), R.drawable.divider_reviews_episodes));
        recyclerViewReviews.addItemDecoration(itemDecoration);

        detailPresenter.showMovieVideos(movie);
        detailPresenter.showMovieReviews(movie);
    }

    @Override
    public void showTvDetails(TvShow tvShow) {
        //  DO NOTHING
    }

    @Override
    public void showEpisodeList() {
        // DO NOTHING
    }

    @Override
    public void onTrailerClicked(String videoUrl) {
        callback.onTrailerClick(videoUrl);
    }

    @Override
    public void showSeasonList() {
        // DO NOTHING
    }

    @Override
    public void showVideos() {
        listVideosRecyclerAdapter.notifyDataSetChanged();
    }


    @Override
    public void showReviews() {
        tvReviewLabel.setVisibility(View.VISIBLE);
        reviewButtonTextView.setVisibility(View.VISIBLE);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        ((BaseApplication) getActivity().getApplication()).releaseDetailComponent();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        listNames.clear();
        getArguments().clear();
        detailPresenter.destroy();
    }

    private String getAppendedGenreNames (ArrayList<String> genreNamesList){
        String genreName = "";
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < genreNamesList.size(); i++) {
            if (i != genreNamesList.size() -1){
                genreName = builder.append(genreNamesList.get(i)).append(", ").toString();
            } else {
                genreName = builder.append(genreNamesList.get(i)).toString();
            }

        }
        return genreName;
    }
}
