package com.santoni7.interactiondemo.app_b.data;

import android.graphics.Bitmap;

import io.reactivex.Single;

public interface BitmapSource {
    Single<Bitmap> downloadImage(String urlString);
}
