package com.example.android.filmfun4me.activity.activity.list.view;

import android.app.SearchManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.SearchView;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.example.android.filmfun4me.R;
import com.example.android.filmfun4me.activity.activity.detail.view.DetailMovieFragment;
import com.example.android.filmfun4me.data.Movie;
import com.example.android.filmfun4me.data.TvShow;
import com.example.android.filmfun4me.utils.Constants;

public class SearchActivity extends AppCompatActivity implements ListFragment.Callback {

    private static final String SEARCH_FRAG = "SEARCH_FRAG";

    int selectedButton = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        Intent intent = getIntent();

        FragmentManager manager = getSupportFragmentManager();
        ListFragment fragment = ListFragment.newSearchInstance(0);
        FragmentTransaction transaction = manager.beginTransaction();
        transaction.replace(R.id.root_activity_search, fragment, SEARCH_FRAG);
        transaction.commit();

        if (Intent.ACTION_SEARCH.equals(intent.getAction())) {
            String query = intent.getStringExtra(SearchManager.QUERY);
            fragment.searchMovies(query);
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
                searchManager.getSearchableInfo(new ComponentName(this, SearchActivity.class)));


        //if (savedSearchQuery != null){
            searchView.setIconifiedByDefault(false);
            searchView.onActionViewExpanded();
            //searchView.setQuery(savedSearchQuery, false);
            searchView.setFocusable(true);
        //}

        // setOnCloseListener doesn't work so implemented this
        MenuItem menuItem = menu.findItem(R.id.search);
        menuItem.setOnActionExpandListener(new MenuItem.OnActionExpandListener() {
            @Override
            public boolean onMenuItemActionExpand(MenuItem item) {
                return true;
            }

            @Override
            public boolean onMenuItemActionCollapse(MenuItem item) {
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
                //savedSearchQuery = newText;
                if (!newText.contentEquals("")){
                    if (selectedButton == Constants.BUTTON_MOVIES){
                        ListFragment searchFragment = (ListFragment) getSupportFragmentManager().findFragmentByTag(SEARCH_FRAG);
                        searchFragment.searchMovies(newText);
                        return true;
                    } else {
                        //listPresenter.showTvSearchResults(newText);
                    }
                }
                return false;
            }
        });

        return true;
    }

    @Override
    public void onMovieClicked(Movie movie, String singleMovieGenres, int selectedButton) {

    }

    @Override
    public void onTvShowClicked(TvShow tvShow, String singleTvShowGenres, int selectedButton) {

    }

}
