package com.example.android.filmfun4me.activity.activity.list.view;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.example.android.filmfun4me.R;
import com.example.android.filmfun4me.activity.activity.main.view.MainFragment;
import com.example.android.filmfun4me.activity.activity.main.view.TvShowsFragment;

/**
 * Created by gobov on 1/14/2018.
 */

public class ListFragmentAdapter extends FragmentPagerAdapter{

    private Context mContext;

    private SharedPreferences sharedPreferences;

    private static final String SELECTED_BUTTON = "selectedButton";

    private static final int BUTTON_MOVIES = 0;
    private static final int BUTTON_TVSHOWS = 1;

    ListFragmentAdapter(Context context, FragmentManager fm) {
        super(fm);

        // for strings
        mContext = context;
        sharedPreferences = mContext.getSharedPreferences(SELECTED_BUTTON, 0);
    }


    @Override
    public Fragment getItem(int position) {

        Fragment frag = null;
        return ListFragment.newInstance(position);
    }

    @Override
    public int getCount() {
        return 3;
    }


    @Override
    public CharSequence getPageTitle(int position) {
        // you will set which names to retrieve based SELECTED BUTTON
        if (position == 0){
            return "Most Popular";
        } else if (position == 1){
            return "Highest rated";
        } else {
            return "Upcoming";
        }
    }
}
