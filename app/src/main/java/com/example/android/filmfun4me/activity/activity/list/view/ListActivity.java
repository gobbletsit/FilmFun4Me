package com.example.android.filmfun4me.activity.activity.list.view;

import android.app.FragmentManager;
import android.content.SharedPreferences;
import android.support.design.widget.TabLayout;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

import com.example.android.filmfun4me.R;

public class ListActivity extends AppCompatActivity {

    public static final String LIST_FRAG = "LIST_FRAG";

    // Prefs name
    private static final String SELECTED_SHARED = "selectedShared";

    // Button prefs tag
    private static final String SELECTED_BUTTON_MOVIE = "selectedButton";

    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);

        android.support.v4.app.FragmentManager manager = getSupportFragmentManager();

        ListFragment fragment = (ListFragment) manager.findFragmentByTag(LIST_FRAG);

        if (fragment == null) {
            fragment = ListFragment.newInstance();
        }

        FragmentTransaction transaction = manager.beginTransaction();
        transaction.replace(R.id.root_activity_list, fragment, LIST_FRAG);
        transaction.commit();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.most_popular_item: // strpaj u shared prefs ko button;
                sharedPreferences = this.getSharedPreferences(SELECTED_SHARED, 0);
                editor = sharedPreferences.edit();

                // For ListInteractor to know which list to get
                editor.putInt(SELECTED_BUTTON_MOVIE, 0);
                editor.apply();

                uploadFragment();

                Toast.makeText(this, "Jedenje kurca 1", Toast.LENGTH_SHORT).show();
                return true;

            case R.id.highest_item: // strpaj u shared prefs ko button;

                sharedPreferences = this.getSharedPreferences(SELECTED_SHARED, 0);
                editor = sharedPreferences.edit();

                // For ListInteractor to know which list to get
                editor.putInt(SELECTED_BUTTON_MOVIE, 1);
                editor.apply();

                uploadFragment();

                Toast.makeText(this, "Jedenje kurca 22", Toast.LENGTH_SHORT).show();
                return true;

            case R.id.upcoming_item: // strpaj u shared prefs ko button;

                sharedPreferences = this.getSharedPreferences(SELECTED_SHARED, 0);
                editor = sharedPreferences.edit();

                // For ListInteractor to know which list to get
                editor.putInt(SELECTED_BUTTON_MOVIE, 2);

                editor.apply();

                uploadFragment();

                Toast.makeText(this, "Jedenje kurca 333", Toast.LENGTH_SHORT).show();
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }

    }

    public void uploadFragment(){
        android.support.v4.app.FragmentManager manager = getSupportFragmentManager();

        ListFragment fragment = (ListFragment) manager.findFragmentByTag(LIST_FRAG);


        fragment = ListFragment.newInstance();


        FragmentTransaction transaction = manager.beginTransaction();
        transaction.replace(R.id.root_activity_list, fragment, LIST_FRAG);
        transaction.commit();
    }
}
