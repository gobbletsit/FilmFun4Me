package com.example.android.filmfun4me.activity.activity.list.view;

import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.support.design.widget.TabLayout;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.example.android.filmfun4me.R;

public class ListActivity extends AppCompatActivity {

    public static final String LIST_FRAG = "LIST_FRAG";

    // Button prefs tag
    private static final String SELECTED_BUTTON = "selectedButton";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);

        int selectedButton;

        if (getIntent() != null){
            Bundle extras = getIntent().getExtras();
            if (extras != null){
                selectedButton = extras.getInt(SELECTED_BUTTON);
                ListFragmentAdapter listFragmentAdapter = new ListFragmentAdapter(this, getSupportFragmentManager(), selectedButton);
                ViewPager viewPager = this.findViewById(R.id.view_pager_list);
                TabLayout tabLayout = this.findViewById(R.id.tab_layout_list);
                viewPager.setAdapter(listFragmentAdapter);
                tabLayout.setupWithViewPager(viewPager);
            }
        }
    }
}
