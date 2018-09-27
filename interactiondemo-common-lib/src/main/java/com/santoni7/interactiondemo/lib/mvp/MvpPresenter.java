package com.santoni7.interactiondemo.lib.mvp;

public interface MvpPresenter<V extends MvpView> {
    void attachView(V view);
    void viewReady();
    void detachView();
    void destroy();
}
