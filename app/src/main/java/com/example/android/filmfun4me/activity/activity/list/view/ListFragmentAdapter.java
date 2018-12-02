package com.example.android.filmfun4me.activity.activity.list.view;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.example.android.filmfun4me.R;
import com.example.android.filmfun4me.activity.activity.main.view.MoviesFragment;
import com.example.android.filmfun4me.activity.activity.main.view.TvShowsFragment;

/**
 * Created by gobov on 1/14/2018.
 */

public class ListFragmentAdapter extends FragmentPagerAdapter{

    private Context mContext;

    public ListFragmentAdapter(Context context, FragmentManager fm) {
        super(fm);
        mContext = context;
    }


    @Override
    public Fragment getItem(int position) {

        Fragment frag = null;
        if (position == 0){
            frag = MoviesFragment.newInstance(position);
        } else {
            frag = TvShowsFragment.newInstance(position);
        }
        return frag;
    }

    @Override
    public int getCount() {
        return 2;
    }


    @Override
    public CharSequence getPageTitle(int position) {
        if (position == 0){
            return mContext.getString(R.string.movie_tab_title);
        } else {
            return mContext.getString(R.string.tv_show_tab_title);
        }
    }
}
