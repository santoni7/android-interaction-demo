package com.santoni7.interactiondemo.app_b.activity;

import android.graphics.Bitmap;
import android.util.Log;

import com.santoni7.interactiondemo.app_b.Constants;
import com.santoni7.interactiondemo.app_b.data.BitmapSource;
import com.santoni7.interactiondemo.app_b.data.LinkContentRepository;
import com.santoni7.interactiondemo.lib.model.ImageLink;
import com.santoni7.interactiondemo.lib.mvp.PresenterBase;

import java.io.FileNotFoundException;
import java.net.ConnectException;
import java.net.UnknownHostException;
import java.util.Calendar;

import javax.inject.Inject;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.Nullable;
import io.reactivex.disposables.CompositeDisposable;

public class PresenterB extends PresenterBase<ContractB.View> implements ContractB.Presenter {

    private static final String TAG = PresenterB.class.getSimpleName();
    private LinkContentRepository repository;
    private BitmapSource imageDataSource;

    private CompositeDisposable disposables = new CompositeDisposable();


    @Inject
    public PresenterB(LinkContentRepository contentRepository, BitmapSource bitmapSource) {
        this.repository = contentRepository;
        this.imageDataSource = bitmapSource;
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
                    getView().showMessage(ContractB.Message.ERR_INSERTING_LINK);
                }));
    }


    @Override
    public void onActionOpenLink(long id) {
        disposables.add(repository.select(id)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(imageLink -> {
                    // Schedule remove link with "green" status
                    if (imageLink.getStatus() == ImageLink.Status.LOADED) {
                        Log.d(TAG, "Scheduled service execution for id=" + id + "; link=" + imageLink);

                        getView().scheduleDeleteLink(imageLink, Constants.SERVICE_EXECUTION_DELAY_MS);
                        getView().scheduleDownloadImage(imageLink, Constants.IMAGE_SAVE_PATH, Constants.SERVICE_EXECUTION_DELAY_MS);
                        getView().showMessage(ContractB.Message.SERVICE_SCHEDULED);
                    }
                    loadAndDisplayImage(imageLink);
                }, err -> {
                    Log.e(TAG, "Error fetching link from database");
                    err.printStackTrace();
                    getView().showMessage(ContractB.Message.ERR_FETCHING_LINK);
                }));
    }

    private void loadAndDisplayImage(ImageLink imageLink) {
        disposables.add(imageDataSource.downloadImage(imageLink.getUrl())
                .observeOn(AndroidSchedulers.mainThread())
                .doFinally(getView()::hideProgressBar)
                .subscribe(img -> this.imageReceived(imageLink, img),
                        err -> {
                            if(err instanceof FileNotFoundException){
                                getView().showMessage(ContractB.Message.ERR_FILE_NOT_FOUND);
                            } else if(err instanceof UnknownHostException) {
                                getView().showMessage(ContractB.Message.ERR_UNKNOWN_HOST);
                            } else if(err instanceof NullPointerException) {
                                getView().showMessage(ContractB.Message.ERR_NULL_RESPONSE);
                            } else if(err instanceof ConnectException) {
                                getView().showMessage(ContractB.Message.ERR_CONNECTION_REFUSED);
                            }
                            Log.d(TAG, "Error downloading image: " + err);
                            err.printStackTrace();
                            this.imageReceived(imageLink, null);
                        }));
    }

    private void update(ImageLink imageLink) {
        disposables.add(repository.update(imageLink)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe((affected, err) -> {
                    if (affected == 0 || err != null) {
                        getView().showMessage(ContractB.Message.ERR_UPDATING_LINK);
                    }
                }));
    }

    private void imageReceived(ImageLink imageLink, @Nullable Bitmap bitmap) {
        if (bitmap != null) {
            imageLink.setStatus(ImageLink.Status.LOADED);
        } else {
            imageLink.setStatus(ImageLink.Status.ERROR);
        }

        getView().displayData(imageLink, bitmap);

        update(imageLink);
    }

    @Override
    public void destroy() {
        disposables.clear();
        super.destroy();
    }
}
