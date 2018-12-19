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


public class MoviesFragment extends Fragment implements MainView {

    // Prefs name
    private static final String SELECTED_SHARED = "selectedShared";

    // Button prefs tag
    private static final String SELECTED_BUTTON_MOVIE = "selectedButton";

    //private static final String INT_POSITION = "intPosition";
    private static final String KEY_FRAGMENT_POSITION = "position";


    @Inject
    MainPresenter presenter;

    private ImageButton moviesButton;
    private ImageButton tvShowsButton;

    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;

    private int fragmentPosition;

    public MoviesFragment() {
        // Required empty public constructor
    }

    public static MoviesFragment newInstance(int position) {
        MoviesFragment moviesFragment = new MoviesFragment();
        Bundle args = new Bundle();
        args.putInt(KEY_FRAGMENT_POSITION, position);
        moviesFragment.setArguments(args);
        return moviesFragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setRetainInstance(true);
        ((BaseApplication) getActivity().getApplication()).createMainComponent().inject(this);

        // To pass along
        fragmentPosition = getArguments().getInt(KEY_FRAGMENT_POSITION);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_movies, container, false);

        moviesButton = new ImageButton(getContext());
        tvShowsButton = new ImageButton(getContext());

        moviesButton = view.findViewById(R.id.button_popular);
        tvShowsButton = view.findViewById(R.id.button_highest_rated);

        presenter.setView(this);

        moviesButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                presenter.goToMostPopularMoviesList(fragmentPosition);
            }
        });

        tvShowsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                presenter.goToHighestRatedMoviesList(fragmentPosition);
            }
        });

        return view;
    }

    @Override
    public void onPopularButtonClick(int fragmentPosition) {


        sharedPreferences = getActivity().getSharedPreferences(SELECTED_SHARED, 0);
        editor = sharedPreferences.edit();

        // For ListInteractor to know which list to get
        editor.putInt(SELECTED_BUTTON_MOVIE, 0);

        // For DetailInteractor
        editor.putInt(KEY_FRAGMENT_POSITION, fragmentPosition);
        editor.apply();

        Intent intent = new Intent(getActivity(), ListActivity.class);
        intent.putExtra(KEY_FRAGMENT_POSITION, fragmentPosition);
        startActivity(intent);
    }

    @Override
    public void onHighestRatedButtonClick(int fragmentPosition) {


        sharedPreferences = getActivity().getSharedPreferences(SELECTED_SHARED, 0);
        editor = sharedPreferences.edit();

        // For ListInteractor to know which list to get
        editor.putInt(SELECTED_BUTTON_MOVIE, 1);

        // For DetailInteractor
        editor.putInt(KEY_FRAGMENT_POSITION, fragmentPosition);
        editor.apply();

        Intent intent = new Intent(getActivity(), ListActivity.class);
        intent.putExtra(KEY_FRAGMENT_POSITION, fragmentPosition);
        startActivity(intent);
    }

    @Override
    public void onUpcomingButtonClick(int fragmentPosition) {


        sharedPreferences = getActivity().getSharedPreferences(SELECTED_SHARED, 0);
        editor = sharedPreferences.edit();

        // For ListInteractor to know which list to get
        editor.putInt(SELECTED_BUTTON_MOVIE, 2);

        // For DetailInteractor
        editor.putInt(KEY_FRAGMENT_POSITION, fragmentPosition);
        editor.apply();

        Intent intent = new Intent(getActivity(), ListActivity.class);
        intent.putExtra(KEY_FRAGMENT_POSITION, fragmentPosition);
        startActivity(intent);
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
