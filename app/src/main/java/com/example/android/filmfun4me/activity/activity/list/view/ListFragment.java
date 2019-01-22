package com.example.android.filmfun4me.activity.activity.list.view;


import android.app.SearchManager;
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
import android.support.v7.widget.SearchView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.OvershootInterpolator;
import android.widget.Toast;

import com.example.android.filmfun4me.BaseApplication;
import com.example.android.filmfun4me.R;
import com.example.android.filmfun4me.activity.activity.list.presenter.ListPresenter;
import com.example.android.filmfun4me.data.Movie;
import com.example.android.filmfun4me.data.TvShow;
import com.example.android.filmfun4me.utils.Constants;

import javax.inject.Inject;

import jp.wasabeef.recyclerview.animators.adapters.ScaleInAnimationAdapter;


public class ListFragment extends Fragment implements ListView {

    private static final String TAG = ListFragment.class.getSimpleName();
    private String savedSearchQuery;

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
        if (savedInstanceState != null){
            if (savedInstanceState.containsKey(Constants.PAGER_POSITION)){
                pagerPosition = (int) savedInstanceState.get(Constants.PAGER_POSITION);
            }
            if (savedInstanceState.containsKey("saved_query")){

                savedSearchQuery = savedInstanceState.getString("saved_query");
            }
            if (savedInstanceState.containsKey(Constants.SELECTED_BUTTON)){
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

        if (isNetworkAvailable() && getArguments() != null){
            if (getArguments().containsKey(Constants.SELECTED_BUTTON)){
                selectedButton = (int) getArguments().get(Constants.SELECTED_BUTTON);
                if (getArguments().containsKey(Constants.PAGER_POSITION) && selectedButton == Constants.BUTTON_MOVIES){
                    pagerPosition = (int) getArguments().get(Constants.PAGER_POSITION);
                    listPresenter.setMovieView(this, pagerPosition);
                } else if(getArguments().containsKey(Constants.PAGER_POSITION) && selectedButton == Constants.BUTTON_TV_SHOWS) {
                    pagerPosition = (int) getArguments().get(Constants.PAGER_POSITION);
                    listPresenter.setTvShowView(this, pagerPosition);
                } else {
                    listPresenter.setSearchView(this);
                }
            }
        } else {
            Toast.makeText(getActivity(), "No network connection! Please check your internet connection", Toast.LENGTH_LONG).show();
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
        setAnimationAdapter();
    }

    @Override
    public void setUpMovieSearchView() {
        if (customAdapter == null){
            customAdapter = new ListMovieRecyclerAdapter(listPresenter);
        }
        if (scaleInAnimationAdapter == null){
            setAnimationAdapter();
        }
        customAdapter.notifyDataSetChanged();
        scaleInAnimationAdapter.notifyDataSetChanged();
    }

    @Override
    public void setUpTvShowView() {
        customAdapter = new ListTvShowRecyclerAdapter(listPresenter);
        setAnimationAdapter();
    }

    @Override
    public void setUpTvSearchView() {
        if (customAdapter == null){
            customAdapter = new ListTvShowRecyclerAdapter(listPresenter);
        }
        if (scaleInAnimationAdapter == null){
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
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        menu.clear();
        inflater.inflate(R.menu.search_menu, menu);
        SearchView searchView = (SearchView) menu.findItem(R.id.search).getActionView();
        SearchManager searchManager = (SearchManager) getActivity().getSystemService(Context.SEARCH_SERVICE);
        searchView.setSearchableInfo(searchManager.getSearchableInfo(getActivity().getComponentName()));

        if (savedSearchQuery != null){
            searchView.setIconified(true);
            searchView.onActionViewExpanded();
            searchView.setQuery(savedSearchQuery, false);
            searchView.setFocusable(true);
        }

        // setOnCloseListener doesn't work so implemented this
        MenuItem menuItem = menu.findItem(R.id.search);
        menuItem.setOnActionExpandListener(new MenuItem.OnActionExpandListener() {
            @Override
            public boolean onMenuItemActionExpand(MenuItem item) {
                return true;
            }

            @Override
            public boolean onMenuItemActionCollapse(MenuItem item) {
                callback.onSearchDialogClosed();
                return true;
            }
        });

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                savedSearchQuery = newText;
                if (!newText.contentEquals("")){
                    if (selectedButton == Constants.BUTTON_MOVIES){
                        listPresenter.showMovieSearchResults(newText);
                    } else {
                        listPresenter.showTvSearchResults(newText);
                    }
                }
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

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(Constants.PAGER_POSITION,pagerPosition);
        outState.putString("saved_query", savedSearchQuery);
    }

    public interface Callback {
        void onMovieClicked(Movie movie, String singleMovieGenres, int selectedButton);
        void onTvShowClicked(TvShow tvShow, String singleTvShowGenres, int selectedButton);
        void onSearchItemClick();
        void onSearchDialogClosed();
    }
}
