package com.santoni7.interactiondemo.app_a.activity;

import android.content.Intent;
import android.util.Log;

import com.santoni7.interactiondemo.app_a.data.LinkRepository;
import com.santoni7.interactiondemo.app_a.data.ImageLinkDatabase;
import com.santoni7.interactiondemo.lib.CommonConst;
import com.santoni7.interactiondemo.app_a.data.LinkSortingOrderEnum;
import com.santoni7.interactiondemo.lib.mvp.PresenterBase;
import com.santoni7.interactiondemo.lib.model.ImageLink;

import java.util.List;


import javax.inject.Inject;
import javax.inject.Singleton;

import io.reactivex.android.schedulers.AndroidSchedulers;

@Singleton
public class PresenterA extends PresenterBase<ContractA.View> implements ContractA.Presenter {
    private static final String TAG = PresenterA.class.getSimpleName();

    private LinkRepository repository;

    @Inject
    public PresenterA(LinkRepository repository){
        this.repository = repository;
    }

    @Override
    public void viewReady() {

    }

    @Override
    public void updateDataOrdered(LinkSortingOrderEnum order) {
        disposables.add(
                repository.getSortedLinks(order.getComparator())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(this::onDataUpdated));
    }

    private void onDataUpdated(List<ImageLink> imageLinks) {
        Log.d(TAG, "Data updated: " + imageLinks);
        getView().getHistoryView().setLinks(imageLinks);
    }

    @Override
    public void onTestOkButtonClicked(String textInput) {
        Intent i = new Intent(CommonConst.ACTION_NEW_LINK);
        i.putExtra(CommonConst.EXTRA_LINK_URL, textInput);
        getView().startActivity(i);
    }

    @Override
    public void onHistoryItemClicked(ImageLink item) {
        Intent i = new Intent(CommonConst.ACTION_OPEN_LINK);
        i.putExtra(CommonConst.EXTRA_LINK_ID, item.getLinkId());
        getView().startActivity(i);
    }

}
