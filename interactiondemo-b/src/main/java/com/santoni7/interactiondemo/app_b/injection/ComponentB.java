package com.santoni7.interactiondemo.app_b.injection;

import com.santoni7.interactiondemo.app_b.activity.ContractB;

import javax.inject.Singleton;

import dagger.Component;

@Singleton
@Component(modules = {PresenterModule.class})
public interface ComponentB {
    ContractB.Presenter providePresenter();
}
