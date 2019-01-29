package com.example.android.filmfun4me.activity.activity.list.view;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.example.android.filmfun4me.R;
import com.example.android.filmfun4me.utils.Constants;

/**
 * Created by gobov on 1/14/2018.
 */

public class ListFragmentPagerAdapter extends FragmentStatePagerAdapter {

    private Context context;

    private int selectedButton;

    ListFragmentPagerAdapter(Context context, FragmentManager fm, int selectedButton) {
        super(fm);

        // to get resource Strings
        this.context = context;
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
        // set which titles to retrieve based on SELECTED BUTTON
        if (selectedButton == Constants.BUTTON_MOVIES){
            if (position == 0){
                return context.getResources().getString(R.string.most_popular_label);
            } else if (position == 1){
                return context.getResources().getString(R.string.highest_rated_label);
            } else {
                return context.getResources().getString(R.string.upcoming_label);
            }
        } else if (selectedButton == Constants.BUTTON_TV_SHOWS){
            if (position == 0){
                return context.getResources().getString(R.string.most_popular_label);
            } else {
                return context.getResources().getString(R.string.highest_rated_label);
            }
        } else {
            return null;
        }
    }

    @Override
    public int getItemPosition(Object object) {
        return POSITION_NONE;
    }

    // to refresh without recreating
    void setSelectedButton(int selectedButton) {
        this.selectedButton = selectedButton;
    }
}
