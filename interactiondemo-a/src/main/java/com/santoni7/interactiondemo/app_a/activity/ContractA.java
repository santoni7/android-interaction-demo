package com.santoni7.interactiondemo.app_a.activity;

import android.content.Intent;

import com.santoni7.interactiondemo.app_a.data.LinkSortingOrderEnum;
import com.santoni7.interactiondemo.lib.model.ImageLink;
import com.santoni7.interactiondemo.lib.mvp.MvpPresenter;
import com.santoni7.interactiondemo.lib.mvp.MvpView;

import java.util.List;

public interface ContractA {
    interface View extends MvpView {

        HistoryView getHistoryView();

        void startActivity(Intent i);

        interface HistoryView {
            void setLinks(List<ImageLink> links);
        }
        interface TestView {}
    }

    interface Presenter extends MvpPresenter<View> {

        void onTestOkButtonClicked(String textInput);

        void onHistoryItemClicked(ImageLink item);

        void updateDataOrdered(LinkSortingOrderEnum order);
    }
}
