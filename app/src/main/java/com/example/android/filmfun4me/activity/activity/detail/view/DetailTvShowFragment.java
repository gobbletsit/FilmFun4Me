package com.example.android.filmfun4me.activity.activity.detail.view;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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

public class DetailTvShowFragment extends android.support.v4.app.Fragment implements DetailView {

    private static final String TAG = DetailTvShowFragment.class.getSimpleName();

    @Inject
    DetailPresenter detailPresenter;

    // view binding
    @BindView(R.id.tv_detail_tv_show_title) TextView tvDetailTvShowTitle;
    @BindView(R.id.tv_detail_tv_show_release_date) TextView tvDetailTvShowReleaseDate;
    @BindView(R.id.tv_detail_tv_show_overview) TextView tvDetailTvShowOverview;
    @BindView(R.id.tv_detail_tv_show_rating) TextView tvDetailTvShowRating;
    @BindView(R.id.tv_detail_tv_show_lang) TextView tvDetailTvShowLang;
    @BindView(R.id.tv_detail_tv_show_genre) TextView tvTvShowGenre;

    @BindView(R.id.image_view_poster_tv_show) ImageView ivTvShowPoster;

    @BindView(R.id.recycler_tv_show_videos) RecyclerView recyclerViewVideos;
    @BindView(R.id.recycler_episode_list) RecyclerView recyclerViewEpisodes;
    @BindView(R.id.recycler_season_list) RecyclerView recyclerViewSeasons;

    private ListEpisodeRecyclerAdapter customEpisodeAdapter;
    private ListVideosRecyclerAdapter listVideosRecyclerAdapter;
    private ListSeasonButtonRecyclerAdapter listSeasonButtonRecyclerAdapter;

    private Callback callback;

    public DetailTvShowFragment (){
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
        // NOTHING HERE
    }

    @Override
    public void showTvDetails(TvShow tvShow) {

        if (tvShow.getBackdropPath() != null){
            Picasso.with(getActivity()).load(BaseUtils.getBackdropPath(tvShow.getBackdropPath())).into(ivTvShowPoster);
        } else {
            Picasso.with(getActivity()).load(R.drawable.poster_not_available).into(ivTvShowPoster);
        }

        tvDetailTvShowTitle.setText(tvShow.getTitle());
        tvDetailTvShowReleaseDate.setText(DateUtils.formatDate(tvShow.getReleaseDate(), TAG));
        tvDetailTvShowOverview.setText(tvShow.getOverview());
        tvDetailTvShowRating.setText(String.valueOf(tvShow.getVoteAverage()) + "/10");
        tvDetailTvShowLang.setText(tvShow.getLanguage());

        if(getArguments() != null && getArguments().containsKey(Constants.KEY_SINGLE_TV_SHOW_GENRES)){
            tvTvShowGenre.setText(getArguments().getString(Constants.KEY_SINGLE_TV_SHOW_GENRES));
        }

        setAdapters(tvShow.getId());
    }

    @Override
    public void showVideos() {
        listVideosRecyclerAdapter.notifyDataSetChanged();
    }

    @Override
    public void showReviewLabel() {
        // nothing for now
    }

    @Override
    public void showReviews(ArrayList<ParentObject> parentObjects) {
        // DO NOTHING
    }

    @Override
    public void showSeasonList() {
        listSeasonButtonRecyclerAdapter.notifyDataSetChanged();
    }

    @Override
    public void showEpisodeList() {
        //customEpisodeAdapter.notifyDataSetChanged();
        recyclerViewEpisodes.setVisibility(View.VISIBLE);
    }

    @Override
    public void showEpisodes(ArrayList<ParentObject> parentObjects) {
        ListEpisodeExpandableAdapter listEpisodeExpandableAdapter = new ListEpisodeExpandableAdapter(getActivity(), parentObjects);
        listEpisodeExpandableAdapter.setCustomParentAnimationViewId(R.id.ib_expand_arrow);
        listEpisodeExpandableAdapter.setParentClickableViewAnimationDefaultDuration();
        listEpisodeExpandableAdapter.setParentAndIconExpandOnClick(true);
        recyclerViewEpisodes.setAdapter(listEpisodeExpandableAdapter);
    }

    private void setRecyclersLayouts(){

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

    private void setAdapters(String tvShowId){
        listVideosRecyclerAdapter = new ListVideosRecyclerAdapter(detailPresenter);
        recyclerViewVideos.setAdapter(listVideosRecyclerAdapter);

        listSeasonButtonRecyclerAdapter = new ListSeasonButtonRecyclerAdapter(detailPresenter, tvShowId);
        recyclerViewSeasons.setAdapter(listSeasonButtonRecyclerAdapter);

        //customEpisodeAdapter = new ListEpisodeRecyclerAdapter(detailPresenter);
        //recyclerViewEpisodes.setAdapter(customEpisodeAdapter);
    }

    @Override
    public void onTrailerClicked(String videoUrl) {
        callback.onTrailerClick(videoUrl);
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
