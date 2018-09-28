package com.santoni7.interactiondemo.lib.model;


import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.arch.persistence.room.TypeConverters;
import android.content.ContentValues;

import com.santoni7.interactiondemo.lib.converters.DateConverter;
import com.santoni7.interactiondemo.lib.converters.StatusConverter;

import java.util.Comparator;
import java.util.Date;
import java.util.Locale;

@Entity(tableName = ImageLink.TABLE_NAME)
public class ImageLink {
    /**
     * Database info:
     */
    public static final String TABLE_NAME = "Links";
    public static final String COLUMN_ID = "linkId";
    public static final String COLUMN_URL = "url";
    public static final String COLUMN_STATUS = "status";
    public static final String COLUMN_TIMESTAMP = "timestamp";



    public enum Status {
        LOADED(1), ERROR(2), UNKNOWN(3);

        private int code;
        Status(int code) {
            this.code = code;
        }
        public int getCode() {
            return code;
        }
    }

    /**
     * Fiel
     */
    @PrimaryKey(autoGenerate = true)
    private long linkId;

    private String url;

    @TypeConverters(DateConverter.class)
    private Date timestamp;

    @TypeConverters(StatusConverter.class)
    private Status status;



    @Override
    public String toString() {
        return String.format(Locale.getDefault(), "Link#%d: {url: '%s', status: '%s', timestamp: '%s'}",
                linkId, url, status.toString(), timestamp.toString());
    }



    public static ImageLink fromContentValues(ContentValues values) {
        final ImageLink link = new ImageLink();
        if (values.containsKey(COLUMN_ID)) {
            link.setLinkId(values.getAsLong(COLUMN_ID));
        }
        if (values.containsKey(COLUMN_URL)) {
            link.setUrl(values.getAsString(COLUMN_URL));
        }
        if (values.containsKey(COLUMN_STATUS)) {
            link.setStatus(StatusConverter.intToStatus(values.getAsInteger(COLUMN_STATUS)));
        }
        if (values.containsKey(COLUMN_TIMESTAMP)) {
            link.setTimestamp(DateConverter.fromTimestamp(values.getAsLong(COLUMN_TIMESTAMP)));
        }
        return link;
    }

    public static ContentValues toContentValues(ImageLink link){
        final ContentValues contentValues = new ContentValues();
        contentValues.put(COLUMN_ID, link.getLinkId());
        contentValues.put(COLUMN_STATUS, StatusConverter.fromStatus(link.getStatus()));
        contentValues.put(COLUMN_TIMESTAMP, DateConverter.dateToTimestamp(link.getTimestamp()));
        contentValues.put(COLUMN_URL, link.getUrl());
        return contentValues;
    }

    public long getLinkId() {
        return linkId;
    }

    public void setLinkId(long linkId) {
        this.linkId = linkId;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public Date getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Date timestamp) {
        this.timestamp = timestamp;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }


}
