package com.example.android.filmfun4me.activity.activity.detail.view;


import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
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
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

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

public class DetailMovieFragment extends Fragment implements DetailView, View.OnClickListener {

    private static final String TAG = DetailMovieFragment.class.getSimpleName();

    @Inject
    DetailPresenter detailPresenter;

    @BindView(R.id.tv_detail_movie_title)
    TextView tvDetailMovieTitle;
    @BindView(R.id.tv_detail_release_date)
    TextView tvDetailReleaseDate;
    @BindView(R.id.tv_detail_overview)
    TextView tvDetailOverview;
    @BindView(R.id.tv_detail_rating)
    TextView tvDetailRating;
    @BindView(R.id.tv_detail_lang)
    TextView tvDetailLang;
    @BindView(R.id.tv_review_label)
    TextView tvReviewLabel;
    @BindView(R.id.tv_detail_genre)
    TextView tvGenre;
    @BindView(R.id.tv_movie_details_not_available)
    TextView tvDetailsNotAvailable;

    @BindView(R.id.button_drop_rev)
    Button btn_drop_review;

    @BindView(R.id.image_view_poster)
    ImageView ivPoster;

    @BindView(R.id.recycler_movie_videos)
    RecyclerView recyclerViewVideos;
    @BindView(R.id.recycler_detail_reviews)
    RecyclerView recyclerViewReviews;

    @BindView(R.id.progress_bar_movie_details)
    ProgressBar progressBar;
    @BindView(R.id.movie_details_container_lyt)
    ConstraintLayout detailsContainerLyt;

    private ListVideosRecyclerAdapter listVideosRecyclerAdapter;
    private ListReviewRecyclerAdapter listReviewsRecyclerAdapter;

    private OnTrailerClickCallback onTrailerClickCallback;

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
        onTrailerClickCallback = (OnTrailerClickCallback) context;
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

        // handle review adapter visibility
        btn_drop_review.setOnClickListener(this);
        tvReviewLabel.setOnClickListener(this);

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

        if (movie.getBackdropPath() != null) {
            Picasso.with(getActivity()).load(BaseUtils.getBackdropPath(movie.getBackdropPath())).into(ivPoster);
        } else {
            Picasso.with(getActivity()).load(R.drawable.poster_not_available).into(ivPoster);
        }

        tvDetailMovieTitle.setText(movie.getTitle());
        tvDetailReleaseDate.setText(DateUtils.formatDate(movie.getReleaseDate(), TAG));
        tvDetailOverview.setText(movie.getOverview());
        tvDetailRating.setText(String.valueOf(movie.getVoteAverage()) + "/10");
        tvDetailLang.setText(movie.getLanguage());

        if (getArguments() != null && getArguments().containsKey(Constants.KEY_SINGLE_MOVIE_GENRES)) {
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
        // DO NOTHING
    }

    @Override
    public void onTrailerClicked(String videoUrl) {
        onTrailerClickCallback.onTrailerClick(videoUrl);
    }

    @Override
    public void showSeasonList() {
        // DO NOTHING
    }

    @Override
    public void showVideos() {
        if (listVideosRecyclerAdapter != null) {
            listVideosRecyclerAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public void showReviews() {
        if (listVideosRecyclerAdapter != null) {
            listReviewsRecyclerAdapter.notifyDataSetChanged();
        }
        tvReviewLabel.setVisibility(View.VISIBLE);
        btn_drop_review.setVisibility(View.VISIBLE);
    }

    private void setRecyclersLayouts() {
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

    private void setAdapters() {
        listReviewsRecyclerAdapter = new ListReviewRecyclerAdapter(detailPresenter);
        recyclerViewReviews.setAdapter(listReviewsRecyclerAdapter);

        listVideosRecyclerAdapter = new ListVideosRecyclerAdapter(detailPresenter);
        recyclerViewVideos.setAdapter(listVideosRecyclerAdapter);
    }

    @Override
    public void showLoading() {
        detailsContainerLyt.setVisibility(View.GONE);
        progressBar.setVisibility(View.VISIBLE);
    }

    @Override
    public void onLoadingFinished() {
        progressBar.setVisibility(View.GONE);
        detailsContainerLyt.setVisibility(View.VISIBLE);
    }

    @Override
    public void loadingErrorMessage(String error) {
        progressBar.setVisibility(View.GONE);
        detailsContainerLyt.setVisibility(View.GONE);
        tvDetailsNotAvailable.setVisibility(View.VISIBLE);
        if (error.contains("only-if-cached")) {
            tvDetailsNotAvailable.setText(getResources().getString(R.string.details_check_internet));
        } else {
            Toast.makeText(getActivity(), error, Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onClick(View v) {
        if (recyclerViewReviews.getVisibility() == View.VISIBLE) {
            btn_drop_review.setText("+");
            recyclerViewReviews.setVisibility(View.GONE);
        } else if (recyclerViewReviews.getVisibility() == View.GONE) {
            btn_drop_review.setText("-");
            recyclerViewReviews.setVisibility(View.VISIBLE);
            recyclerViewReviews.requestFocus();
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        getArguments().clear();
        detailPresenter.destroy();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        ((BaseApplication) getActivity().getApplication()).releaseDetailComponent();
    }
}
