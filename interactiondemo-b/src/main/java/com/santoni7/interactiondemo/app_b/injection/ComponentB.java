package com.santoni7.interactiondemo.app_b.injection;

import com.santoni7.interactiondemo.app_b.activity.ActivityB;

import dagger.Component;

@AppBScope
@Component(modules = {PresenterModule.class})
public interface ComponentB {
    void inject(ActivityB activity);
}
