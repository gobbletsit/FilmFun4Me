package com.example.android.filmfun4me.activity.activity.detail.view;


import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bignerdranch.expandablerecyclerview.Model.ParentObject;
import com.example.android.filmfun4me.BaseApplication;
import com.example.android.filmfun4me.R;
import com.example.android.filmfun4me.activity.activity.detail.presenter.DetailPresenter;
import com.example.android.filmfun4me.data.Movie;
import com.example.android.filmfun4me.data.TvShow;
import com.example.android.filmfun4me.utils.BaseUtils;
import com.example.android.filmfun4me.utils.Constants;
import com.example.android.filmfun4me.utils.DateUtils;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

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

    @BindView(R.id.button_drop_rev) Button btn_drop_review;

    @BindView(R.id.image_view_poster) ImageView ivPoster;

    @BindView(R.id.recycler_movie_videos) RecyclerView recyclerViewVideos;
    @BindView(R.id.recycler_detail_reviews) RecyclerView recyclerViewReviews;

    private ListVideosRecyclerAdapter listVideosRecyclerAdapter;
    private ListReviewRecyclerAdapter listReviewsRecyclerAdapter;

    private Callback callback;

    public DetailMovieFragment() {
        // Required empty public constructor
    }


    public static DetailMovieFragment newInstance(Movie movie, String singleMovieGenres) {
        DetailMovieFragment fragment = new DetailMovieFragment();
        Bundle args = new Bundle();
        args.putParcelable(Constants.KEY_MOVIE, movie);
        args.putString(Constants.KEY_SINGLE_MOVIE_GENRES, singleMovieGenres);
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

        setRecyclersLayouts();
        setAdapters();

        btn_drop_review.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (recyclerViewReviews.getVisibility() == View.VISIBLE) {
                    btn_drop_review.setText("+");
                    recyclerViewReviews.setVisibility(View.GONE);
                } else if (recyclerViewReviews.getVisibility() == View.GONE){
                    btn_drop_review.setText("-");
                    detailPresenter.generateReviews();
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
                detailPresenter.setDetailView(this);
                detailPresenter.showMovieDetails(movie);
            }
        }
    }

    @Override
    public void showMovieDetails(Movie movie) {

        if (movie.getBackdropPath() != null){
            Picasso.with(getActivity()).load(BaseUtils.getBackdropPath(movie.getBackdropPath())).into(ivPoster);
        } else {
            Picasso.with(getActivity()).load(R.drawable.poster_not_available).into(ivPoster);
        }

        tvDetailMovieTitle.setText(movie.getTitle());
        tvDetailReleaseDate.setText(DateUtils.formatDate(movie.getReleaseDate(), TAG));
        tvDetailOverview.setText(movie.getOverview());
        tvDetailRating.setText(String.valueOf(movie.getVoteAverage()) + "/10");
        tvDetailLang.setText(movie.getLanguage());

        if (getArguments() != null && getArguments().containsKey(Constants.KEY_SINGLE_MOVIE_GENRES)){
            tvGenre.setText(getArguments().getString(Constants.KEY_SINGLE_MOVIE_GENRES));
        }
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
    public void showEpisodes(ArrayList<ParentObject> parentObjects) {
        // nothing here
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
    public void showReviewLabel() {
        tvReviewLabel.setVisibility(View.VISIBLE);
        btn_drop_review.setVisibility(View.VISIBLE);
    }


    @Override
    public void showReviews(ArrayList<ParentObject> parentObjects) {
        ListReviewExpandableAdapter mCrimeExpandableAdapter = new ListReviewExpandableAdapter(getActivity(), parentObjects);
        mCrimeExpandableAdapter.setCustomParentAnimationViewId(R.id.parent_review_item_expand_arrow);
        mCrimeExpandableAdapter.setParentClickableViewAnimationDefaultDuration();
        mCrimeExpandableAdapter.setParentAndIconExpandOnClick(true);
        recyclerViewReviews.setAdapter(mCrimeExpandableAdapter);
        //listReviewsRecyclerAdapter.notifyDataSetChanged();
        //tvReviewLabel.setVisibility(View.VISIBLE);
        //btn_drop_review.setVisibility(View.VISIBLE);
    }

    private void setRecyclersLayouts(){
        LinearLayoutManager videoListLayoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false);
        recyclerViewVideos.setLayoutManager(videoListLayoutManager);

        LinearLayoutManager reviewListLayoutManager = new LinearLayoutManager(getActivity());
        recyclerViewReviews.setLayoutManager(reviewListLayoutManager);
        DividerItemDecoration itemDecoration = new DividerItemDecoration(
                recyclerViewReviews.getContext(),
                reviewListLayoutManager.getOrientation()
        );

        itemDecoration.setDrawable(ContextCompat.getDrawable(getActivity(), R.drawable.divider_reviews_episodes));
        recyclerViewReviews.addItemDecoration(itemDecoration);
    }

    private void setAdapters(){
        //listReviewsRecyclerAdapter = new ListReviewRecyclerAdapter(detailPresenter);
        //recyclerViewReviews.setAdapter(listReviewsRecyclerAdapter);

        listVideosRecyclerAdapter = new ListVideosRecyclerAdapter(detailPresenter);
        recyclerViewVideos.setAdapter(listVideosRecyclerAdapter);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        ((BaseApplication) getActivity().getApplication()).releaseDetailComponent();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        getArguments().clear();
        detailPresenter.destroy();
    }
}
