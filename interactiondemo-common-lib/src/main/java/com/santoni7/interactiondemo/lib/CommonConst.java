package com.santoni7.interactiondemo.lib;

import android.net.Uri;

import com.santoni7.interactiondemo.lib.model.ImageLink;

public class CommonConst {
    public static final String AUTHORITY = "com.santoni7.interactiondemo.app_a.provider";
    public static final Uri CONTENT_URI = Uri.parse("content://" + AUTHORITY + "/" + ImageLink.TABLE_NAME);


    public static final String ACTION_NEW_LINK = "com.santoni7.interactiondemo.ACTION_NEW_LINK";
    public static final String ACTION_OPEN_LINK = "com.santoni7.interactiondemo.ACTION_OPEN_LINK";

    public static final String EXTRA_LINK_URL = "com.santoni7.interactiondemo.EXTRA_LINK_URL";
    public static final String EXTRA_LINK_ID = "com.santoni7.interactiondemo.EXTRA_LINK_ID";
}
