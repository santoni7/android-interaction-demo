package com.santoni7.interactiondemo.app_a.fragment;


import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.santoni7.interactiondemo.app_a.adapter.HistoryAdapter;
import com.santoni7.interactiondemo.app_a.R;
import com.santoni7.interactiondemo.app_a.activity.ContractA;
import com.santoni7.interactiondemo.app_a.base.FragmentBase;
import com.santoni7.interactiondemo.lib.model.ImageLink;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;


public class HistoryFragment extends FragmentBase<ContractA.Presenter> implements ContractA.View.HistoryView, HistoryAdapter.OnItemClickedListener {
    private static final String TAG = HistoryFragment.class.getSimpleName();

    @BindView(R.id.recyclerView) RecyclerView recyclerView;

    private HistoryAdapter adapter = new HistoryAdapter(new ArrayList<>(), this);

    public HistoryFragment() {
        // Required empty public constructor
    }

    public static HistoryFragment newInstance() {
        return new HistoryFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
//        if(container == null)
//        {
//            getFragmentManager().beginTransaction().remove(this).commit();
//            return null;
//        }
        View v = inflater.inflate(R.layout.fragment_history, container, false);
        ButterKnife.bind(this, v);

        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(adapter);

        return v;
    }

    @Override
    public void onClick(ImageLink item) {
        getPresenter().onHistoryItemClicked(item);
    }

    @Override
    public void setLinks(List<ImageLink> links) {
        adapter.setLinks(links);
    }

    @Override
    public void setPresenter(ContractA.Presenter presenter) {
        Log.d(TAG, "setPresenter(..): presenter=" + presenter);
        super.setPresenter(presenter);
    }
}
