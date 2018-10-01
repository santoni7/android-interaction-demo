package com.santoni7.interactiondemo.app_a.injection;

import com.santoni7.interactiondemo.app_a.ApplicationA;
import com.santoni7.interactiondemo.app_a.activity.ActivityA;
import com.santoni7.interactiondemo.app_a.activity.PresenterA;
import com.santoni7.interactiondemo.app_a.data.ImageLinkDatabase;
import com.santoni7.interactiondemo.app_a.fragment.HistoryFragment;
import com.santoni7.interactiondemo.app_a.fragment.TestFragment;

import dagger.Component;

@AppAScope
@Component(modules = {ContextModule.class, PresenterModule.class})
public interface ComponentA {

    void inject(ActivityA activity);

    void inject(PresenterA presenterA);

    void injectFragment(HistoryFragment fragment);

    void injectFragment(TestFragment fragment);
//
//
    ImageLinkDatabase provideDatabase();
//
//    LinkRepository provideRepository();
//
//    ContractA.Presenter providePresenter();
//
//    ClipboardExceptionHandler provideExceptionHandler();
}
