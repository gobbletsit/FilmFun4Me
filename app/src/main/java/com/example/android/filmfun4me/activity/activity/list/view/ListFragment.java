package com.example.android.filmfun4me.activity.activity.list.view;


import android.content.Context;
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
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.android.filmfun4me.BaseApplication;
import com.example.android.filmfun4me.R;
import com.example.android.filmfun4me.activity.activity.list.presenter.ListPresenter;
import com.example.android.filmfun4me.data.Movie;
import com.example.android.filmfun4me.data.TvShow;
import com.example.android.filmfun4me.utils.Constants;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import jp.wasabeef.recyclerview.animators.adapters.ScaleInAnimationAdapter;


public class ListFragment extends Fragment implements ListView {

    @Inject
    ListPresenter listPresenter;

    @Inject
    RecyclerView.Adapter customAdapter;

    @BindView(R.id.tv_list_not_available)
    TextView tvListNotAvailable;
    @BindView(R.id.rec_list_activity)
    RecyclerView recyclerView;
    @BindView(R.id.list_progress_bar)
    ProgressBar progressBar;

    private int selectedButton;
    private int pagerPosition;

    private ScaleInAnimationAdapter scaleInAnimationAdapter;

    private Callback callback;

    public ListFragment() {
        // Required empty public constructor
    }

    public static ListFragment newInstance(int position, int selectedButton) {
        ListFragment listFragment = new ListFragment();
        Bundle args = new Bundle();
        args.putInt(Constants.PAGER_POSITION, position);
        args.putInt(Constants.SELECTED_BUTTON, selectedButton);
        listFragment.setArguments(args);
        return listFragment;
    }

    public static ListFragment newSearchInstance(int selectedButton) {
        ListFragment listFragment = new ListFragment();
        Bundle args = new Bundle();
        args.putInt(Constants.SELECTED_BUTTON, selectedButton);
        listFragment.setArguments(args);
        return listFragment;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (savedInstanceState != null) {
            if (savedInstanceState.containsKey(Constants.PAGER_POSITION)) {
                pagerPosition = (int) savedInstanceState.get(Constants.PAGER_POSITION);
            }
            if (savedInstanceState.containsKey(Constants.SELECTED_BUTTON)) {
                selectedButton = savedInstanceState.getInt(Constants.SELECTED_BUTTON);
            }
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

        ButterKnife.bind(this, view);

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

        if (getArguments() != null) {
            if (getArguments().containsKey(Constants.SELECTED_BUTTON)) {
                selectedButton = (int) getArguments().get(Constants.SELECTED_BUTTON);
                if (getArguments().containsKey(Constants.PAGER_POSITION) && selectedButton == Constants.BUTTON_MOVIES) {
                    pagerPosition = (int) getArguments().get(Constants.PAGER_POSITION);
                    listPresenter.setMovieView(this, pagerPosition);
                } else if (getArguments().containsKey(Constants.PAGER_POSITION) && selectedButton == Constants.BUTTON_TV_SHOWS) {
                    pagerPosition = (int) getArguments().get(Constants.PAGER_POSITION);
                    listPresenter.setTvShowView(this, pagerPosition);
                } else {
                    listPresenter.setSearchView(this, selectedButton);
                }
            }
        }
    }

    @Override
    public void setUpMovieView() {
        setAnimationAdapter();
    }

    @Override
    public void setUpMovieSearchView() {
        if (scaleInAnimationAdapter == null) {
            setAnimationAdapter();
        }
        customAdapter.notifyDataSetChanged();
        scaleInAnimationAdapter.notifyDataSetChanged();
    }

    @Override
    public void setUpTvShowView() {
        setAnimationAdapter();
    }

    @Override
    public void setUpTvSearchView() {
        if (scaleInAnimationAdapter == null) {
            setAnimationAdapter();
        }
        customAdapter.notifyDataSetChanged();
        scaleInAnimationAdapter.notifyDataSetChanged();
    }

    @Override
    public void onMovieClicked(Movie movie, String singleMovieGenres) {
        callback.onMovieClicked(movie, singleMovieGenres, selectedButton);
    }

    @Override
    public void onTvShowClicked(TvShow tvShow, String singleTvShowGenres) {
        callback.onTvShowClicked(tvShow, singleTvShowGenres, selectedButton);
    }

    @Override
    public void showLoading() {
        progressBar.setVisibility(View.VISIBLE);
    }

    @Override
    public void onLoadingFinished() {
        progressBar.setVisibility(View.GONE);
    }

    @Override
    public void loadingErrorMessage(String error) {
        progressBar.setVisibility(View.GONE);
        tvListNotAvailable.setVisibility(View.VISIBLE);
        if (error.contains("only-if-cached")) {
            tvListNotAvailable.setText(getResources().getString(R.string.list_check_internet));
        } else {
            Toast.makeText(getActivity(), error, Toast.LENGTH_LONG).show();
        }
    }

    private void setAnimationAdapter() {
        scaleInAnimationAdapter = new ScaleInAnimationAdapter(customAdapter);
        scaleInAnimationAdapter.setDuration(400);
        scaleInAnimationAdapter.setInterpolator(new OvershootInterpolator());
        // disable the first scroll mode
        scaleInAnimationAdapter.setFirstOnly(false);
        scaleInAnimationAdapter.notifyDataSetChanged();
        recyclerView.setAdapter(scaleInAnimationAdapter);
    }

    void searchMovies(String query) {
        listPresenter.showMovieSearchResults(query);
    }

    void searchTvShows(String query) {
        listPresenter.showTvSearchResults(query);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(Constants.PAGER_POSITION, pagerPosition);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        listPresenter.destroy();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        ((BaseApplication) getActivity().getApplication()).releaseListComponent();
    }

    @Override
    public void onDetach() {
        super.onDetach();
        // to avoid leaking
        callback = null;
    }

    public interface Callback {
        void onMovieClicked(Movie movie, String singleMovieGenres, int selectedButton);

        void onTvShowClicked(TvShow tvShow, String singleTvShowGenres, int selectedButton);
    }
}
