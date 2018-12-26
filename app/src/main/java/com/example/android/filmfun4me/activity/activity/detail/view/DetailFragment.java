package com.example.android.filmfun4me.activity.activity.detail.view;


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

public class DetailFragment extends Fragment implements DetailView {

    private static final String TAG = DetailFragment.class.getSimpleName();

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
    @BindView(R.id.tv_detail_lang_label) TextView tvLangLabel;
    @BindView(R.id.tv_detail_overview_label) TextView tvOverviewLabel;
    @BindView(R.id.tv_detail_date_label) TextView tvDateLabel;
    @BindView(R.id.tv_seasons_label) TextView tvSeasonLabel;
    @BindView(R.id.tv_drop_rev_button) TextView reviewButtonTextView;

    @BindView(R.id.image_view_poster) ImageView ivPoster;
    @BindView(R.id.image_view_rating_star) ImageView ivRatingStar;
    @BindView(R.id.image_view_play_icon) ImageView ivPlayIcon;

    @BindView(R.id.recycler_detail_reviews) RecyclerView recyclerViewReviews;

    @BindView(R.id.linear_reviews_container) LinearLayout reviewsContainerLinearLayout;
    @BindView(R.id.linear_review_button_container) LinearLayout reviewButtonLinearLayout;
    @BindView(R.id.linear_season_button_container) LinearLayout seasonButtonLinearLayout;

    private LinearLayoutManager layoutManager;
    private LayoutInflater layoutInflater;

    private Movie movie;
    private TvShow tvShow;

    private List<Review> reviewList = new ArrayList<>(20);
    private List<Season> seasonList = new ArrayList<>(20);
    private List<Episode> episodeList = new ArrayList<>(20);

    // genre list
    ArrayList<String> listNames;

    private CustomReviewAdapter customReviewAdapter;
    private CustomEpisodeAdapter customEpisodeAdapter;


    public DetailFragment() {
        // Required empty public constructor
    }


    public static DetailFragment newInstance(Movie movie, ArrayList<String> genreNamesList) {
        DetailFragment fragment = new DetailFragment();
        Bundle args = new Bundle();
        args.putParcelable(Constants.KEY_MOVIE, movie);
        args.putStringArrayList(Constants.KEY_GENRE_NAMES_LIST_MOVIE, genreNamesList);
        fragment.setArguments(args);
        return fragment;
    }

    public static DetailFragment newInstanceTv(TvShow tvShow, ArrayList<String> genreNamesList) {
        DetailFragment fragment = new DetailFragment();
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
        View view = inflater.inflate(R.layout.fragment_detail, container, false);

        ButterKnife.bind(this, view);

        layoutInflater = getActivity().getLayoutInflater();
        layoutManager = new LinearLayoutManager(getActivity());
        recyclerViewReviews.setLayoutManager(layoutManager);

        reviewButtonLinearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (reviewList.size() != 0) {
                    reviewsContainerLinearLayout.setVisibility(View.VISIBLE);
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
                this.movie = movie;
                detailPresenter.setView(this);
                detailPresenter.showMovieDetails(movie);

            }
        } else if (getArguments() != null && getArguments().containsKey(Constants.KEY_TV_SHOW)) {
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

        String releaseDate = " " + formatDate(movie.getReleaseDate());

        tvDetailMovieTitle.setText(movie.getTitle());
        tvDetailReleaseDate.setText(releaseDate);
        tvDetailOverview.setText(movie.getOverview());
        tvDetailRating.setText(String.valueOf(movie.getVoteAverage()));
        tvDetailLang.setText(movie.getLanguage());

        listNames = getArguments().getStringArrayList(Constants.KEY_GENRE_NAMES_LIST_MOVIE);
        if (listNames != null){
            tvGenre.setText(getAppendedGenreNames(listNames));
        }

        customReviewAdapter = new CustomReviewAdapter();
        // recycler view
        layoutInflater = getActivity().getLayoutInflater();
        layoutManager = new LinearLayoutManager(getActivity());
        recyclerViewReviews.setLayoutManager(layoutManager);
        recyclerViewReviews.setAdapter(customReviewAdapter);

        DividerItemDecoration itemDecoration = new DividerItemDecoration(
                recyclerViewReviews.getContext(),
                layoutManager.getOrientation()
        );

        itemDecoration.setDrawable(ContextCompat.getDrawable(getActivity(), R.drawable.divider_reviews_episodes));
        recyclerViewReviews.addItemDecoration(itemDecoration);

        seasonButtonLinearLayout.setVisibility(View.GONE);
        tvSeasonLabel.setVisibility(View.GONE);

        //detailPresenter.setColorOfMovieRatingStar(ivRatingStar);
        detailPresenter.showMovieVideos(movie);
        detailPresenter.showMovieReviews(movie);
    }

