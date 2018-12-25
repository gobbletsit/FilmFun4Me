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

public class ListFragmentPagerAdapter extends FragmentPagerAdapter{

    private Context mContext;

    private int selectedButton;

    ListFragmentPagerAdapter(Context context, FragmentManager fm, int selectedButton) {
        super(fm);

        // for strings
        mContext = context;
        this.selectedButton = selectedButton;
    }


    @Override
    public Fragment getItem(int position) {
        return ListFragment.newInstance(position, selectedButton);
    }

    @Override
    public int getCount() {
        if (selectedButton == 0){
            return 3;
        } else {
            return 2;
        }
    }


    @Override
    public CharSequence getPageTitle(int position) {
        // you will set which names to retrieve based on SELECTED BUTTON
        if (selectedButton == 0){
            if (position == 0){
                return "Most Popular";
            } else if (position == 1){
                return "Highest rated";
            } else {
                return "Upcoming";
            }
        } else {
            if (position == 1){
                return "Most Popular";
            } else {
                return "Highest rated";
            }
        }
    }
}
