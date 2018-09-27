package com.santoni7.interactiondemo.lib.converters;

import android.arch.persistence.room.TypeConverter;

import com.santoni7.interactiondemo.lib.model.ImageLink;

public class StatusConverter {

    @TypeConverter
    public static int fromStatus(ImageLink.Status status) {
        return status.getCode();
    }

    @TypeConverter
    public static ImageLink.Status intToStatus(int code) {
        if (code == ImageLink.Status.LOADED.getCode()) {
            return ImageLink.Status.LOADED;
        } else if (code == ImageLink.Status.ERROR.getCode()) {
            return ImageLink.Status.ERROR;
        } else if (code == ImageLink.Status.UNKNOWN.getCode()) {
            return ImageLink.Status.UNKNOWN;
        } else {
            throw new IllegalArgumentException("Unknown image status code");
        }
    }
}
