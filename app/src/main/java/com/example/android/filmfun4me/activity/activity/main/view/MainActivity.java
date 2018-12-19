package com.example.android.filmfun4me.activity.activity.main.view;

import android.support.design.widget.TabLayout;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageButton;

import com.example.android.filmfun4me.R;
import com.example.android.filmfun4me.activity.activity.list.view.ListFragment;
import com.example.android.filmfun4me.activity.activity.list.view.ListFragmentAdapter;

import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity {

    private static final String MAIN_FRAG = "MAIN_FRAG";

    private ListFragmentAdapter adapter;

 
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        adapter = new ListFragmentAdapter(this, getSupportFragmentManager());

        android.support.v4.app.FragmentManager manager = getSupportFragmentManager();

        ListFragment fragment = (ListFragment) manager.findFragmentByTag(LIST_FRAG);

        if (fragment == null) {
            fragment = ListFragment.newInstance();
        }

        FragmentTransaction transaction = manager.beginTransaction();
        transaction.replace(R.id.root_activity_list, fragment, LIST_FRAG);
        transaction.commit();

    }
}
