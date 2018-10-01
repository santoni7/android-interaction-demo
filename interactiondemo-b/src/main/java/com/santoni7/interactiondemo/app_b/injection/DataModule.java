package com.santoni7.interactiondemo.app_b.injection;

import android.content.Context;

import com.santoni7.interactiondemo.app_b.data.BitmapSource;
import com.santoni7.interactiondemo.app_b.data.LinkContentRepository;
import com.santoni7.interactiondemo.app_b.data.RemoteBitmapSource;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module(includes = ContextModule.class)
public class DataModule {
    @AppBScope
    @Provides
    LinkContentRepository linkRepository(Context context){
        return new LinkContentRepository(context);
    }

    @AppBScope
    @Provides
    BitmapSource bitmapSource(){
        return new RemoteBitmapSource();
    }
}
