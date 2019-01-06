package com.example.android.filmfun4me.activity.activity.list.view;

import android.content.Intent;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;

import com.example.android.filmfun4me.R;
import com.example.android.filmfun4me.activity.activity.detail.view.DetailActivity;
import com.example.android.filmfun4me.data.Movie;
import com.example.android.filmfun4me.data.TvShow;
import com.example.android.filmfun4me.utils.Constants;

import java.util.ArrayList;

public class ListActivity extends AppCompatActivity implements ListFragment.Callback {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);

        int selectedButton;

        if (getIntent() != null){
            Bundle extras = getIntent().getExtras();
            if (extras != null){
                selectedButton = extras.getInt(Constants.SELECTED_BUTTON);
                setTitle(getStringTitle(selectedButton));
                ListFragmentPagerAdapter listFragmentPagerAdapter = new ListFragmentPagerAdapter(this, getSupportFragmentManager(), selectedButton);
                ViewPager viewPager = this.findViewById(R.id.view_pager_list);
                TabLayout tabLayout = this.findViewById(R.id.tab_layout_list);
                viewPager.setAdapter(listFragmentPagerAdapter);
                tabLayout.setupWithViewPager(viewPager);
            }
        }
    }

    @Override
    public void onMovieClicked(Movie movie, ArrayList<String> singleGenreNamesList, int selectedButton) {
        Intent detailIntent = new Intent(this, DetailActivity.class);
        Bundle extras = new Bundle();
        extras.putParcelable(Constants.KEY_MOVIE, movie);
        extras.putStringArrayList(Constants.KEY_GENRE_NAMES_LIST_MOVIE, singleGenreNamesList);
        extras.putInt(Constants.SELECTED_BUTTON, selectedButton);
        detailIntent.putExtras(extras);
        startActivity(detailIntent);
    }

    @Override
    public void onTvShowClicked(TvShow tvShow, ArrayList<String> singleGenreNamesList, int selectedButton) {
        Intent detailIntent = new Intent(this, DetailActivity.class);
        Bundle extras = new Bundle();
        extras.putParcelable(Constants.KEY_TV_SHOW, tvShow);
        extras.putStringArrayList(Constants.KEY_GENRE_NAMES_LIST_TV_SHOW, singleGenreNamesList);
        extras.putInt(Constants.SELECTED_BUTTON, selectedButton);
        detailIntent.putExtras(extras);
        startActivity(detailIntent);
    }

    private String getStringTitle(int selectedButton){
        if (selectedButton == 0){
            return "Movies";
        } else {
            return "TV shows";
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.options_menu, menu);
        return true;
    }
}
