package com.example.android.filmfun4me.activity.activity.main.view;


import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import com.example.android.filmfun4me.BaseApplication;
import com.example.android.filmfun4me.R;
import com.example.android.filmfun4me.activity.activity.main.presenter.MainPresenter;
import static com.example.android.filmfun4me.utils.Constants.*;

import javax.inject.Inject;


public class MainFragment extends Fragment implements MainView {

    @Inject
    MainPresenter presenter;

    private Callback callback;


    public MainFragment() {
        // Required empty public constructor
    }

    public static MainFragment newInstance() {
        return new MainFragment();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        callback = (Callback) context;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setRetainInstance(true);
        ((BaseApplication) getActivity().getApplication()).createMainComponent().inject(this);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_main, container, false);

        ImageButton moviesButton = new ImageButton(getContext());
        ImageButton tvShowsButton = new ImageButton(getContext());

        moviesButton = view.findViewById(R.id.image_button_movie);
        tvShowsButton = view.findViewById(R.id.image_button_tv);

        presenter.setView(this);

        moviesButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                callback.onMoviesClicked();
            }
        });

        tvShowsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                callback.onTvShowsClicked();
            }
        });

        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        ((BaseApplication) getActivity().getApplication()).releaseMainComponent();
    }

    public interface Callback {
        void onMoviesClicked();
        void onTvShowsClicked();
    }
}
