package com.example.android.filmfun4me.activity.activity.detail.view;

import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
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

public class DetailTvShowFragment extends android.support.v4.app.Fragment implements DetailView {

    private static final String TAG = DetailTvShowFragment.class.getSimpleName();

    @Inject
    DetailPresenter detailPresenter;

    // date formats
    private static final String INPUT_DATE_FORMAT = "yyyy-mm-dd";
    private static final String OUTPUT_DATE_FORMAT = "dd, MMM yyyy";

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

    @BindView(R.id.linear_season_button_container) LinearLayout seasonButtonLinearLayout;

    private LinearLayoutManager episodeListLayoutManager;
    private LinearLayoutManager videoListLayoutManager;

    private TvShow tvShow;

    private List<Season> seasonList = new ArrayList<>(20);

    // genre list
    ArrayList<String> listNames;

    private ListEpisodeRecyclerAdapter customEpisodeAdapter;
    private ListVideosRecyclerAdapter listVideosRecyclerAdapter;

    private Callback callback;

    public DetailTvShowFragment (){
        // required empty public constructor
    }

    public static DetailTvShowFragment newInstance(TvShow tvShow, ArrayList<String> genreNamesList) {
        DetailTvShowFragment fragment = new DetailTvShowFragment();
        Bundle args = new Bundle();
        args.putParcelable(Constants.KEY_TV_SHOW, tvShow);
        args.putStringArrayList(Constants.KEY_GENRE_NAMES_LIST_TV_SHOW, genreNamesList);
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

        episodeListLayoutManager = new LinearLayoutManager(getActivity());
        recyclerViewEpisodes.setLayoutManager(episodeListLayoutManager);

        videoListLayoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false);
        recyclerViewVideos.setLayoutManager(videoListLayoutManager);

        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        if (getArguments() != null && getArguments().containsKey(Constants.KEY_TV_SHOW)) {
            TvShow tvShow = (TvShow) getArguments().get(Constants.KEY_TV_SHOW);
            if (tvShow != null) {
                this.tvShow = tvShow;
                detailPresenter.setView(this);
                detailPresenter.showTvShowDetails(tvShow.getId());
            }
        }

    }

    @Override
    public void showMovieDetails(Movie movie) {
        // NOTHING HERE
    }

    @Override
    public void showTvDetails(TvShow tvShow) {
        String releaseDate = " " + DateUtils.formatDate(tvShow.getReleaseDate(),TAG);

        tvDetailTvShowTitle.setText(tvShow.getTitle());
        tvDetailTvShowReleaseDate.setText(releaseDate);
        tvDetailTvShowOverview.setText(tvShow.getOverview());
        tvDetailTvShowRating.setText(String.valueOf(tvShow.getVoteAverage()) + "/10");
        tvDetailTvShowLang.setText(tvShow.getLanguage());

        Picasso.with(getActivity()).load(BaseUtils.getBackdropPath(tvShow.getBackdropPath())).into(ivTvShowPoster);

        listNames = getArguments().getStringArrayList(Constants.KEY_GENRE_NAMES_LIST_TV_SHOW);
        if (listNames != null){
            tvTvShowGenre.setText(getAppendedGenreNames(listNames));
        }

        customEpisodeAdapter = new ListEpisodeRecyclerAdapter(detailPresenter);
        recyclerViewEpisodes.setAdapter(customEpisodeAdapter);

        listVideosRecyclerAdapter = new ListVideosRecyclerAdapter(detailPresenter);
        recyclerViewVideos.setAdapter(listVideosRecyclerAdapter);


        DividerItemDecoration itemDecoration = new DividerItemDecoration(
                recyclerViewEpisodes.getContext(),
                episodeListLayoutManager.getOrientation()
        );

        itemDecoration.setDrawable(ContextCompat.getDrawable(getActivity(), R.drawable.divider_reviews_episodes));
        recyclerViewEpisodes.addItemDecoration(itemDecoration);

        detailPresenter.showTvVideos(tvShow);
        detailPresenter.showSeasonList(tvShow);
    }

    @Override
    public void showVideos() {
        listVideosRecyclerAdapter.notifyDataSetChanged();
    }

    @Override
    public void showReviews() {
        // DO NOTHING
    }

    // NEEDS REFACTORING
    @Override
    public void showSeasonList(List<Season> seasonList) {
        this.seasonList.clear();
        this.seasonList.addAll(seasonList);

        detailPresenter.setSeasons(seasonList, seasonButtonLinearLayout, tvShow);
    }

    @Override
    public void showEpisodeList() {
        customEpisodeAdapter.notifyDataSetChanged();
        recyclerViewEpisodes.setVisibility(View.VISIBLE);
    }

    @Override
    public void onTrailerClicked(String videoUrl) {
        callback.onTrailerClick(videoUrl);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        listNames.clear();

        getArguments().clear();
        detailPresenter.destroy();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        ((BaseApplication) getActivity().getApplication()).releaseDetailComponent();
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
