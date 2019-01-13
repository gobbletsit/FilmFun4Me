package com.example.android.filmfun4me.activity.activity.list.view;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.example.android.filmfun4me.utils.Constants;

/**
 * Created by gobov on 1/14/2018.
 */

public class ListFragmentPagerAdapter extends FragmentStatePagerAdapter {

    // for resource string titles
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
        if (selectedButton == Constants.BUTTON_MOVIES){
            return 3;
        } else {
            return 2;
        }
    }


    @Override
    public CharSequence getPageTitle(int position) {
        // you will set which titles to retrieve based on SELECTED BUTTON
        if (selectedButton == Constants.BUTTON_MOVIES){
            if (position == 0){
                return "Most Popular";
            } else if (position == 1){
                return "Highest rated";
            } else {
                return "Upcoming";
            }
        } else if (selectedButton == Constants.BUTTON_TV_SHOWS){
            if (position == 0){
                return "Most Popular";
            } else {
                return "Highest rated";
            }
        } else {
            return null;
        }
    }
}
