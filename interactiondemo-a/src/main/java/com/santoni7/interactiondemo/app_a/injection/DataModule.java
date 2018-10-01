package com.santoni7.interactiondemo.app_a.injection;

import android.arch.persistence.room.Room;
import android.content.Context;

import com.santoni7.interactiondemo.app_a.data.LinkRepository;
import com.santoni7.interactiondemo.app_a.data.ImageLinkDatabase;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module(includes = ContextModule.class)
public class DataModule {
    @Provides
    @Singleton
    ImageLinkDatabase imageLinkDatabase(Context context) {
        return Room.databaseBuilder(context, ImageLinkDatabase.class, "image-database")
                .fallbackToDestructiveMigration()
                .build();
    }

    @Provides
    @Singleton
    LinkRepository linkRepository(ImageLinkDatabase database) {
        return new LinkRepository(database);
    }

}
