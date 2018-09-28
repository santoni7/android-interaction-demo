package com.santoni7.interactiondemo.app_a.activity;

import android.content.Intent;

import com.santoni7.interactiondemo.app_a.LinkRepository;
import com.santoni7.interactiondemo.lib.CommonConst;
import com.santoni7.interactiondemo.app_a.ImageLinkOrder;
import com.santoni7.interactiondemo.lib.mvp.PresenterBase;
import com.santoni7.interactiondemo.lib.model.ImageLink;

import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;

public class PresenterA extends PresenterBase<ContractA.View> implements ContractA.Presenter {
    private static final String TAG = PresenterA.class.getSimpleName();

    private LinkRepository repository;

    @Override
    public void viewReady() {
        repository = new LinkRepository(getView().getDatabase());
    }

    @Override
    public void updateDataOrdered(ImageLinkOrder order) {
        disposables.add(
                repository.getSortedLinks(order.getComparator())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(this::onDataUpdated));
    }

    private void onDataUpdated(List<ImageLink> imageLinks) {
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

    @Override
    public void onPageSelected(PagerState pagerState) {
        if(pagerState == PagerState.HISTORY){
            getView().showMenuItemOrderBy();
        } else {
            getView().hideMenuItemOrderBy();
        }
    }
}
