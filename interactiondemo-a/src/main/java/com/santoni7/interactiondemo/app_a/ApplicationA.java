package com.santoni7.interactiondemo.app_a;

import android.app.Application;

import com.santoni7.interactiondemo.app_a.injection.ComponentA;
import com.santoni7.interactiondemo.app_a.injection.ContextModule;
import com.santoni7.interactiondemo.app_a.injection.DaggerComponentA;
import com.santoni7.interactiondemo.app_a.injection.DataModule;
import com.santoni7.interactiondemo.app_a.injection.PresenterModule;

public class ApplicationA extends Application {

    private static ComponentA component;

    @Override
    public void onCreate() {
        super.onCreate();
        component = DaggerComponentA.builder()
                .contextModule(new ContextModule(getApplicationContext()))
                .dataModule(new DataModule())
                .presenterModule(new PresenterModule())
                .build();
    }


    public static ComponentA getComponent() {
        return component;
    }
}
