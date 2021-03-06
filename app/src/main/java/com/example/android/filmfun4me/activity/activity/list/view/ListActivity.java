package com.example.android.filmfun4me.activity.activity.list.view;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.support.constraint.ConstraintLayout;
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
import android.widget.TextView;

import com.example.android.filmfun4me.BaseApplication;
import com.example.android.filmfun4me.NetworkLostReceiver;
import com.example.android.filmfun4me.R;
import com.example.android.filmfun4me.activity.activity.detail.view.DetailActivity;
import com.example.android.filmfun4me.data.Movie;
import com.example.android.filmfun4me.data.TvShow;
import com.example.android.filmfun4me.utils.Constants;
import com.example.android.filmfun4me.NetworkRegainedReceiver;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ListActivity extends AppCompatActivity implements ListFragment.Callback {

    public static boolean isListActive;

    private static final String SEARCH_VISIBLE = "search_visible";
    private static final String SEARCH_FRAG = "SEARCH_FRAG";
    private static final String QUERY = "query";

    @BindView(R.id.root_list_search_results_container)
    FrameLayout searchFragmentLayout;
    @BindView(R.id.tab_layout_list)
    TabLayout tabLayout;
    @BindView(R.id.list_activity_footer)
    ConstraintLayout footerLayout;

    @BindView(R.id.ib_movies)
    ImageButton ibMovies;
    @BindView(R.id.ib_tv)
    ImageButton ibTv;

    @BindView(R.id.footer_movie_label)
    TextView footerMoviesLabel;
    @BindView(R.id.footer_tv_label)
    TextView footerTvLabel;

    @BindView(R.id.view_pager_list)
    ViewPager viewPager;

    private ListFragmentPagerAdapter listFragmentPagerAdapter;

    private int selectedButton;

    private boolean isSearchVisible;
    private String query;

    private NetworkRegainedReceiver networkRegainedReceiver;
    private NetworkLostReceiver networkLostReceiver;
    private boolean isRegainedReceiverRegistered;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);

        ButterKnife.bind(this);

        if (savedInstanceState != null) {
            selectedButton = savedInstanceState.getInt(Constants.SELECTED_BUTTON);
            if (savedInstanceState.containsKey(SEARCH_VISIBLE)) {
                isSearchVisible = savedInstanceState.getBoolean(SEARCH_VISIBLE);
                if (isSearchVisible) {
                    query = savedInstanceState.getString(QUERY);
                    switchToSearchFragment();
                }
            }
        }

        setTitle(getStringTitle(selectedButton));
        listFragmentPagerAdapter = new ListFragmentPagerAdapter(ListActivity.this, getSupportFragmentManager(), selectedButton);
        viewPager.setAdapter(listFragmentPagerAdapter);
        tabLayout.setupWithViewPager(viewPager);

        ibMovies.setSelected(true);
        footerMoviesLabel.setSelected(true);
        View.OnClickListener moviesClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectedButton = Constants.BUTTON_MOVIES;
                // switch fragment
                onFooterButtonClick();
            }
        };
        ibMovies.setOnClickListener(moviesClickListener);
        footerMoviesLabel.setOnClickListener(moviesClickListener);

        View.OnClickListener tvClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectedButton = Constants.BUTTON_TV_SHOWS;
                // switch fragment
                onFooterButtonClick();
            }
        };
        ibTv.setOnClickListener(tvClickListener);
        footerTvLabel.setOnClickListener(tvClickListener);
    }

    @Override
    protected void onResume() {
        // for receiver to know if activity running
        isListActive = true;
        networkLostReceiver = new NetworkLostReceiver();
        networkLostReceiver.setListActivityHandler(this);
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(ConnectivityManager.CONNECTIVITY_ACTION);
        registerReceiver(networkLostReceiver, intentFilter);
        super.onResume();
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

    private void switchToSearchFragment() {
        android.support.v4.app.FragmentManager fragmentManager = getSupportFragmentManager();
        android.support.v4.app.FragmentTransaction transaction = fragmentManager.beginTransaction();
        ListFragment searchFragment = ListFragment.newSearchInstance(selectedButton);
        transaction.replace(R.id.root_list_search_results_container, searchFragment, SEARCH_FRAG);
        transaction.commit();
        tabLayout.setVisibility(View.GONE);
        footerLayout.setVisibility(View.GONE);
        searchFragmentLayout.setVisibility(View.VISIBLE);
    }

    private void onFooterButtonClick() {
        if (selectedButton == Constants.BUTTON_MOVIES) {
            ibMovies.setSelected(true);
            footerMoviesLabel.setSelected(true);
            ibTv.setSelected(false);
            footerTvLabel.setSelected(false);
            setTitle(getStringTitle(selectedButton));
            // adapter loads frag based on selected button
            listFragmentPagerAdapter.setSelectedButton(selectedButton);
            listFragmentPagerAdapter.notifyDataSetChanged();
        } else {
            ibTv.setSelected(true);
            footerTvLabel.setSelected(true);
            ibMovies.setSelected(false);
            footerMoviesLabel.setSelected(false);
            setTitle(getStringTitle(selectedButton));
            // adapter loads frag based on selected button
            listFragmentPagerAdapter.setSelectedButton(selectedButton);
            listFragmentPagerAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.search_menu, menu);

        // Associate searchable configuration with the SearchView
        SearchManager searchManager =
                (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        SearchView searchView =
                (SearchView) menu.findItem(R.id.search).getActionView();
        searchView.setSearchableInfo(
                searchManager.getSearchableInfo(getComponentName()));

        MenuItem menuItem = menu.findItem(R.id.search);

        // on orientation change
        if (query != null) {
            searchView.clearFocus();
            menuItem.expandActionView();
            searchView.post(new Runnable() {
                @Override
                public void run() {
                    searchView.setQuery(query, false);
                }
            });
        }

        // no need to override onOptionsItemSelected
        menuItem.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                isSearchVisible = true;
                switchToSearchFragment();
                return false;
            }
        });

        // setOnCloseListener doesn't work so implemented this
        menuItem.setOnActionExpandListener(new MenuItem.OnActionExpandListener() {
            @Override
            public boolean onMenuItemActionExpand(MenuItem item) {
                return true;
            }

            @Override
            public boolean onMenuItemActionCollapse(MenuItem item) {
                searchFragmentLayout.setVisibility(View.GONE);
                isSearchVisible = false;
                tabLayout.setVisibility(View.VISIBLE);
                footerLayout.setVisibility(View.VISIBLE);
                return true;
            }
        });

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                query = newText;
                ListFragment listFragment = (ListFragment) getSupportFragmentManager().findFragmentByTag(SEARCH_FRAG);
                if (!newText.contentEquals("")) {
                    if (selectedButton == Constants.BUTTON_MOVIES) {
                        listFragment.searchMovies(newText);
                        return true;
                    } else {
                        listFragment.searchTvShows(newText);
                        return true;
                    }
                }
                return false;
            }
        });

        return super.onCreateOptionsMenu(menu);
    }

    private String getStringTitle(int selectedButton) {
        if (selectedButton == 0) {
            return getResources().getString(R.string.movies_label);
        } else {
            return getResources().getString(R.string.tv_label);
        }
    }

    // for broadcast receiver to trigger(refresh) when connection is re-established
    public void reload() {
        listFragmentPagerAdapter.setSelectedButton(selectedButton);
        listFragmentPagerAdapter.notifyDataSetChanged();
    }

    // called from NetworkLostReceiver only if there is no connection
    public void registerNetworkRegainedReceiver() {
        networkRegainedReceiver = new NetworkRegainedReceiver();
        networkRegainedReceiver.setListActivityHandler(this);
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(ConnectivityManager.CONNECTIVITY_ACTION);
        registerReceiver(networkRegainedReceiver, intentFilter);
        // so we can unregister
        isRegainedReceiverRegistered = true;
    }

    @Override
    protected void onPause() {
        isListActive = false;
        unregisterReceiver(networkLostReceiver);
        if (isRegainedReceiverRegistered) {
            unregisterReceiver(networkRegainedReceiver);
            isRegainedReceiverRegistered = false;
        }
        super.onPause();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        if (isSearchVisible) {
            outState.putString(QUERY, query);
        }
        outState.putInt(Constants.SELECTED_BUTTON, selectedButton);
        outState.putBoolean(SEARCH_VISIBLE, isSearchVisible);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}