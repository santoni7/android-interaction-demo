package com.santoni7.interactiondemo.app_b.activity;

import android.graphics.Bitmap;

import com.santoni7.interactiondemo.lib.model.ImageLink;
import com.santoni7.interactiondemo.lib.mvp.MvpPresenter;
import com.santoni7.interactiondemo.lib.mvp.MvpView;

import io.reactivex.annotations.Nullable;

public class ContractB {
    public interface View extends MvpView{
        void showProgressBar();
        void hideProgressBar();

        void displayData(ImageLink imageLink, @Nullable Bitmap image);
    }

    public interface Presenter extends MvpPresenter<View>{

        void onActionNewLink(String url);

        void onActionOpenLink(long id);
    }
}
