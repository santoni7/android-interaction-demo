package com.santoni7.interactiondemo.app_a.data;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;

import com.santoni7.interactiondemo.lib.model.ImageLink;

@Database(entities = {ImageLink.class}, version = 2, exportSchema = false)
public abstract class ImageLinkDatabase extends RoomDatabase{

    public abstract ImageLinkDao getImageLinkDao();

}