    @Override
    public void showTvDetails(TvShow tvShow) {

        String releaseDate = " " + formatDate(tvShow.getReleaseDate());

        tvDetailMovieTitle.setText(tvShow.getTitle());
        tvDetailReleaseDate.setText(releaseDate);
        tvDetailOverview.setText(tvShow.getOverview());
        tvDetailRating.setText(String.valueOf(tvShow.getVoteAverage()));
        tvDetailLang.setText(tvShow.getLanguage());

        listNames = getArguments().getStringArrayList(Constants.KEY_GENRE_NAMES_LIST_TV_SHOW);
        if (listNames != null){
            tvGenre.setText(getAppendedGenreNames(listNames));
        }

        tvReviewLabel.setVisibility(View.GONE);
        reviewButtonTextView.setVisibility(View.GONE);

        customEpisodeAdapter = new CustomEpisodeAdapter();
        recyclerViewReviews.setAdapter(customEpisodeAdapter);

        DividerItemDecoration itemDecoration = new DividerItemDecoration(
                recyclerViewReviews.getContext(),
                layoutManager.getOrientation()
        );

        itemDecoration.setDrawable(ContextCompat.getDrawable(getActivity(), R.drawable.divider_reviews_episodes));
        recyclerViewReviews.addItemDecoration(itemDecoration);

        detailPresenter.showTvVideos(tvShow);
        detailPresenter.showSeasonList(tvShow);
    }

    @Override
    public void showEpisodeList(List<Episode> episodeList) {
        this.episodeList.clear();
        this.episodeList.addAll(episodeList);
        this.customEpisodeAdapter.notifyDataSetChanged();

        if (episodeList.size() != 0) {
            reviewsContainerLinearLayout.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onTrailerClicked(View v) {
        String videoUrl = (String) v.getTag();
        Intent playVideoIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(videoUrl));
        startActivity(playVideoIntent);
    }

    @Override
    public void showSeasonList(List<Season> seasonList) {
        this.seasonList.clear();
        this.seasonList.addAll(seasonList);

        detailPresenter.setSeasons(seasonList, seasonButtonLinearLayout, tvShow);
    }

    @Override
    public void showVideos(List<Video> videos) {

        Video video = videos.get(0);

        if (Video.getUrl(video) != null) {
            ivPoster.setTag(Video.getUrl(video));
        } else {
            ivPlayIcon.setVisibility(View.GONE);
        }

        if (Video.getThumbnailUrl(video) != null) {
            Picasso.with(getActivity()).load(Video.getThumbnailUrl(video)).into(ivPoster);
            ivPlayIcon.setVisibility(View.VISIBLE);
        } else {
            ivPlayIcon.setVisibility(View.GONE);
        }

        ivPoster.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                detailPresenter.whenTrailerClicked(v);
            }
        });
    }


    @Override
    public void showReviews(List<Review> reviews) {
        this.reviewList.clear();
        this.reviewList.addAll(reviews);
        this.customReviewAdapter.notifyDataSetChanged();

        if (reviewList.size() == 0) {
            tvReviewLabel.setVisibility(View.INVISIBLE);
            reviewButtonTextView.setVisibility(View.INVISIBLE);
        }

    }

    public class CustomReviewAdapter extends RecyclerView.Adapter<CustomReviewAdapter.ViewHolder> {


        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = layoutInflater.inflate(R.layout.review_list_item, parent, false);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(ViewHolder holder, int position) {
            Review review = reviewList.get(position);

            holder.tvReviewAuthor.setText(review.getReviewAuthor());
            holder.tvReviewAuthor.setTextColor(getResources().getColor(R.color.colorSeasonsAndReviews));
            holder.tvReviewContent.setText(review.getReviewContent());
            holder.tvReviewContent.setTextColor(getResources().getColor(R.color.colorSeasonsAndReviews));

        }

        @Override
        public int getItemCount() {
            return reviewList.size();
        }

        class ViewHolder extends RecyclerView.ViewHolder {

            @BindView(R.id.tv_review_author) TextView tvReviewAuthor;
            @BindView(R.id.tv_review_content) TextView tvReviewContent;

            ViewHolder(View itemView) {
                super(itemView);
                ButterKnife.bind(this, itemView);
            }
        }
    }

    // EPISODE LIST ADAPTER
    public class CustomEpisodeAdapter extends RecyclerView.Adapter<CustomEpisodeAdapter.ViewHolder> {
        @Override
        public CustomEpisodeAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = layoutInflater.inflate(R.layout.episode_list_item, parent, false);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(CustomEpisodeAdapter.ViewHolder holder, int position) {
            Episode episode = episodeList.get(position);

            holder.tv__episode_title.setText(episode.getName());
            holder.tv_episode_overview.setText(episode.getOverview());

            String posterPath = episode.getPosterPath();

            if (posterPath != null) {
                Picasso.with(getActivity()).load(BaseUtils.getPosterPath(episode.getPosterPath())).into(holder.iv_episode_poster);
            } else {
                Picasso.with(getActivity()).load(R.drawable.poster_not_available).into(holder.iv_episode_poster);
            }
        }

        @Override
        public int getItemCount() {
            return episodeList.size();
        }

        class ViewHolder extends RecyclerView.ViewHolder {

            private TextView tv__episode_title;
            private TextView tv_episode_overview;
            private ImageView iv_episode_poster;

            ViewHolder(View itemView) {
                super(itemView);
                this.tv__episode_title = itemView.findViewById(R.id.tv_episode_title);
                this.tv_episode_overview = itemView.findViewById(R.id.tv_episode_overview);
                this.iv_episode_poster = itemView.findViewById(R.id.imv_episode_poster);
            }
        }
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

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                NavUtils.navigateUpFromSameTask(getActivity());
                return true;
        }
        return super.onOptionsItemSelected(item);
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
