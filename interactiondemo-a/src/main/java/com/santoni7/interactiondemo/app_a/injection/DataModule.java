package com.santoni7.interactiondemo.app_a.injection;

import android.arch.persistence.room.Room;
import android.content.Context;

import com.santoni7.interactiondemo.app_a.data.LinkRepository;
import com.santoni7.interactiondemo.app_a.data.ImageLinkDatabase;

import dagger.Module;
import dagger.Provides;

@Module(includes = ContextModule.class)
public class DataModule {
    @AppAScope
    @Provides
    ImageLinkDatabase imageLinkDatabase(Context context) {
        return Room.databaseBuilder(context, ImageLinkDatabase.class, "image-database")
                .fallbackToDestructiveMigration()
                .build();
    }

    @AppAScope
    @Provides
    LinkRepository linkRepository(ImageLinkDatabase database) {
        return new LinkRepository(database);
    }

}
