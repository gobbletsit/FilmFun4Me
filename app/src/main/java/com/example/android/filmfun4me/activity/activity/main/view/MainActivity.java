package com.example.android.filmfun4me.activity.activity.main.view;

import android.support.design.widget.TabLayout;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.example.android.filmfun4me.R;
import com.example.android.filmfun4me.activity.activity.list.view.ListFragmentAdapter;

public class MainActivity extends AppCompatActivity {

    private ListFragmentAdapter adapter;

    private TabLayout tabLayout;

    private ViewPager viewPager;
 
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        adapter = new ListFragmentAdapter(this, getSupportFragmentManager());

        viewPager = findViewById(R.id.view_pager_list);
        viewPager.setAdapter(adapter);

        tabLayout = findViewById(R.id.tab_layout_list);
        tabLayout.setupWithViewPager(viewPager);

    }
}
