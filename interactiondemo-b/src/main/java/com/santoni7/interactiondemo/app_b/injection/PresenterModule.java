package com.santoni7.interactiondemo.app_b.injection;

import com.santoni7.interactiondemo.app_b.activity.ContractB;
import com.santoni7.interactiondemo.app_b.activity.PresenterB;
import com.santoni7.interactiondemo.app_b.data.LinkContentRepository;
import com.santoni7.interactiondemo.app_b.data.BitmapSource;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module(includes = DataModule.class)
public class PresenterModule {
    @Provides
    @Singleton
    ContractB.Presenter presenter(LinkContentRepository repository, BitmapSource bitmapSource){
        return new PresenterB(repository, bitmapSource);
    }
}
