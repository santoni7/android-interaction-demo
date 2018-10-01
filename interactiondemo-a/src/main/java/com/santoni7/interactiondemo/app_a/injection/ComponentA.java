package com.santoni7.interactiondemo.app_a.injection;

import com.santoni7.interactiondemo.app_a.ClipboardExceptionHandler;
import com.santoni7.interactiondemo.app_a.data.LinkRepository;
import com.santoni7.interactiondemo.app_a.activity.ContractA;
import com.santoni7.interactiondemo.app_a.data.ImageLinkDatabase;

import javax.inject.Singleton;

import dagger.Component;

@Singleton
@Component(modules = {PresenterModule.class})
public interface ComponentA {

    ImageLinkDatabase provideDatabase();

    LinkRepository provideRepository();

    ContractA.Presenter providePresenter();

    ClipboardExceptionHandler provideExceptionHandler();
}
