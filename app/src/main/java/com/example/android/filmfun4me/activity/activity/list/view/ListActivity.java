package com.example.android.filmfun4me.activity.activity.list.view;

import android.content.Intent;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageButton;

import com.example.android.filmfun4me.R;
import com.example.android.filmfun4me.activity.activity.detail.view.DetailActivity;
import com.example.android.filmfun4me.data.Movie;
import com.example.android.filmfun4me.data.TvShow;
import com.example.android.filmfun4me.utils.Constants;

public class ListActivity extends AppCompatActivity implements ListFragment.Callback {

    public static final String SEARCH_VISIBLE = "search_visible";

    private FrameLayout searchFragmentLayout;
    private TabLayout tabLayout;
    private ConstraintLayout footer;

    private ImageButton ibMovies;
    private ImageButton ibTv;

    private int selectedButton;
    private boolean isSearchVisible;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);

        searchFragmentLayout = findViewById(R.id.root_list_search_results_container);
        tabLayout = findViewById(R.id.tab_layout_list);
        ViewPager viewPager = findViewById(R.id.view_pager_list);
        footer = findViewById(R.id.list_activity_footer);
        ibMovies = findViewById(R.id.ib_movies);
        ibTv = findViewById(R.id.ib_tv);

        if (savedInstanceState != null){
            selectedButton = savedInstanceState.getInt(Constants.SELECTED_BUTTON);
            isSearchVisible = savedInstanceState.getBoolean(SEARCH_VISIBLE);
            if (isSearchVisible){
                switchToSearchFragment();
            }
        }

        setTitle(getStringTitle(selectedButton));
        ListFragmentPagerAdapter listFragmentPagerAdapter = new ListFragmentPagerAdapter(ListActivity.this, getSupportFragmentManager(), selectedButton);
        viewPager.setAdapter(listFragmentPagerAdapter);
        tabLayout.setupWithViewPager(viewPager);

        ibMovies.setSelected(true);
        ibMovies.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectedButton = Constants.BUTTON_MOVIES;
                ibMovies.setSelected(true);
                ibTv.setSelected(false);
                setTitle(getStringTitle(selectedButton));
                ListFragmentPagerAdapter listMovieFragmentPagerAdapter = new ListFragmentPagerAdapter(ListActivity.this, getSupportFragmentManager(), selectedButton);
                viewPager.setAdapter(listMovieFragmentPagerAdapter);
                tabLayout.setupWithViewPager(viewPager, true);

            }
        });

        ibTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectedButton = Constants.BUTTON_TV_SHOWS;
                ibTv.setSelected(true);
                ibMovies.setSelected(false);
                setTitle(getStringTitle(selectedButton));
                ListFragmentPagerAdapter listTvFragmentPagerAdapter = new ListFragmentPagerAdapter(ListActivity.this, getSupportFragmentManager(), selectedButton);
                viewPager.setAdapter(listTvFragmentPagerAdapter);
                tabLayout.setupWithViewPager(viewPager, true);
            }
        });
    }

    @Override
    public void onMovieClicked(Movie movie, String singleMovieGenres, int selectedButton) {
        Intent detailIntent = new Intent(this, DetailActivity.class);
        Bundle extras = new Bundle();
        extras.putParcelable(Constants.KEY_MOVIE, movie);
        extras.putString(Constants.KEY_SINGLE_MOVIE_GENRES, singleMovieGenres);
        extras.putInt(Constants.SELECTED_BUTTON, selectedButton);
        detailIntent.putExtras(extras);
        startActivity(detailIntent);
    }

    @Override
    public void onTvShowClicked(TvShow tvShow, String singleTvShowGenres, int selectedButton) {
        Intent detailIntent = new Intent(this, DetailActivity.class);
        Bundle extras = new Bundle();
        extras.putParcelable(Constants.KEY_TV_SHOW, tvShow);
        extras.putString(Constants.KEY_SINGLE_TV_SHOW_GENRES, singleTvShowGenres);
        extras.putInt(Constants.SELECTED_BUTTON, selectedButton);
        detailIntent.putExtras(extras);
        startActivity(detailIntent);
    }

    @Override
    public void onSearchItemClick() {
        switchToSearchFragment();
    }

    @Override
    public void onSearchDialogClosed() {
        searchFragmentLayout.setVisibility(View.GONE);
        tabLayout.setVisibility(View.VISIBLE);
        footer.setVisibility(View.VISIBLE);
    }

    private String getStringTitle(int selectedButton){
        if (selectedButton == 0){
            return "Movies";
        } else {
            return "TV shows";
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        if (searchFragmentLayout.getVisibility() == View.VISIBLE){
            isSearchVisible = true;
        }
        outState.putInt(Constants.SELECTED_BUTTON, selectedButton);
        outState.putBoolean(SEARCH_VISIBLE, isSearchVisible);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    private void switchToSearchFragment(){
        android.support.v4.app.FragmentManager fragmentManager = getSupportFragmentManager();
        android.support.v4.app.FragmentTransaction transaction = fragmentManager.beginTransaction();
        ListFragment searchFragment = ListFragment.newSearchInstance(selectedButton);
        transaction.replace(R.id.root_list_search_results_container, searchFragment);
        transaction.commit();
        tabLayout.setVisibility(View.GONE);
        searchFragmentLayout.setVisibility(View.VISIBLE);
        footer.setVisibility(View.GONE);
    }
}
