package com.example.android.filmfun4me.activity.activity.detail.view;

import android.support.v7.widget.RecyclerView;
import android.text.SpannableString;
import android.text.style.UnderlineSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.android.filmfun4me.R;
import com.example.android.filmfun4me.activity.activity.detail.presenter.DetailPresenter;

public class ListSeasonButtonRecyclerAdapter extends RecyclerView.Adapter<ListSeasonButtonRecyclerAdapter.ViewHolder> {

    private DetailPresenter detailPresenter;
    private String tvShowId;

    ListSeasonButtonRecyclerAdapter(DetailPresenter detailPresenter, String tvShowId){
        this.detailPresenter = detailPresenter;
        this.tvShowId = tvShowId;
    }

    @Override
    public ListSeasonButtonRecyclerAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.season_list_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ListSeasonButtonRecyclerAdapter.ViewHolder holder, int position) {
        detailPresenter.onBindSeasonListItemOnPosition(position, holder);
    }

    @Override
    public int getItemCount() {
        return detailPresenter.getSeasonListItemRowsCount();
    }

    class ViewHolder extends RecyclerView.ViewHolder implements DetailSeasonItemView, View.OnClickListener {

        Button seasonButton;

        ViewHolder(View itemView) {
            super(itemView);
            seasonButton = itemView.findViewById(R.id.season_button);
            seasonButton.setOnClickListener(this);
        }

        @Override
        public void setSeasonButtonNumber(String seasonButtonNumber) {
            SpannableString content = new SpannableString(seasonButtonNumber);
            content.setSpan(new UnderlineSpan(), 0, content.length(), 0);
            seasonButton.setText(content);
        }

        @Override
        public void onClick(View v) {
            detailPresenter.onSeasonListItemInteraction(tvShowId, getAdapterPosition());
            seasonButton.setSelected(true);
        }
    }
}
