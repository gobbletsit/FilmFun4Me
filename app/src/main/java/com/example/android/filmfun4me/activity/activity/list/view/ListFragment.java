package com.example.android.filmfun4me.activity.activity.list.view;


import android.app.SearchManager;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.OvershootInterpolator;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.android.filmfun4me.BaseApplication;
import com.example.android.filmfun4me.R;
import com.example.android.filmfun4me.activity.activity.list.presenter.ListPresenter;
import com.example.android.filmfun4me.activity.activity.main.view.MainActivity;
import com.example.android.filmfun4me.data.Genre;
import com.example.android.filmfun4me.data.Movie;
import com.example.android.filmfun4me.data.TvShow;
import com.example.android.filmfun4me.utils.BaseUtils;
import com.example.android.filmfun4me.utils.Constants;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import jp.wasabeef.recyclerview.animators.adapters.ScaleInAnimationAdapter;


public class ListFragment extends Fragment implements ListView {

    private static final String TAG = ListFragment.class.getSimpleName();

    @Inject
    ListPresenter listPresenter;

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
        args.putInt(Constants.PAGER_POSITION, position);
        args.putInt(Constants.SELECTED_BUTTON, selectedButton);
        listFragment.setArguments(args);
        return listFragment;
    }

    public static ListFragment newSearchInstance() {
        ListFragment listFragment = new ListFragment();
        /*Bundle args = new Bundle();
        args.putString(SearchManager.QUERY, query);
        listFragment.setArguments(args);*/
        return listFragment;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (savedInstanceState != null){
            pagerPosition = (int) savedInstanceState.get(Constants.PAGER_POSITION);
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
        setHasOptionsMenu(true);

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

        if (getArguments() != null && getArguments().containsKey(Constants.PAGER_POSITION) && getArguments().containsKey(Constants.SELECTED_BUTTON)){
            pagerPosition = (int) getArguments().get(Constants.PAGER_POSITION);
            selectedButton = (int) getArguments().get(Constants.SELECTED_BUTTON);

            if (isNetworkAvailable()&& selectedButton == Constants.BUTTON_MOVIES) {
                listPresenter.setMovieView(this, pagerPosition);
            } else if (isNetworkAvailable() && selectedButton == Constants.BUTTON_TV_SHOWS){
                listPresenter.setTvShowView(this, pagerPosition);
            } else {
                listPresenter.setMovieSearchView(this);
            }
        } /*else if (isNetworkAvailable()){
            listPresenter.setMovieSearchView(this);
        }*/
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        listPresenter.destroy();
    }

    @Override
    public void setUpMovieView() {
        customAdapter = new ListMovieRecyclerAdapter(listPresenter);
        setAnimationAdapter();
    }

    @Override
    public void setUpTvShowView() {
        customAdapter = new ListTvShowRecyclerAdapter(listPresenter);
        setAnimationAdapter();
    }

    @Override
    public void setUpMovieSearchView() {
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
        // setup progress bar
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

    private void setAnimationAdapter(){
        scaleInAnimationAdapter = new ScaleInAnimationAdapter(customAdapter);
        scaleInAnimationAdapter.setDuration(400);
        scaleInAnimationAdapter.setInterpolator(new OvershootInterpolator());
        // disable the first scroll mode
        scaleInAnimationAdapter.setFirstOnly(false);
        scaleInAnimationAdapter.notifyDataSetChanged();
        recyclerView.setAdapter(scaleInAnimationAdapter);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(Constants.PAGER_POSITION,pagerPosition);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        menu.clear();
        inflater.inflate(R.menu.options_menu, menu);
        SearchView searchView = (SearchView) menu.findItem(R.id.search).getActionView();
        SearchManager searchManager = (SearchManager) getActivity().getSystemService(Context.SEARCH_SERVICE);
        searchView.setSearchableInfo(searchManager.getSearchableInfo(getActivity().getComponentName()));

        searchView.setOnCloseListener(new SearchView.OnCloseListener() {
            @Override
            public boolean onClose() {
                if (pagerPosition == 0){
                    listPresenter.showMostPopularMovies();
                } else if (pagerPosition == 1){
                    listPresenter.showHighestRatedMovies();
                } else {
                    listPresenter.showUpcomingMovies();
                }
                return false;
            }
        });

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                listPresenter.showSearchResults(newText);
                return false;
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.search:
                callback.onSearchItemClick();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public interface Callback {
        void onMovieClicked(Movie movie, String singleMovieGenres, int selectedButton);
        void onTvShowClicked(TvShow tvShow, String singleTvShowGenres, int selectedButton);
        void onSearchItemClick();
    }
}
