package com.santoni7.interactiondemo.app_a.activity;

import android.arch.lifecycle.Lifecycle;
import android.arch.lifecycle.OnLifecycleEvent;
import android.content.Intent;
import android.util.Log;

import com.santoni7.interactiondemo.app_a.LinkRepository;
import com.santoni7.interactiondemo.lib.CommonConst;
import com.santoni7.interactiondemo.lib.mvp.PresenterBase;
import com.santoni7.interactiondemo.lib.model.ImageLink;

import java.util.Calendar;
import java.util.List;
import java.util.Random;

import io.reactivex.android.schedulers.AndroidSchedulers;

public class MainPresenter extends PresenterBase<MainContract.View> implements MainContract.Presenter {
    private static final String TAG = MainPresenter.class.getSimpleName();

    LinkRepository repository;


    @Override
    public void viewReady() {
        repository = new LinkRepository(getView().getDatabase());
        disposables.add(
                repository.getLinksSorted(LinkRepository.COMPARATOR_BY_STATUS)
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(this::dataUpdated));
    }

    private void dataUpdated(List<ImageLink> imageLinks) {
        getView().getHistoryView().setLinks(imageLinks);
    }

    @Override
    public void onTestOkButtonClicked(String textInput) {
        Intent i = new Intent(CommonConst.ACTION_NEW_LINK);
        i.putExtra(CommonConst.EXTRA_LINK_URL, textInput);
        getView().startActivity(i);
//        ImageLink newLink = new ImageLink();
//        newLink.setUrl(textInput);
//        newLink.setStatus(ImageLink.Status.values()[new Random().nextInt(3)]);
//        newLink.setTimestamp(Calendar.getInstance().getTime());
//        Log.d(TAG, "Created new ImageLink: " + newLink.toString());
//        repository.insertAsync(newLink);
    }

    @Override
    public void onHistoryItemClicked(ImageLink item) {
        Intent i = new Intent(CommonConst.ACTION_OPEN_LINK);
        i.putExtra(CommonConst.EXTRA_LINK_ID, item.getLinkId());
        getView().startActivity(i);
    }

    //    @Override
//    public void clearAll() {
//
//        repository.clearAll();
//    }

    @OnLifecycleEvent(Lifecycle.Event.ON_RESUME)
    public void onResume() {

    }

    @OnLifecycleEvent(Lifecycle.Event.ON_PAUSE)
    public void onPause() {

    }
}
