package com.santoni7.interactiondemo.app_b.data;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

import io.reactivex.Single;
import io.reactivex.schedulers.Schedulers;

public class RemoteBitmapSource implements BitmapSource {
    private final String TAG = RemoteBitmapSource.class.getSimpleName();

    @Override
    public Single<Bitmap> downloadImage(String urlString) {
        return Single.<Bitmap>create(emitter -> {
            InputStream inputStream = null;
            try {
                URL url = new URL(urlString);
                inputStream = url.openConnection().getInputStream();
                Bitmap img = BitmapFactory.decodeStream(inputStream);

                Log.d(TAG, "Bitmap successfully downloaded");
                emitter.onSuccess(img);
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
