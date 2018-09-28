package com.santoni7.interactiondemo.app_a.activity;

import android.arch.lifecycle.LifecycleObserver;
import android.arch.lifecycle.LifecycleOwner;
import android.content.Intent;

import com.santoni7.interactiondemo.app_a.ImageLinkOrder;
import com.santoni7.interactiondemo.lib.mvp.MvpPresenter;
import com.santoni7.interactiondemo.lib.mvp.MvpView;
import com.santoni7.interactiondemo.lib.model.ImageLink;
import com.santoni7.interactiondemo.app_a.data.ImageLinkDatabase;

import java.util.List;

public interface ContractA {
    interface View extends MvpView, LifecycleOwner {

        HistoryView getHistoryView();

        TestView getTestView();

        ImageLinkDatabase getDatabase();

        void toast(String msg);

        void startActivity(Intent i);

        void showMenuItemOrderBy();

        void hideMenuItemOrderBy();

        interface HistoryView {
            void setLinks(List<ImageLink> links);

            void setPresenter(Presenter presenter);
        }

        interface TestView {
            void setPresenter(Presenter presenter);
        }
    }

    interface Presenter extends MvpPresenter<View>, LifecycleObserver {

        void onTestOkButtonClicked(String textInput);

        void onHistoryItemClicked(ImageLink item);

        void onPageSelected(PagerState pagerState);

//        void setSortingComparator(Comparator<ImageLink> imageLinkComparator);

        void updateDataOrdered(ImageLinkOrder order);
    }
}
