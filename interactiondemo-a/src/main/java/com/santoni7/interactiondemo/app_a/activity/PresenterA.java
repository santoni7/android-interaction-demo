package com.santoni7.interactiondemo.app_a.activity;

import android.content.Intent;
import android.support.v4.util.Pair;
import android.support.v7.util.DiffUtil;
import android.util.Log;

import com.santoni7.interactiondemo.app_a.data.LinkRepository;
import com.santoni7.interactiondemo.lib.CommonConst;
import com.santoni7.interactiondemo.app_a.data.LinkSortingOrderEnum;
import com.santoni7.interactiondemo.lib.mvp.PresenterBase;
import com.santoni7.interactiondemo.lib.model.ImageLink;

import java.util.ArrayList;
import java.util.List;


import javax.inject.Inject;
import javax.inject.Singleton;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

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
        Pair<List<ImageLink>, DiffUtil.DiffResult> initPair = Pair.create(new ArrayList<>(), null);
        disposables.add(
                repository.getSortedLinks(order.getComparator())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(this::onDataUpdated, err -> {
                            Log.e(TAG, "Error updating data: " + err.getMessage());
                            err.printStackTrace();
//                            getView().showMessage(err);
                        }));
    }

    private void onDataUpdated(List<ImageLink> imageLinks) {
        Log.d(TAG, "Data updated: size()=" + imageLinks.size());
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


    private static class ImageLinkDiffCallback extends DiffUtil.Callback {
        private List<ImageLink> current;
        private List<ImageLink> next;

        public ImageLinkDiffCallback(List<ImageLink> current, List<ImageLink> next) {
            this.current = current;
            this.next = next;
        }

        @Override
        public int getOldListSize() {
            return current.size();
        }

        @Override
        public int getNewListSize() {
            return next.size();
        }

        @Override
        public boolean areItemsTheSame(int oldItemPosition, int newItemPosition) {
            ImageLink currentItem = current.get(oldItemPosition);
            ImageLink nextItem = next.get(newItemPosition);
            return currentItem.getLinkId() == nextItem.getLinkId();
        }

        @Override
        public boolean areContentsTheSame(int oldItemPosition, int newItemPosition) {
            ImageLink currentItem = current.get(oldItemPosition);
            ImageLink nextItem = next.get(newItemPosition);
            return currentItem.equals(nextItem);
        }
    }
}
