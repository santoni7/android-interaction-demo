package com.santoni7.interactiondemo.app_a.provider;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.UriMatcher;
import android.database.Cursor;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.santoni7.interactiondemo.app_a.ApplicationA;
import com.santoni7.interactiondemo.app_a.data.ImageLinkDao;
import com.santoni7.interactiondemo.lib.CommonConst;
import com.santoni7.interactiondemo.lib.model.ImageLink;

/**
 * A ContentProvider based on ImageLinkDatabase.
 * It is needed to allow other applications (like App B) to access database of this app.
 */
public class LinkContentProvider extends ContentProvider {

    private static UriMatcher MATCHER = new UriMatcher(UriMatcher.NO_MATCH);

    private static final int CODE_LINKS_ALL = 1;
    private static final int CODE_LINKS_SINGLE = 2;

    static {
        MATCHER.addURI(CommonConst.AUTHORITY, ImageLink.TABLE_NAME, CODE_LINKS_ALL);
        MATCHER.addURI(CommonConst.AUTHORITY, ImageLink.TABLE_NAME + "/*", CODE_LINKS_SINGLE);
    }

    @Override
    public boolean onCreate() {
        return true;
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] projection, @Nullable String selection,
                        @Nullable String[] selectionArgs, @Nullable String sortOrder) {
        final int code = MATCHER.match(uri);
        if (code == CODE_LINKS_ALL || code == CODE_LINKS_SINGLE) {
            Context ctx = getContext();
            if (ctx == null) return null;

            final ImageLinkDao dao = ApplicationA.getComponent().provideDatabase().getImageLinkDao();
            final Cursor cursor;

            if (code == CODE_LINKS_ALL) {
                cursor = dao.selectAll();
            } else {
                long id = ContentUris.parseId(uri);
                cursor = dao.selectById(id);
            }
            // Set notification uri, so that observers of this uri will be notified
            cursor.setNotificationUri(ctx.getContentResolver(), uri);
            return cursor;
        } else {
            throw new IllegalArgumentException("Unknown URI provided: " + uri.toString());
        }
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        switch (MATCHER.match(uri)) {
            case CODE_LINKS_ALL:
                return "vnd.android.cursor.dir/" + CommonConst.AUTHORITY + "." + ImageLink.TABLE_NAME;
            case CODE_LINKS_SINGLE:
                return "vnd.android.cursor.item/" + CommonConst.AUTHORITY + "." + ImageLink.TABLE_NAME;
            default:
                throw new IllegalArgumentException("");
        }
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues contentValues) {
        switch (MATCHER.match(uri)) {
            case CODE_LINKS_ALL:
                final ImageLinkDao dao = ApplicationA.getComponent().provideDatabase().getImageLinkDao();
                final Context ctx = getContext();
                if (ctx == null || contentValues == null) {
                    return null;
                }

                final ImageLink link = ImageLink.fromContentValues(contentValues);
                final long id = dao.insert(link);

                ctx.getContentResolver().notifyChange(uri, null);
                return ContentUris.withAppendedId(uri, id);
            case CODE_LINKS_SINGLE:
                throw new IllegalArgumentException("\"Wrong URI - Cannot insert into item: \" + uri.toString()");
            default:
                throw new IllegalArgumentException("Unknown URI provided: " + uri.toString());
        }
    }


    @Override
    public int delete(@NonNull Uri uri, @Nullable String selection,
                      @Nullable String[] selectionArgs) {
        switch (MATCHER.match(uri)) {
            case CODE_LINKS_ALL:
                throw new IllegalArgumentException("Must specify ID in URI: " + uri);
            case CODE_LINKS_SINGLE:
                final ImageLinkDao dao = ApplicationA.getComponent().provideDatabase().getImageLinkDao();
                final Context ctx = getContext();
                if (ctx == null) {
                    return 0;
                }

                final int count = dao.delete(ContentUris.parseId(uri));

                ctx.getContentResolver().notifyChange(uri, null);
                return count;
            default:
                throw new IllegalArgumentException("Unknown URI: " + uri);
        }
    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues contentValues, @Nullable String s,
                      @Nullable String[] strings) {
        switch (MATCHER.match(uri)) {
            case CODE_LINKS_ALL:
                throw new IllegalArgumentException("Must specify ID in URI: " + uri);
            case CODE_LINKS_SINGLE:
                final ImageLinkDao dao = ApplicationA.getComponent().provideDatabase().getImageLinkDao();
                final Context ctx = getContext();
                if (ctx == null || contentValues == null) {
                    return 0;
                }

                final ImageLink link = ImageLink.fromContentValues(contentValues);
                link.setLinkId(ContentUris.parseId(uri));
                final int count = dao.update(link);

                ctx.getContentResolver().notifyChange(uri, null);
                return count;
            default:
                throw new IllegalArgumentException("Unknown URI: " + uri);
        }
    }


}
