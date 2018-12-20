package com.example.android.filmfun4me.activity.activity.main.view;

import android.content.Intent;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.example.android.filmfun4me.R;
import com.example.android.filmfun4me.activity.activity.list.view.ListActivity;

public class MainActivity extends AppCompatActivity implements MainFragment.Callback {

    private static final String MAIN_FRAG = "MAIN_FRAG";
    private static final String SELECTED_BUTTON = "selectedButton";
    private static final int BUTTON_MOVIES = 0;
    private static final int BUTTON_TV_SHOWS = 1;
 
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        android.support.v4.app.FragmentManager manager = getSupportFragmentManager();

        MainFragment fragment = (MainFragment) manager.findFragmentByTag(MAIN_FRAG);

        if (fragment == null) {
            fragment = MainFragment.newInstance();
        }

        FragmentTransaction transaction = manager.beginTransaction();
        transaction.replace(R.id.root_activity_main, fragment, MAIN_FRAG);
        transaction.commit();

    }

    @Override
    public void onMoviesClicked() {
        Intent intent = new Intent(this, ListActivity.class);
        Bundle extras = new Bundle();
        extras.putInt(SELECTED_BUTTON, BUTTON_MOVIES);
        intent.putExtras(extras);
        startActivity(intent);
    }

    @Override
    public void onTvShowsClicked() {
        Intent intent = new Intent(this, ListActivity.class);
        Bundle extras = new Bundle();
        extras.putInt(SELECTED_BUTTON, BUTTON_TV_SHOWS);
        intent.putExtras(extras);
        startActivity(intent);
    }
}
