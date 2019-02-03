package com.example.android.filmfun4me.activity.activity.detail.view;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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

public class DetailTvShowFragment extends android.support.v4.app.Fragment implements DetailView {

    private static final String TAG = DetailTvShowFragment.class.getSimpleName();

    @Inject
    DetailPresenter detailPresenter;

    // view binding
    @BindView(R.id.tv_detail_tv_show_title)
    TextView tvDetailTvShowTitle;
    @BindView(R.id.tv_detail_tv_show_release_date)
    TextView tvDetailTvShowReleaseDate;
    @BindView(R.id.tv_detail_tv_show_overview)
    TextView tvDetailTvShowOverview;
    @BindView(R.id.tv_detail_tv_show_rating)
    TextView tvDetailTvShowRating;
    @BindView(R.id.tv_detail_tv_show_lang)
    TextView tvDetailTvShowLang;
    @BindView(R.id.tv_detail_tv_show_genre)
    TextView tvTvShowGenre;
    @BindView(R.id.tv_tv_details_not_available)
    TextView tvDetailsNotAvailable;

    @BindView(R.id.image_view_cover_tv_show)
    ImageView ivTvShowCover;

    @BindView(R.id.recycler_tv_show_videos)
    RecyclerView recyclerViewVideos;
    @BindView(R.id.recycler_episode_list)
    RecyclerView recyclerViewEpisodes;
    @BindView(R.id.recycler_season_list)
    RecyclerView recyclerViewSeasons;

    @BindView(R.id.progress_bar_tv_details)
    ProgressBar progressBar;
    @BindView(R.id.tv_details_container_lyt)
    ConstraintLayout detailsContainerLyt;

    private ListVideosRecyclerAdapter listVideosRecyclerAdapter;
    private ListSeasonButtonRecyclerAdapter listSeasonButtonRecyclerAdapter;

    private OnTrailerClickCallback onTrailerClickCallback;

    public DetailTvShowFragment() {
        // required empty public constructor
    }

    public static DetailTvShowFragment newInstance(TvShow tvShow, String singleTvShowGenres) {
        DetailTvShowFragment fragment = new DetailTvShowFragment();
        Bundle args = new Bundle();
        args.putParcelable(Constants.KEY_TV_SHOW, tvShow);
        args.putString(Constants.KEY_SINGLE_TV_SHOW_GENRES, singleTvShowGenres);
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
        View view = inflater.inflate(R.layout.fragment_tv_show_details, container, false);

        ButterKnife.bind(this, view);

        setRecyclersLayouts();

        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        if (getArguments() != null && getArguments().containsKey(Constants.KEY_TV_SHOW)) {
            TvShow tvShow = (TvShow) getArguments().get(Constants.KEY_TV_SHOW);
            if (tvShow != null) {
                detailPresenter.setDetailView(this);
                detailPresenter.showTvShowDetails(tvShow);
            }
        }

    }

    @Override
    public void showMovieDetails(Movie movie) {
        // DO NOTHING
    }

    @Override
    public void showTvDetails(TvShow tvShow) {

        if (tvShow.getBackdropPath() != null) {
            Picasso.with(getActivity()).load(BaseUtils.getBackdropPath(tvShow.getBackdropPath())).into(ivTvShowCover);
        } else {
            Picasso.with(getActivity()).load(R.drawable.poster_not_available).into(ivTvShowCover);
        }

        tvDetailTvShowTitle.setText(tvShow.getTitle());
        tvDetailTvShowReleaseDate.setText(DateUtils.formatDate(tvShow.getReleaseDate(), TAG));
        tvDetailTvShowOverview.setText(tvShow.getOverview());
        tvDetailTvShowRating.setText(String.valueOf(tvShow.getVoteAverage()) + "/10");
        tvDetailTvShowLang.setText(tvShow.getLanguage());

        if (getArguments() != null && getArguments().containsKey(Constants.KEY_SINGLE_TV_SHOW_GENRES)) {
            tvTvShowGenre.setText(getArguments().getString(Constants.KEY_SINGLE_TV_SHOW_GENRES));
        }

        setAdapters(tvShow.getId());
    }

    @Override
    public void showVideos() {
        if (listVideosRecyclerAdapter != null) {
            listVideosRecyclerAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public void showReviews() {
        // DO NOTHING
    }

    @Override
    public void showSeasonList() {
        if (listSeasonButtonRecyclerAdapter != null) {
            listSeasonButtonRecyclerAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public void showEpisodeList() {
        recyclerViewEpisodes.setVisibility(View.VISIBLE);
        recyclerViewEpisodes.setHasFixedSize(true);
        recyclerViewEpisodes.requestFocus();
    }

    @Override
    public void showEpisodes(ArrayList<ParentObject> parentObjects) {
        ListEpisodeExpandableAdapter listEpisodeExpandableAdapter = new ListEpisodeExpandableAdapter(getActivity(), parentObjects);
        listEpisodeExpandableAdapter.setCustomParentAnimationViewId(R.id.ib_expand_arrow);
        listEpisodeExpandableAdapter.setParentClickableViewAnimationDefaultDuration();
        listEpisodeExpandableAdapter.setParentAndIconExpandOnClick(true);
        recyclerViewEpisodes.setAdapter(listEpisodeExpandableAdapter);
    }

    private void setRecyclersLayouts() {

        LinearLayoutManager videoListLayoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false);
        recyclerViewVideos.setLayoutManager(videoListLayoutManager);

        LinearLayoutManager seasonListLayoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false);
        recyclerViewSeasons.setLayoutManager(seasonListLayoutManager);

        LinearLayoutManager episodeListLayoutManager = new LinearLayoutManager(getActivity());
        recyclerViewEpisodes.setLayoutManager(episodeListLayoutManager);
        DividerItemDecoration itemDecoration = new DividerItemDecoration(
                recyclerViewEpisodes.getContext(),
                episodeListLayoutManager.getOrientation()
        );
        itemDecoration.setDrawable(ContextCompat.getDrawable(getActivity(), R.drawable.divider_reviews_episodes));
        recyclerViewEpisodes.addItemDecoration(itemDecoration);
    }

    private void setAdapters(String tvShowId) {
        listVideosRecyclerAdapter = new ListVideosRecyclerAdapter(detailPresenter);
        recyclerViewVideos.setAdapter(listVideosRecyclerAdapter);

        listSeasonButtonRecyclerAdapter = new ListSeasonButtonRecyclerAdapter(detailPresenter, tvShowId);
        recyclerViewSeasons.setAdapter(listSeasonButtonRecyclerAdapter);
    }

    @Override
    public void onTrailerClicked(String videoUrl) {
        onTrailerClickCallback.onTrailerClick(videoUrl);
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
            Toast.makeText(getActivity(), error, Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        detailPresenter.destroy();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        ((BaseApplication) getActivity().getApplication()).releaseDetailComponent();
    }

    @Override
    public void onDetach() {
        super.onDetach();
        // to avoid leaking
        onTrailerClickCallback = null;
    }
}
