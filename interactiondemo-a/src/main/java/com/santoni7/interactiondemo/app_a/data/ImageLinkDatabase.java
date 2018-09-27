package com.santoni7.interactiondemo.app_a.data;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;

import com.santoni7.interactiondemo.lib.model.ImageLink;

@Database(entities = {ImageLink.class}, version = 2, exportSchema = false)
public abstract class ImageLinkDatabase extends RoomDatabase{

    private static ImageLinkDatabase INSTANCE;

    public static ImageLinkDatabase getDatabase(Context ctx){
        if(INSTANCE == null){
            INSTANCE = Room
                    .databaseBuilder(ctx, ImageLinkDatabase.class, "image-database")
                    .fallbackToDestructiveMigration()
                    .build();
        }
        return INSTANCE;
    }

    public abstract ImageLinkDao getImageLinkDao();

}
