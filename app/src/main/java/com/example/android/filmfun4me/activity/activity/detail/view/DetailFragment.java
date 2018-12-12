package com.example.android.filmfun4me.activity.activity.detail.view;


import android.content.Intent;
import android.graphics.Paint;
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

    @Inject
    DetailPresenter detailPresenter;

    private static final String TAG = DetailFragment.class.getSimpleName();

    // storage keys
    private static final String KEY_MOVIE = "movie";
    private static final String KEY_TV_SHOW = "tv_show";
    private static final String KEY_THEME_COLOR_MOVIE = "theme_color_movie";
    private static final String KEY_THEME_COLOR_TV = "theme_color_tv";
    private static final String KEY_GENRE_NAMES_LIST_MOVIE = "genreNames";
    private static final String KEY_GENRE_NAMES_LIST_TV_SHOW = "showGenreNames";

    // date formats
    private static final String INPUT_DATE_FORMAT = "yyyy-mm-dd";
    private static final String OUTPUT_DATE_FORMAT = "dd, MMM yyyy";

    // view binding
    @BindView(R.id.tv_detail_movie_title) private TextView tvDetailMovieTitle;
    @BindView(R.id.tv_detail_release_date) private TextView tvDetailReleaseDate;
    @BindView(R.id.tv_detail_overview) private TextView tvDetailOverview;
    @BindView(R.id.tv_detail_rating) private TextView tvDetailRating;
    @BindView(R.id.tv_detail_lang) private TextView tvDetailLang;
    @BindView(R.id.review_label) private TextView tvReviewLabel;
    @BindView(R.id.tv_detail_genre) private TextView tvGenre;
    @BindView(R.id.detail_language_label) private TextView tvLangLabel;
    @BindView(R.id.detail_overview_label) private TextView tvOverviewLabel;
    @BindView(R.id.detail_date_label) private TextView tvDateLabel;
    @BindView(R.id.tv_seasons_label) private TextView tvSeasonLabel;
    @BindView(R.id.drop_rev_button) private TextView reviewButtonTextView;

    @BindView(R.id.image_view_detail) private ImageView imageVideoView;
    @BindView(R.id.image_view_rating_star) private ImageView ratingStar;
    @BindView(R.id.image_view_play_icon) private ImageView ivPlayIcon;

    @BindView(R.id.recycler_detail_review_list) private RecyclerView recyclerView;

    @BindView(R.id.reviews_container) private LinearLayout reviewContainerLinearLayout;
    @BindView(R.id.linear_review_button_holder) private LinearLayout reviewButtonLinearLayout;
    @BindView(R.id.season_button_holder) private LinearLayout seasonButtonLinearLayout;

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

    int themeColor;

    public DetailFragment() {
        // Required empty public constructor
    }


    public static DetailFragment newInstance(Movie movie, ArrayList<String> genreNamesList, int themeColorMovie) {
        DetailFragment fragment = new DetailFragment();
        Bundle args = new Bundle();
        args.putParcelable(KEY_MOVIE, movie);
        args.putStringArrayList(KEY_GENRE_NAMES_LIST_MOVIE, genreNamesList);
        args.putInt(KEY_THEME_COLOR_MOVIE, themeColorMovie);
        fragment.setArguments(args);
        return fragment;
    }

    public static DetailFragment newInstanceTv(TvShow tvShow, ArrayList<String> genreNamesList, int themeColorTv) {
        DetailFragment fragment = new DetailFragment();
        Bundle args = new Bundle();
        args.putParcelable(KEY_TV_SHOW, tvShow);
        args.putStringArrayList(KEY_GENRE_NAMES_LIST_TV_SHOW, genreNamesList);
        args.putInt(KEY_THEME_COLOR_TV, themeColorTv);
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

        ButterKnife.bind(getActivity(), view);

        tvDetailMovieTitle = view.findViewById(R.id.tv_detail_movie_title);
        tvDetailReleaseDate = view.findViewById(R.id.tv_detail_release_date);
        tvDetailOverview = view.findViewById(R.id.tv_detail_overview);
        tvDetailRating = view.findViewById(R.id.tv_detail_rating);
        tvDetailLang = view.findViewById(R.id.tv_detail_lang);
        imageVideoView = view.findViewById(R.id.image_view_detail);
        recyclerView = view.findViewById(R.id.recycler_detail_review_list);
        reviewButtonTextView = view.findViewById(R.id.drop_rev_button);
        reviewContainerLinearLayout = view.findViewById(R.id.reviews_container);
        tvReviewLabel = view.findViewById(R.id.review_label);
        tvGenre = view.findViewById(R.id.tv_detail_genre);
        ratingStar = view.findViewById(R.id.image_view_rating_star);
        tvLangLabel = view.findViewById(R.id.detail_language_label);
        tvOverviewLabel = view.findViewById(R.id.detail_overview_label);
        tvDateLabel = view.findViewById(R.id.detail_date_label);
        tvSeasonLabel = view.findViewById(R.id.tv_seasons_label);
        ivPlayIcon = view.findViewById(R.id.image_view_play_icon);

        // REVIEW BUTTON HOLDER
        reviewButtonLinearLayout = view.findViewById(R.id.linear_review_button_holder);

        // RECYCLER VIEW
        layoutInflater = getActivity().getLayoutInflater();
        layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);

        // SEASON BUTTON HOLDER FOR SEASON BUTTONS
        seasonButtonLinearLayout = view.findViewById(R.id.season_button_holder);

        reviewButtonLinearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (reviewList.size() != 0) {
                    reviewContainerLinearLayout.setVisibility(View.VISIBLE);
                }
            }
        });

        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        if (getArguments() != null && getArguments().containsKey(KEY_MOVIE)) {
            Movie movie = (Movie) getArguments().get(KEY_MOVIE);
            if (movie != null) {
                this.movie = movie;
                detailPresenter.setView(this);
                detailPresenter.showDetails(movie);

            }
        } else if (getArguments() != null && getArguments().containsKey(KEY_TV_SHOW)) {
            TvShow tvShow = (TvShow) getArguments().get(KEY_TV_SHOW);
            if (tvShow != null) {
                this.tvShow = tvShow;
                detailPresenter.setView(this);
                detailPresenter.showSingleTvShowDetails(tvShow.getId());
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

        String genreName = "";

        StringBuilder builder = new StringBuilder();

        listNames = getArguments().getStringArrayList(KEY_GENRE_NAMES_LIST_MOVIE);
        for (int i = 0; i < listNames.size(); i++) {
            genreName = builder.append(listNames.get(i)).append(" , ").toString();
        }

        tvGenre.setText(genreName);

        themeColor = getArguments().getInt(KEY_THEME_COLOR_MOVIE);
        setColorOfText(themeColor);

        customReviewAdapter = new CustomReviewAdapter();
        recyclerView.setAdapter(customReviewAdapter);

        DividerItemDecoration itemDecoration = new DividerItemDecoration(
                recyclerView.getContext(),
                layoutManager.getOrientation()
        );

        itemDecoration.setDrawable(ContextCompat.getDrawable(getActivity(), R.drawable.divider_reviews_episodes));
        recyclerView.addItemDecoration(itemDecoration);

        seasonButtonLinearLayout.setVisibility(View.GONE);
        tvSeasonLabel.setVisibility(View.GONE);

        detailPresenter.setColorOfMovieRatingStar(ratingStar);
        detailPresenter.showVideos(movie);
        detailPresenter.showReviews(movie);
    }

    @Override
    public void showTvDetails(TvShow tvShow) {

        String releaseDate = " " + formatDate(tvShow.getReleaseDate());

        tvDetailMovieTitle.setText(tvShow.getTitle());
        tvDetailReleaseDate.setText(releaseDate);
        tvDetailOverview.setText(tvShow.getOverview());
        tvDetailRating.setText(String.valueOf(tvShow.getVoteAverage()));
        tvDetailLang.setText(tvShow.getLanguage());

        String genreName = "";
        StringBuilder builder = new StringBuilder();

        listNames = getArguments().getStringArrayList(KEY_GENRE_NAMES_LIST_TV_SHOW);
        for (int i = 0; i < listNames.size(); i++) {
            genreName = builder.append(listNames.get(i)).append(" , ").toString();
        }

        tvGenre.setText(genreName);

        themeColor = getArguments().getInt(KEY_THEME_COLOR_TV);
        setColorOfText(themeColor);

        tvReviewLabel.setVisibility(View.GONE);
        reviewButtonTextView.setVisibility(View.GONE);

        customEpisodeAdapter = new CustomEpisodeAdapter();
        recyclerView.setAdapter(customEpisodeAdapter);

        DividerItemDecoration itemDecoration = new DividerItemDecoration(
                recyclerView.getContext(),
                layoutManager.getOrientation()
        );

        itemDecoration.setDrawable(ContextCompat.getDrawable(getActivity(), R.drawable.divider_reviews_episodes));
        recyclerView.addItemDecoration(itemDecoration);

        detailPresenter.setColorOfTvRatingStar(ratingStar);
        detailPresenter.showTvVideos(tvShow);
        detailPresenter.showSeasonList(tvShow);
    }

    @Override
    public void showEpisodeList(List<Episode> episodeList) {
        this.episodeList.clear();
        this.episodeList.addAll(episodeList);
        this.customEpisodeAdapter.notifyDataSetChanged();

        if (episodeList.size() != 0) {
            reviewContainerLinearLayout.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onTrailerClicked(View v) {
        String videoUrl = (String) v.getTag();
        Intent playVideoIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(videoUrl));
        startActivity(playVideoIntent);
    }

    @Override
    public void setMovieRatingStar(ImageView imageView) {
        this.ratingStar = imageView;
    }

    @Override
    public void setTvRatingStar(ImageView imageView) {
        this.ratingStar = imageView;
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
            imageVideoView.setTag(Video.getUrl(video));
        } else {
            ivPlayIcon.setVisibility(View.GONE);
        }

        if (Video.getThumbnailUrl(video) != null) {
            Picasso.with(getActivity()).load(Video.getThumbnailUrl(video)).into(imageVideoView);
            ivPlayIcon.setVisibility(View.VISIBLE);
        } else {
            ivPlayIcon.setVisibility(View.GONE);
        }

        imageVideoView.setOnClickListener(new View.OnClickListener() {
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
            Log.d(TAG, "REVIEW LIST SIZE = " + reviewList.size());
            tvReviewLabel.setVisibility(View.INVISIBLE);
            reviewButtonTextView.setVisibility(View.INVISIBLE);
        }

    }

    // REVIEW LIST ADAPTER
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

            private TextView tvReviewAuthor;
            private TextView tvReviewContent;

            ViewHolder(View itemView) {
                super(itemView);

                this.tvReviewAuthor = itemView.findViewById(R.id.tv_review_author);
                this.tvReviewContent = itemView.findViewById(R.id.tv_review_content);
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

        public class ViewHolder extends RecyclerView.ViewHolder {

            private TextView tv__episode_title;
            private TextView tv_episode_overview;
            private ImageView iv_episode_poster;

            public ViewHolder(View itemView) {
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
        reviewList.clear();

        getArguments().clear();

        ((BaseApplication) getActivity().getApplication()).releaseDetailComponent();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        reviewList.clear();
        listNames.clear();

        getArguments().clear();
    }

    private void setColorOfText(int themeColor) {
        tvDetailMovieTitle.setTextColor(themeColor);
        tvDetailReleaseDate.setTextColor(themeColor);
        tvDetailOverview.setTextColor(themeColor);
        tvDetailRating.setTextColor(themeColor);
        tvDetailLang.setTextColor(themeColor);
        tvGenre.setTextColor(themeColor);
        tvDateLabel.setTextColor(themeColor);
        tvOverviewLabel.setTextColor(themeColor);
        tvLangLabel.setTextColor(themeColor);
        tvReviewLabel.setTextColor(getResources().getColor(R.color.colorSeasonsAndReviews));
        reviewButtonTextView.setTextColor(getResources().getColor(R.color.colorSeasonsAndReviews));

        // UNDERLINING TEXT VIEWS
        tvReviewLabel.setPaintFlags(tvReviewLabel.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
        reviewButtonTextView.setPaintFlags(reviewButtonTextView.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
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
}
