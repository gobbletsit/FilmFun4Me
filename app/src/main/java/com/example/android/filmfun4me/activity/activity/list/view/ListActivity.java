package com.example.android.filmfun4me.activity.activity.list.view;

import android.content.Intent;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.example.android.filmfun4me.R;
import com.example.android.filmfun4me.activity.activity.detail.view.DetailActivity;
import com.example.android.filmfun4me.data.Movie;
import com.example.android.filmfun4me.data.TvShow;

import java.util.ArrayList;

public class ListActivity extends AppCompatActivity implements ListFragment.Callback {

    public static final String LIST_FRAG = "LIST_FRAG";

    // Button prefs tag
    private static final String SELECTED_BUTTON = "selectedButton";

    private static final String KEY_MOVIE = "movie";
    private static final String KEY_TV_SHOW = "tv_show";
    private static final String KEY_GENRE_NAMES_LIST_MOVIE = "genreNames";
    private static final String KEY_GENRE_NAMES_LIST_TV_SHOW = "showGenreNames";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);

        int selectedButton;

        if (getIntent() != null){
            Bundle extras = getIntent().getExtras();
            if (extras != null){
                selectedButton = extras.getInt(SELECTED_BUTTON);
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
        extras.putParcelable(KEY_MOVIE, movie);
        extras.putStringArrayList(KEY_GENRE_NAMES_LIST_MOVIE, singleGenreNamesList);
        extras.putInt(SELECTED_BUTTON, selectedButton);
        detailIntent.putExtras(extras);
        startActivity(detailIntent);
    }

    @Override
    public void onTvShowClicked(TvShow tvShow, ArrayList<String> singleGenreNamesList, int selectedButton) {
        Intent detailIntent = new Intent(this, DetailActivity.class);
        Bundle extras = new Bundle();
        extras.putParcelable(KEY_TV_SHOW, tvShow);
        extras.putStringArrayList(KEY_GENRE_NAMES_LIST_TV_SHOW, singleGenreNamesList);
        extras.putInt(SELECTED_BUTTON, selectedButton);
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
}
