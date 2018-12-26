package com.example.android.filmfun4me.activity.activity.list.view;


import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.OvershootInterpolator;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.android.filmfun4me.BaseApplication;
import com.example.android.filmfun4me.R;
import com.example.android.filmfun4me.activity.activity.list.presenter.ListPresenter;
import com.example.android.filmfun4me.data.Genre;
import com.example.android.filmfun4me.data.Movie;
import com.example.android.filmfun4me.data.TvShow;
import com.example.android.filmfun4me.utils.BaseUtils;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import jp.wasabeef.recyclerview.animators.adapters.ScaleInAnimationAdapter;


public class ListFragment extends Fragment implements ListView {

    private static final String TAG = ListFragment.class.getSimpleName();

    @Inject
    ListPresenter listPresenter;

    private static final String PAGER_POSITION = "pager_position";
    private static final String SELECTED_BUTTON = "selectedButton";
    private static final int BUTTON_MOVIES = 0;
    private static final int BUTTON_TV_SHOWS = 1;

    private int selectedButton;
    private int pagerPosition;

    private RecyclerView recyclerView;
    private RecyclerView.Adapter customAdapter;
    private ScaleInAnimationAdapter scaleInAnimationAdapter;

    private Callback callback;

    public ListFragment() {
        // Required empty public constructor
    }


    public static ListFragment newInstance(int position, int selectedButton) {
        ListFragment listFragment = new ListFragment();
        Bundle args = new Bundle();
        args.putInt(PAGER_POSITION, position);
        args.putInt(SELECTED_BUTTON, selectedButton);
        listFragment.setArguments(args);
        return listFragment;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (savedInstanceState != null){
            pagerPosition = (int) savedInstanceState.get(PAGER_POSITION);
        }
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

        ((BaseApplication) getActivity().getApplication()).createListComponent().inject(this);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_list, container, false);

        recyclerView = view.findViewById(R.id.rec_list_activity);

        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);

        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(recyclerView.getContext(), layoutManager.getOrientation());
        dividerItemDecoration.setDrawable(ContextCompat.getDrawable(getActivity(), R.drawable.recycler_divider_with_spacing));
        recyclerView.addItemDecoration(dividerItemDecoration);

        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        if (getArguments() != null && getArguments().containsKey(PAGER_POSITION) && getArguments().containsKey(SELECTED_BUTTON)){
            pagerPosition = (int) getArguments().get(PAGER_POSITION);
            selectedButton = (int) getArguments().get(SELECTED_BUTTON);

            if (isNetworkAvailable()&& selectedButton == BUTTON_MOVIES) {
                listPresenter.setMovieView(this, pagerPosition);
            } else if (isNetworkAvailable() && selectedButton == BUTTON_TV_SHOWS){
                listPresenter.setTvShowView(this, pagerPosition);
            }
        }
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        listPresenter.destroy();
    }

    @Override
    public void setUpMovieView() {
        customAdapter = new ListMovieRecyclerAdapter(listPresenter);
        scaleInAnimationAdapter = new ScaleInAnimationAdapter(customAdapter);
        scaleInAnimationAdapter.setDuration(400);
        scaleInAnimationAdapter.setInterpolator(new OvershootInterpolator());
        // disable the first scroll mode
        scaleInAnimationAdapter.setFirstOnly(false);
        scaleInAnimationAdapter.notifyDataSetChanged();
        recyclerView.setAdapter(scaleInAnimationAdapter);

    }

    @Override
    public void setUpTvShowView() {
        customAdapter = new ListTvShowRecyclerAdapter(listPresenter);
        scaleInAnimationAdapter = new ScaleInAnimationAdapter(customAdapter);
        scaleInAnimationAdapter.setDuration(400);
        scaleInAnimationAdapter.setInterpolator(new OvershootInterpolator());
        // disable the first scroll mode
        scaleInAnimationAdapter.setFirstOnly(false);
        scaleInAnimationAdapter.notifyDataSetChanged();
        recyclerView.setAdapter(scaleInAnimationAdapter);
    }


    // ovo dvoje treba mijenjat, samo si ovako napravio kako bi radilo
    @Override
    public void onMovieClicked(Movie movie, ArrayList<String> singleGenreNamesList) {
        callback.onMovieClicked(movie, singleGenreNamesList, selectedButton);
    }

    @Override
    public void onTvShowClicked(TvShow tvShow, ArrayList<String> singleGenreNamesList) {
        callback.onTvShowClicked(tvShow, singleGenreNamesList, selectedButton);
    }


    @Override
    public void showLoading() {

    }

    @Override
    public void loadingErrorMessage(String error) {
        Toast.makeText(getActivity(), error, Toast.LENGTH_LONG).show();
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        ((BaseApplication) getActivity().getApplication()).releaseListComponent();
    }

    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager = (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();

        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(PAGER_POSITION,pagerPosition);
    }

    public interface Callback {
        void onMovieClicked(Movie movie, ArrayList<String> singleGenreNamesList, int selectedButton);
        void onTvShowClicked(TvShow tvShow, ArrayList<String> singleGenreNamesList, int selectedButton);
    }

}
