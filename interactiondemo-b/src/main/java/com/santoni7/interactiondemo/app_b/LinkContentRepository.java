package com.santoni7.interactiondemo.app_b;

import android.content.ContentUris;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;

import com.santoni7.interactiondemo.lib.CommonConst;
import com.santoni7.interactiondemo.lib.converters.DateConverter;
import com.santoni7.interactiondemo.lib.converters.StatusConverter;
import com.santoni7.interactiondemo.lib.model.ImageLink;

public class LinkContentRepository {
    private Context ctx;

    public LinkContentRepository(Context ctx){
        this.ctx = ctx;
    }



    public ImageLink select(long id){
        ImageLink link = null;

        Uri uri = ContentUris.withAppendedId(CommonConst.CONTENT_URI, id);

        Cursor cursor = ctx.getContentResolver().query(uri, null, null, null, null);
        if(cursor != null){
            if(cursor.moveToFirst()){
                int colId = cursor.getColumnIndex(ImageLink.COLUMN_ID);
                int colUrl = cursor.getColumnIndex(ImageLink.COLUMN_URL);
                int colStatus = cursor.getColumnIndex(ImageLink.COLUMN_STATUS);
                int colTimestamp = cursor.getColumnIndex(ImageLink.COLUMN_TIMESTAMP);

                link = new ImageLink();
                link.setLinkId(cursor.getLong(colId));
                link.setStatus(StatusConverter.intToStatus(cursor.getInt(colStatus)));
                link.setTimestamp(DateConverter.fromTimestamp(cursor.getLong(colTimestamp)));
                link.setUrl(cursor.getString(colUrl));
            }
            cursor.close();
        }
        return link;
    }

    public long insert(ImageLink link){
        Uri itemUri = ctx.getContentResolver().insert(CommonConst.CONTENT_URI, ImageLink.toContentValues(link));
        long id = ContentUris.parseId(itemUri);
        return id;
    }

    public int update(ImageLink link) {
        return ctx.getContentResolver().update(ContentUris.withAppendedId(CommonConst.CONTENT_URI, link.getLinkId()),
                ImageLink.toContentValues(link), null, null);
    }
}
