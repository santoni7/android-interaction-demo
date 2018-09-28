package com.santoni7.interactiondemo.app_a.base;

import android.support.v4.app.Fragment;

import com.santoni7.interactiondemo.lib.mvp.MvpPresenter;

public abstract class FragmentBase<P extends MvpPresenter> extends Fragment {
    private P presenter;


    public P getPresenter() {
        return presenter;
    }

    public void setPresenter(P presenter) {
        this.presenter = presenter;
    }
}
