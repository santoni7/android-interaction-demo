package com.santoni7.interactiondemo.app_a.adapter;

import android.support.annotation.NonNull;
import android.support.v7.util.DiffUtil;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.santoni7.interactiondemo.app_a.R;
import com.santoni7.interactiondemo.lib.model.ImageLink;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.Single;
import io.reactivex.SingleObserver;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class HistoryAdapter extends RecyclerView.Adapter<HistoryAdapter.ViewHolder> {
    private List<ImageLink> links;
    private OnItemClickedListener listener;

    public HistoryAdapter(List<ImageLink> links, OnItemClickedListener listener) {
        this.links = links;
        this.listener = listener;
    }

    public void setLinks(List<ImageLink> links) {
        this.links = links;
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View itemView = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.history_item, viewGroup, false);
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
        viewHolder.setData(links.get(i), listener);
    }

    @Override
    public int getItemCount() {
        return links.size();
    }


    static class ViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.cardView) CardView card;
        @BindView(R.id.txtId) TextView id;
        @BindView(R.id.txtLinkUrl) TextView url;
        @BindView(R.id.txtDateUpdated) TextView dateUpdated;

        ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        void setData(final ImageLink link, final OnItemClickedListener listener) {
            card.setOnClickListener(__ -> listener.onClick(link));

            id.setText(String.valueOf(link.getLinkId()));
            url.setText(link.getUrl());
            dateUpdated.setText(link.getTimestamp().toString());
            int color;
            switch (link.getStatus()) {
                default:
                case UNKNOWN:
                    color = R.color.background_grey;
                    break;
                case LOADED:
                    color = R.color.background_green;
                    break;
                case ERROR:
                    color = R.color.background_red;
            }
            card.setCardBackgroundColor(card.getContext().getResources().getColor(color));
        }
    }

    public interface OnItemClickedListener {
        void onClick(ImageLink item);
    }
}
