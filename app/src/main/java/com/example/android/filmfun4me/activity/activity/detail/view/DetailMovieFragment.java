package com.example.android.filmfun4me.activity.activity.detail.view;


import android.app.ActionBar;
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

    // date formats
    private static final String INPUT_DATE_FORMAT = "yyyy-mm-dd";
    private static final String OUTPUT_DATE_FORMAT = "dd, MMM yyyy";

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

    @BindView(R.id.recycler_detail_reviews) RecyclerView recyclerViewReviews;

    private LinearLayoutManager reviewListLayoutManager;

    private List<Review> reviewList = new ArrayList<>(20);

    // genre list
    ArrayList<String> listNames;

    private ListReviewRecyclerAdapter customReviewAdapter;

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
        recyclerViewReviews.setLayoutManager(reviewListLayoutManager);

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
    public void showDetails(Movie movie) {

        String releaseDate = " " + formatDate(movie.getReleaseDate());

        tvDetailMovieTitle.setText(movie.getTitle());
        tvDetailReleaseDate.setText(releaseDate);
        tvDetailOverview.setText(movie.getOverview());
        tvDetailRating.setText(String.valueOf(movie.getVoteAverage()));
        tvDetailLang.setText(movie.getLanguage());

        Picasso.with(getActivity()).load(BaseUtils.getBackdropPath(movie.getBackdropPath())).into(ivPoster);

        listNames = getArguments().getStringArrayList(Constants.KEY_GENRE_NAMES_LIST_MOVIE);
        if (listNames != null){
            tvGenre.setText(getAppendedGenreNames(listNames));
        }

        customReviewAdapter = new ListReviewRecyclerAdapter(detailPresenter);
        // recycler view
        reviewListLayoutManager = new LinearLayoutManager(getActivity());
        recyclerViewReviews.setLayoutManager(reviewListLayoutManager);
        recyclerViewReviews.setAdapter(customReviewAdapter);

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
    public void showEpisodeList(List<Episode> episodeList) {
        // DO NOTHING
    }

    @Override
    public void onTrailerClicked(View v) {
        String videoUrl = (String) v.getTag();
        Intent playVideoIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(videoUrl));
        startActivity(playVideoIntent);
    }

    @Override
    public void showSeasonList(List<Season> seasonList) {
        // DO NOTHING
    }

    @Override
    public void showVideos(List<Video> videos) {

        /*Video video = videos.get(0);

        if (Video.getUrl(video) != null) {
            ivPoster.setTag(Video.getUrl(video));
        }

        if (Video.getThumbnailUrl(video) != null) {
            Picasso.with(getActivity()).load(Video.getThumbnailUrl(video)).into(ivPoster);
        }

        ivPoster.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                detailPresenter.whenTrailerClicked(v);
            }
        });*/
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
        reviewList.clear();
        listNames.clear();
        getArguments().clear();
        detailPresenter.destroy();
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
            if (i != genreNamesList.size() -1){
                genreName = builder.append(genreNamesList.get(i)).append(", ").toString();
            } else {
                genreName = builder.append(genreNamesList.get(i)).toString();
            }

        }
        return genreName;
    }
}
