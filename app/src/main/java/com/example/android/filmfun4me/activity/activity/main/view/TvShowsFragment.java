package com.example.android.filmfun4me.activity.activity.main.view;


import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import com.example.android.filmfun4me.BaseApplication;
import com.example.android.filmfun4me.R;
import com.example.android.filmfun4me.activity.activity.list.view.ListActivity;
import com.example.android.filmfun4me.activity.activity.main.presenter.MainPresenter;

import javax.inject.Inject;


public class TvShowsFragment extends Fragment implements MainView {

    // Prefs name
    private static final String SELECTED_SHARED = "selectedShared";

    // Prefs button tag
    private static final String SELECTED_BUTTON_TV_SHOW = "selectedButtonTv";

    //private static final String INT_POSITION = "intPosition";
    private static final String KEY_FRAGMENT_POSITION = "position";

    @Inject
    MainPresenter presenter;

    private ImageButton popularButton;
    private ImageButton highestRatedButton;

    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;

    private int fragmentPosition;


    public TvShowsFragment() {
        // Required empty public constructor
    }


    public static TvShowsFragment newInstance(int position) {
        TvShowsFragment tvShowsFragment = new TvShowsFragment();
        Bundle args = new Bundle();
        args.putInt(KEY_FRAGMENT_POSITION, position);
        tvShowsFragment.setArguments(args);
        return tvShowsFragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ((BaseApplication) getActivity().getApplication()).createMainComponent().inject(this);

        // To pass along
        fragmentPosition = getArguments().getInt(KEY_FRAGMENT_POSITION);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_tv_shows, container, false);

        popularButton = new ImageButton(getContext());
        highestRatedButton = new ImageButton(getContext());

        popularButton = view.findViewById(R.id.button_popular_tv_show);
        highestRatedButton = view.findViewById(R.id.button_highest_rated_tv_show);

        presenter.setView(this);

        popularButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                presenter.goToMostPopularTvShowsList(fragmentPosition);
            }
        });


        highestRatedButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                presenter.goToHighestRatedTvShowsList(fragmentPosition);
            }
        });

        return view;
    }

    @Override
    public void onPopularButtonClick(int fragmentPosition) {


        sharedPreferences = getActivity().getSharedPreferences(SELECTED_SHARED, 0);
        editor = sharedPreferences.edit();
        editor.putInt(KEY_FRAGMENT_POSITION, fragmentPosition);

        // For ListInteractor to know which list to get
        editor.putInt(SELECTED_BUTTON_TV_SHOW, 0);
        editor.apply();

        Intent intent = new Intent(getActivity(), ListActivity.class);
        intent.putExtra(KEY_FRAGMENT_POSITION, fragmentPosition);
        startActivity(intent);

    }

    @Override
    public void onHighestRatedButtonClick(int fragmentPosition) {

        sharedPreferences = getActivity().getSharedPreferences(SELECTED_SHARED, 0);
        editor = sharedPreferences.edit();
        editor.putInt(KEY_FRAGMENT_POSITION, fragmentPosition);

        // For ListInteractor to know which list to get
        editor.putInt(SELECTED_BUTTON_TV_SHOW, 1);
        editor.apply();

        Intent intent = new Intent(getActivity(), ListActivity.class);
        intent.putExtra(KEY_FRAGMENT_POSITION, fragmentPosition);
        startActivity(intent);

    }

    @Override
    public void onUpcomingButtonClick(int fragmentPosition) {
        // ONLY HERE BECAUSE OF THE MAIN VIEW IMPLEMENTATION
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        sharedPreferences = getActivity().getSharedPreferences(SELECTED_SHARED, 0);
        editor = sharedPreferences.edit();
        editor.clear().apply();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        ((BaseApplication) getActivity().getApplication()).releaseMainComponent();
    }
}
