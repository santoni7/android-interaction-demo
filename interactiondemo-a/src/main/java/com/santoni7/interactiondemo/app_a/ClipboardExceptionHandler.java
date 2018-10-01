package com.santoni7.interactiondemo.app_a;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.util.Log;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;

import javax.inject.Inject;

/**
 * If an exception is thrown, copies stack trace to clipboard
 */
public class ClipboardExceptionHandler implements Thread.UncaughtExceptionHandler {
    private static final String TAG = ClipboardExceptionHandler.class.getSimpleName();
    private Context context;

    @Inject
    public ClipboardExceptionHandler(Context context) {
        this.context = context;
    }


    @Override
    public void uncaughtException(Thread thread, Throwable throwable) {
        Log.e(TAG, "Uncaught Exception handled: at " + thread + "\n\tError: " + throwable.getMessage());
        throwable.printStackTrace();

        Writer writer = new StringWriter();
        throwable.printStackTrace(new PrintWriter(writer));
        String stacktrace = writer.toString();

        ClipboardManager clipboard = (ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
        ClipData clip = ClipData.newPlainText(throwable.getMessage(), stacktrace);
        if (clipboard != null) {
            clipboard.setPrimaryClip(clip);
        }
    }
}
