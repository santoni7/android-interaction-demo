package com.santoni7.interactiondemo.lib.mvp;

public interface MvpView {
    <P extends MvpPresenter> P getPresenter();
}
