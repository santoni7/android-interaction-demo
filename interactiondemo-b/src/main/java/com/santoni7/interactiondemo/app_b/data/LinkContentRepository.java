package com.santoni7.interactiondemo.app_b.data;

import android.content.ContentUris;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;

import com.santoni7.interactiondemo.lib.CommonConst;
import com.santoni7.interactiondemo.lib.converters.DateConverter;
import com.santoni7.interactiondemo.lib.converters.StatusConverter;
import com.santoni7.interactiondemo.lib.model.ImageLink;

import javax.inject.Inject;

import io.reactivex.Single;
import io.reactivex.schedulers.Schedulers;

public class LinkContentRepository {
    private Context ctx;

    @Inject
    public LinkContentRepository(Context ctx) {
        this.ctx = ctx;
    }


    /* Synchronous methods */

    public ImageLink selectSync(long id) {
        ImageLink link = null;

        Uri uri = ContentUris.withAppendedId(CommonConst.CONTENT_URI, id);

        Cursor cursor = ctx.getContentResolver().query(uri, null, null, null, null);
        if (cursor != null) {
            if (cursor.moveToFirst()) {
                int colId = cursor.getColumnIndex(ImageLink.COLUMN_ID);
                int colUrl = cursor.getColumnIndex(ImageLink.COLUMN_URL);
                int colStatus = cursor.getColumnIndex(ImageLink.COLUMN_STATUS);
                int colTimestamp = cursor.getColumnIndex(ImageLink.COLUMN_TIMESTAMP);

                link = new ImageLink();
                link.setLinkId(cursor.getLong(colId));
                link.setStatus(StatusConverter.intToStatus(cursor.getInt(colStatus)));
                link.setTimestamp(DateConverter.timestampToDate(cursor.getLong(colTimestamp)));
                link.setUrl(cursor.getString(colUrl));
            }
            cursor.close();
        }
        return link;
    }

    public long insertSync(ImageLink link) {
        Uri itemUri = ctx.getContentResolver().insert(CommonConst.CONTENT_URI, ImageLink.toContentValues(link));
        return ContentUris.parseId(itemUri);
    }

    public int updateSync(ImageLink link) {
        return ctx.getContentResolver().update(ContentUris.withAppendedId(CommonConst.CONTENT_URI, link.getLinkId()),
                ImageLink.toContentValues(link), null, null);
    }

    public int deleteSync(long id) {
        return ctx.getContentResolver().delete(ContentUris.withAppendedId(CommonConst.CONTENT_URI, id),
                null, null);
    }

    /* Async methods */

    public Single<ImageLink> select(final long id) {
        return Single.<ImageLink>create(emitter -> {
            ImageLink link = selectSync(id);
            if (link != null) {
                emitter.onSuccess(link);
            } else {
                emitter.onError(new Exception("Could not select ImageLink with id=" + id));
            }
        }).subscribeOn(Schedulers.io());
    }

    public Single<Long> insert(final ImageLink imageLink){
        return Single.<Long>create(emitter -> {
            long id = insertSync(imageLink);
            emitter.onSuccess(id);
        }).subscribeOn(Schedulers.io());
    }

    public Single<Integer> update(final ImageLink imageLink){
        return Single.<Integer>create(emitter -> {
            int affected = updateSync(imageLink);
            emitter.onSuccess(affected);
        }).subscribeOn(Schedulers.io());
    }

    public Single<Integer> delete(final long id){
        return Single.<Integer>create(emitter -> {
            int affected = deleteSync(id);
            emitter.onSuccess(affected);
        }).subscribeOn(Schedulers.io());
    }
}
