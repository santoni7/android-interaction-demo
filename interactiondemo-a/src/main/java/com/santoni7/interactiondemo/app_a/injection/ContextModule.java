package com.santoni7.interactiondemo.app_a.injection;

import android.content.Context;

import com.santoni7.interactiondemo.app_a.ClipboardExceptionHandler;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module
public class ContextModule {
    private Context context;

    public ContextModule(Context context) {
        this.context = context;
    }

    @Provides
    public Context context() {
        return context.getApplicationContext();
    }

    @Provides
    @Singleton
    public ClipboardExceptionHandler clipboardExceptionHandler() { return new ClipboardExceptionHandler(context); }
}
