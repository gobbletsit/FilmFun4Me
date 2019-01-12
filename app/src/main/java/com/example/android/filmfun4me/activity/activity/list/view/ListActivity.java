package com.example.android.filmfun4me.activity.activity.list.view;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.SearchView;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageButton;

import com.example.android.filmfun4me.BaseApplication;
import com.example.android.filmfun4me.R;
import com.example.android.filmfun4me.activity.activity.detail.view.DetailActivity;
import com.example.android.filmfun4me.activity.activity.detail.view.DetailTvShowFragment;
import com.example.android.filmfun4me.activity.activity.list.presenter.ListPresenter;
import com.example.android.filmfun4me.data.Movie;
import com.example.android.filmfun4me.data.TvShow;
import com.example.android.filmfun4me.utils.Constants;

import javax.inject.Inject;

import static com.example.android.filmfun4me.activity.activity.detail.view.DetailActivity.DETAIL_TV_SHOW_FRAG;

public class ListActivity extends AppCompatActivity implements ListFragment.Callback {

    private static final String LIST_FRAG = "list_frag";

    private FrameLayout searchFragmentLayout;
    private TabLayout tabLayout;

    private ImageButton ibMovies;
    private ImageButton ibTv;

    int selectedButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);

        searchFragmentLayout = findViewById(R.id.root_list_search_results_container);
        tabLayout = findViewById(R.id.tab_layout_list);
        ViewPager viewPager = findViewById(R.id.view_pager_list);
        ibMovies = findViewById(R.id.ib_movies);
        ibTv = findViewById(R.id.ib_tv);

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
                tabLayout.setupWithViewPager(viewPager);

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
                tabLayout.setupWithViewPager(viewPager);
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
        android.support.v4.app.FragmentManager fragmentManager = getSupportFragmentManager();
        android.support.v4.app.FragmentTransaction transaction = fragmentManager.beginTransaction();
        ListFragment searchFragment = ListFragment.newSearchInstance(selectedButton);
        transaction.replace(R.id.root_list_search_results_container, searchFragment);
        transaction.commit();
        tabLayout.setVisibility(View.GONE);
        searchFragmentLayout.setVisibility(View.VISIBLE);
    }

    @Override
    public void onSearchDialogClosed() {
        searchFragmentLayout.setVisibility(View.GONE);
        tabLayout.setVisibility(View.VISIBLE);
    }

    private String getStringTitle(int selectedButton){
        if (selectedButton == 0){
            return "Movies";
        } else {
            return "TV shows";
        }
    }
}
