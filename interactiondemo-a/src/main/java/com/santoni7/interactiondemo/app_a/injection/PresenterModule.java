package com.santoni7.interactiondemo.app_a.injection;

import com.santoni7.interactiondemo.app_a.data.LinkRepository;
import com.santoni7.interactiondemo.app_a.activity.ContractA;
import com.santoni7.interactiondemo.app_a.activity.PresenterA;

import dagger.Module;
import dagger.Provides;

@Module(includes = DataModule.class)
public class PresenterModule {
    @AppAScope
    @Provides
    ContractA.Presenter presenter(LinkRepository repository) {
        return new PresenterA(repository);
    }
}
