package com.santoni7.interactiondemo.lib.mvp;

import io.reactivex.disposables.CompositeDisposable;

public abstract class PresenterBase<V extends MvpView> implements MvpPresenter<V>{
    private V view;
    protected CompositeDisposable disposables = new CompositeDisposable();

    @Override
    public void attachView(V view) {
        this.view = view;
    }

    @Override
    public void detachView() {
        this.view = null;
    }

    @Override
    public void destroy() {
        detachView();
        disposables.dispose();
    }

    public V getView(){
        return view;
    }

    public boolean isViewAttached(){
        return view != null;
    }
}
