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

    @Inject
    ListPresenter listPresenter;

    private FrameLayout searchFragmentLayout;
    private TabLayout tabLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);

        ((BaseApplication) this.getApplication()).createListComponent().inject(this);

        searchFragmentLayout = findViewById(R.id.root_list_search_results_container);
        tabLayout = findViewById(R.id.tab_layout_list);
        ViewPager viewPager = this.findViewById(R.id.view_pager_list);

        int selectedButton;
        //Intent intent = getIntent();

        if (getIntent() != null){
            Bundle extras = getIntent().getExtras();
            if (extras != null){
                selectedButton = extras.getInt(Constants.SELECTED_BUTTON);
                setTitle(getStringTitle(selectedButton));
                ListFragmentPagerAdapter listFragmentPagerAdapter = new ListFragmentPagerAdapter(this, getSupportFragmentManager(), selectedButton);
                /*tabLayout.setVisibility(View.VISIBLE);
                searchFragmentLayout.setVisibility(View.INVISIBLE);*/
                viewPager.setAdapter(listFragmentPagerAdapter);
                tabLayout.setupWithViewPager(viewPager);
            }
        }
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
        /*Intent startSearchActivity = new Intent(this, SearchActivity.class);
        startActivity(startSearchActivity);*/
    }

    private String getStringTitle(int selectedButton){
        if (selectedButton == 0){
            return "Movies";
        } else {
            return "TV shows";
        }
    }

    /*@Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.options_menu, menu);
        // Associate searchable configuration with the SearchView
        SearchManager searchManager =
                (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        SearchView searchView =
                (SearchView) menu.findItem(R.id.search).getActionView();
        searchView.setSearchableInfo(
                searchManager.getSearchableInfo(getComponentName()));

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                listPresenter.showSearchResults(newText);
                return false;
            }
        });

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.search:
                android.support.v4.app.FragmentManager manager = getSupportFragmentManager();
                ListFragment fragment = ListFragment.newSearchInstance();
                android.support.v4.app.FragmentTransaction transaction = manager.beginTransaction();
                transaction.replace(R.id.root_list_search_results_container, fragment);
                tabLayout.setVisibility(View.GONE);
                searchFragmentLayout.setVisibility(View.VISIBLE);
                transaction.commit();

                return true;
        }
        return super.onOptionsItemSelected(item);
    }*/
}
