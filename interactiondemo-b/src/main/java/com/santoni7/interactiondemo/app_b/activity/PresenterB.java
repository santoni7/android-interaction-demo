package com.santoni7.interactiondemo.app_b.activity;

import android.graphics.Bitmap;
import android.util.Log;

import com.santoni7.interactiondemo.app_b.Constants;
import com.santoni7.interactiondemo.app_b.R;
import com.santoni7.interactiondemo.app_b.data.LinkContentRepository;
import com.santoni7.interactiondemo.app_b.data.RemoteImageDataSource;
import com.santoni7.interactiondemo.lib.model.ImageLink;
import com.santoni7.interactiondemo.lib.mvp.PresenterBase;

import java.util.Calendar;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.Nullable;
import io.reactivex.disposables.CompositeDisposable;

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

        disposables.add(repository.insert(imageLink)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(id -> {
                    imageLink.setLinkId(id);
                    loadAndDisplayImage(imageLink);
                }, err -> {
                    getView().errorMessage(R.string.error_while_inserting);
                }));
    }


    @Override
    public void onActionOpenLink(long id) {
        disposables.add(repository.select(id)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(imageLink -> {
                    // Schedule remove link with "green" status
                    if (imageLink.getStatus() == ImageLink.Status.LOADED) {
                        Log.d("PresenterB", "Scheduled service execution for id=" + id + "; link=" + imageLink);

                        getView().scheduleDeleteLink(imageLink, Constants.SERVICE_EXECUTION_DELAY_MS);
                        getView().scheduleDownloadImage(imageLink, Constants.IMAGE_SAVE_PATH, Constants.SERVICE_EXECUTION_DELAY_MS);
                        getView().snackbarOk("Service scheduled.");
                    }
                    loadAndDisplayImage(imageLink);
                }, err -> {
                    getView().errorMessage(R.string.error_fetching_link_from_db);
                }));
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

        getView().displayData(imageLink, bitmap);

        // todo subscribe and display success/error messages
        disposables.add(repository.update(imageLink)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe((affected, err) -> {
                    if (affected == 0 || err != null){
                        getView().errorMessage(R.string.error_updating_link);
                    }
                }));
    }

    @Override
    public void destroy() {
        disposables.clear();
        super.destroy();
    }
}
