package com.example.android.filmfun4me.activity.activity.detail.view;

import android.app.Fragment;
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
import com.example.android.filmfun4me.data.Review;
import com.example.android.filmfun4me.data.Season;
import com.example.android.filmfun4me.data.TvShow;
import com.example.android.filmfun4me.data.Video;
import com.example.android.filmfun4me.utils.Constants;
import com.squareup.picasso.Picasso;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;

public class DetailTvShowFragment extends Fragment implements DetailView {

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

    @BindView(R.id.image_view_poster) ImageView ivTvShowPoster;

    @BindView(R.id.recycler_episode_list) RecyclerView recyclerViewEpisodes;

    @BindView(R.id.linear_season_button_container) LinearLayout seasonButtonLinearLayout;

    private LinearLayoutManager episodeListLayoutManager;
    private LayoutInflater layoutInflater;

    private TvShow tvShow;

    private List<Season> seasonList = new ArrayList<>(20);
    private List<Episode> episodeList = new ArrayList<>(20);

    // genre list
    ArrayList<String> listNames;

    private ListEpisodeRecyclerAdapter customEpisodeAdapter;

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

        layoutInflater = getActivity().getLayoutInflater();
        episodeListLayoutManager = new LinearLayoutManager(getActivity());
        recyclerViewEpisodes.setLayoutManager(episodeListLayoutManager);

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
    public void showDetails(Movie movie) {
        // NOTHING HERE
    }

    @Override
    public void showTvDetails(TvShow tvShow) {
        String releaseDate = " " + formatDate(tvShow.getReleaseDate());

        tvDetailTvShowTitle.setText(tvShow.getTitle());
        tvDetailTvShowReleaseDate.setText(releaseDate);
        tvDetailTvShowOverview.setText(tvShow.getOverview());
        tvDetailTvShowRating.setText(String.valueOf(tvShow.getVoteAverage()));
        tvDetailTvShowLang.setText(tvShow.getLanguage());

        listNames = getArguments().getStringArrayList(Constants.KEY_GENRE_NAMES_LIST_TV_SHOW);
        if (listNames != null){
            tvTvShowGenre.setText(getAppendedGenreNames(listNames));
        }

        customEpisodeAdapter = new ListEpisodeRecyclerAdapter();
        recyclerViewEpisodes.setAdapter(customEpisodeAdapter);

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
    public void showVideos(List<Video> videos) {
        Video video = videos.get(0);

        if (Video.getUrl(video) != null) {
            ivTvShowPoster.setTag(Video.getUrl(video));
        }

        if (Video.getThumbnailUrl(video) != null) {
            Picasso.with(getActivity()).load(Video.getThumbnailUrl(video)).into(ivTvShowPoster);
        }

        ivTvShowPoster.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                detailPresenter.whenTrailerClicked(v);
            }
        });
    }

    @Override
    public void showReviews(List<Review> reviews) {
        // DO NOTHING
    }

    @Override
    public void showSeasonList(List<Season> seasonList) {
        this.seasonList.clear();
        this.seasonList.addAll(seasonList);

        detailPresenter.setSeasons(seasonList, seasonButtonLinearLayout, tvShow);
    }

    @Override
    public void showEpisodeList(List<Episode> episodeList) {
        this.episodeList.clear();
        this.episodeList.addAll(episodeList);
        this.customEpisodeAdapter.notifyDataSetChanged();

        if (episodeList.size() != 0) {
            recyclerViewEpisodes.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onTrailerClicked(View v) {
        String videoUrl = (String) v.getTag();
        Intent playVideoIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(videoUrl));
        startActivity(playVideoIntent);
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

    private String formatDate(String date) {

        SimpleDateFormat formatInput = new SimpleDateFormat(INPUT_DATE_FORMAT, java.util.Locale.getDefault());
        SimpleDateFormat formatOutput = new SimpleDateFormat(OUTPUT_DATE_FORMAT, java.util.Locale.getDefault());

        String formattedDate = "";
        try {
            Date newDate = formatInput.parse(date);
            formattedDate = formatOutput.format(newDate);
        } catch (ParseException e) {
            Log.e(TAG, e.toString());
        }
        return formattedDate;

    }

    private String getAppendedGenreNames (ArrayList<String> genreNamesList){
        String genreName = "";
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < genreNamesList.size(); i++) {
            genreName = builder.append(genreNamesList.get(i)).append(" , ").toString();
        }
        return genreName;
    }
}
