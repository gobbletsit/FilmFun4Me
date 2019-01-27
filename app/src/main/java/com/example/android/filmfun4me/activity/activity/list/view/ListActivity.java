package com.example.android.filmfun4me.activity.activity.list.view;

import android.app.SearchManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.android.filmfun4me.NetworkChangeReceiver;
import com.example.android.filmfun4me.R;
import com.example.android.filmfun4me.activity.activity.detail.view.DetailActivity;
import com.example.android.filmfun4me.data.Movie;
import com.example.android.filmfun4me.data.TvShow;
import com.example.android.filmfun4me.utils.Constants;

public class ListActivity extends AppCompatActivity implements ListFragment.Callback {

    public static boolean isListActive;

    private static final String SEARCH_VISIBLE = "search_visible";
    private static final String SEARCH_FRAG = "SEARCH_FRAG";

    private FrameLayout searchFragmentLayout;
    private TabLayout tabLayout;
    private ConstraintLayout footer;

    private ImageButton ibMovies;
    private ImageButton ibTv;

    private TextView footerMoviesLabel;
    private TextView footerTvLabel;

    private int selectedButton;
    private boolean isSearchVisible;

    ListFragmentPagerAdapter listFragmentPagerAdapter;

    private String savedSearchQuery;

    private NetworkChangeReceiver networkChangeReceiver;

    @Override
    protected void onStart() {
        // for receiver to know if running
        isListActive = true;
        super.onStart();
    }

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
        footerMoviesLabel = findViewById(R.id.footer_movie_label);
        footerTvLabel = findViewById(R.id.footer_tv_label);

        // register only if there is no connection so we can reload when the connection is re-established
        if (!isNetworkAvailable()){
            networkChangeReceiver = new NetworkChangeReceiver();
            registerReceiver(networkChangeReceiver, new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION));
        }

        if (savedInstanceState != null){
            selectedButton = savedInstanceState.getInt(Constants.SELECTED_BUTTON);
            if (savedInstanceState.containsKey(SEARCH_VISIBLE)){
                isSearchVisible = savedInstanceState.getBoolean(SEARCH_VISIBLE);
                if (isSearchVisible){
                    savedSearchQuery = savedInstanceState.getString("saved_query");
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
                onFooterButtonClick();
            }
        };
        ibMovies.setOnClickListener(moviesClickListener);
        footerMoviesLabel.setOnClickListener(moviesClickListener);

        View.OnClickListener tvClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectedButton = Constants.BUTTON_TV_SHOWS;
                onFooterButtonClick();
            }
        };
        ibTv.setOnClickListener(tvClickListener);
        footerTvLabel.setOnClickListener(tvClickListener);
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

    private void switchToSearchFragment(){
        android.support.v4.app.FragmentManager fragmentManager = getSupportFragmentManager();
        android.support.v4.app.FragmentTransaction transaction = fragmentManager.beginTransaction();
        ListFragment searchFragment = ListFragment.newSearchInstance(selectedButton);
        transaction.replace(R.id.root_list_search_results_container, searchFragment, SEARCH_FRAG);
        transaction.commit();
        tabLayout.setVisibility(View.GONE);
        searchFragmentLayout.setVisibility(View.VISIBLE);
        footer.setVisibility(View.GONE);
    }

    private void onFooterButtonClick(){
        if (selectedButton == Constants.BUTTON_MOVIES){
            ibMovies.setSelected(true);
            footerMoviesLabel.setSelected(true);
            ibTv.setSelected(false);
            footerTvLabel.setSelected(false);
            setTitle(getStringTitle(selectedButton));
            listFragmentPagerAdapter.setSelectedButton(selectedButton);
            listFragmentPagerAdapter.notifyDataSetChanged();
        } else {
            ibTv.setSelected(true);
            footerTvLabel.setSelected(true);
            ibMovies.setSelected(false);
            footerMoviesLabel.setSelected(false);
            setTitle(getStringTitle(selectedButton));
            listFragmentPagerAdapter.setSelectedButton(selectedButton);
            listFragmentPagerAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.search:
                onSearchRequested();
                // for orientation change
                isSearchVisible = true;
                switchToSearchFragment();
                return super.onOptionsItemSelected(item);
        }
        return true;
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

        if (savedSearchQuery != null ){
            Log.i(ListActivity.class.getSimpleName(), "onCreateOptionsMenu: = " + savedSearchQuery);
            searchView.clearFocus();
        searchView.setIconified(false);
        menuItem.expandActionView();
        searchView.post(new Runnable() {
            @Override
            public void run() {
                searchView.setQuery(savedSearchQuery, false);
            }
        });
        }

        // setOnCloseListener doesn't work so implemented this
        menuItem.setOnActionExpandListener(new MenuItem.OnActionExpandListener() {
            @Override
            public boolean onMenuItemActionExpand(MenuItem item) {
                return true;
            }

            @Override
            public boolean onMenuItemActionCollapse(MenuItem item) {
                searchFragmentLayout.setVisibility(View.GONE);
                tabLayout.setVisibility(View.VISIBLE);
                footer.setVisibility(View.VISIBLE);
                isSearchVisible = false;
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
                savedSearchQuery = newText;
                if (!newText.contentEquals("")){
                    ListFragment listFragment = (ListFragment) getSupportFragmentManager().findFragmentByTag(SEARCH_FRAG);
                    if (selectedButton == Constants.BUTTON_MOVIES){
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

    private String getStringTitle(int selectedButton){
        if (selectedButton == 0){
            return "Movies";
        } else {
            return "TV shows";
        }
    }

    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    // for broadcast receiver to trigger when connection is re-established
    public void reload(){
        listFragmentPagerAdapter.setSelectedButton(selectedButton);
        listFragmentPagerAdapter.notifyDataSetChanged();
    }


    @Override
    protected void onStop() {
        isListActive = false;
        super.onStop();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        if (isSearchVisible){
            outState.putString("saved_query", savedSearchQuery);
        }
        outState.putInt(Constants.SELECTED_BUTTON, selectedButton);
        outState.putBoolean(SEARCH_VISIBLE, isSearchVisible);
    }

    @Override
    protected void onDestroy() {
        if (networkChangeReceiver != null){
            unregisterReceiver(networkChangeReceiver);
        }
        super.onDestroy();
    }
}
