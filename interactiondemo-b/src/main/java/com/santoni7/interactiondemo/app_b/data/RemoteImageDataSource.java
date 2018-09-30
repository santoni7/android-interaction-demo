package com.santoni7.interactiondemo.app_b.data;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

import io.reactivex.Observable;
import io.reactivex.schedulers.Schedulers;

public class RemoteImageDataSource {
    private final String TAG = RemoteImageDataSource.class.getSimpleName();

    public Observable<Bitmap> downloadImage(String urlString) {
        return Observable.<Bitmap>create(emitter -> {
            InputStream inputStream = null;
            try {
                URL url = new URL(urlString);
                inputStream = url.openConnection().getInputStream();
                Bitmap img = BitmapFactory.decodeStream(inputStream);

                Log.d(TAG, "Bitmap successfully downloaded");
                emitter.onNext(img);
                emitter.onComplete();
            } catch (IOException e) {
                emitter.onError(e);
                Log.e(TAG, "Exception occurred during execution: " + e);
                e.printStackTrace();
            } finally {
                if (inputStream != null)
                    inputStream.close();
            }
        }).subscribeOn(Schedulers.io());
    }
}
