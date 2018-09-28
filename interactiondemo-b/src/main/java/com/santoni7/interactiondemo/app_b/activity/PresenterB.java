package com.santoni7.interactiondemo.app_b.activity;

import android.graphics.Bitmap;

import com.santoni7.interactiondemo.app_b.LinkContentRepository;
import com.santoni7.interactiondemo.app_b.RemoteImageDataSource;
import com.santoni7.interactiondemo.lib.model.ImageLink;
import com.santoni7.interactiondemo.lib.mvp.PresenterBase;

import java.util.Calendar;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.Nullable;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;

public class PresenterB extends PresenterBase<ContractB.View> implements ContractB.Presenter {
    private LinkContentRepository repository;
    private RemoteImageDataSource imageDataSource;

    private CompositeDisposable disposables = new CompositeDisposable();

    public PresenterB(LinkContentRepository contentRepository) {
        this.repository = contentRepository;
        this.imageDataSource = new RemoteImageDataSource();
    }

    @Override
    public void viewReady() {
        getView().showProgressBar();
    }

    @Override
    public void onActionNewLink(String url) {
        final ImageLink imageLink = new ImageLink();
        imageLink.setTimestamp(Calendar.getInstance().getTime());
        imageLink.setUrl(url);
        imageLink.setStatus(ImageLink.Status.UNKNOWN);

        long id = repository.insert(imageLink);
        imageLink.setLinkId(id);

        loadAndDisplayImage(imageLink);
    }


    @Override
    public void onActionOpenLink(long id) {
        final ImageLink imageLink = repository.select(id);
        if (imageLink.getStatus() == ImageLink.Status.LOADED) {
            // todo schedule image download and imagelink delete from DB

        }
        loadAndDisplayImage(imageLink);
    }

    private void loadAndDisplayImage(ImageLink imageLink) {
        disposables.add(imageDataSource.downloadImage(imageLink.getUrl())
                .observeOn(AndroidSchedulers.mainThread())
                .doFinally(getView()::hideProgressBar)
                .subscribe(img -> this.imageReceived(imageLink, img),
                        err -> this.imageReceived(imageLink, null)));
    }

    private void imageReceived(ImageLink imageLink, @Nullable Bitmap bitmap) {
        if (bitmap != null) {
            imageLink.setStatus(ImageLink.Status.LOADED);
        } else {
            imageLink.setStatus(ImageLink.Status.ERROR);
        }

        repository.update(imageLink);
        getView().displayData(imageLink, bitmap);
    }

    @Override
    public void destroy() {
        super.destroy();
        disposables.clear();
    }
}
