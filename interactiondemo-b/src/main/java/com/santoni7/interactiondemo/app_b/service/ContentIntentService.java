package com.santoni7.interactiondemo.app_b.service;

import android.app.DownloadManager;
import android.app.IntentService;
import android.app.Notification;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Environment;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.util.Log;
import android.webkit.MimeTypeMap;

import com.santoni7.interactiondemo.app_b.Constants;
import com.santoni7.interactiondemo.app_b.R;
import com.santoni7.interactiondemo.app_b.data.LinkContentRepository;

import java.io.File;


public class ContentIntentService extends IntentService {
    private static final String TAG = ContentIntentService.class.getSimpleName();

    private static final String ACTION_DELETE_IMAGELINK = "com.santoni7.interactiondemo.app_b.action.ACTION_DELETE_IMAGELINK";
    private static final String ACTION_DOWNLOAD_IMAGE = "com.santoni7.interactiondemo.app_b.action.ACTION_DOWNLOAD_IMAGE";

    private static final String EXTRA_IMAGELINK_ID = "com.santoni7.interactiondemo.app_b.extra.IMAGELINK_ID";
    private static final String EXTRA_IMAGE_URL = "com.santoni7.interactiondemo.app_b.extra.IMAGE_URL";
    private static final String EXTRA_DOWNLOAD_PATH = "com.santoni7.interactiondemo.app_b.extra.DOWNLOAD_PATH";

    private static final int NOTIFICATION_LINK_DELETED_ID = 1000;

    public ContentIntentService() {
        super("ContentIntentService");
    }


    /**
     * Build an Intent that starts this service to remove linkId from database
     */
    public static Intent buildIntentActionDeleteLink(Context context, long linkId) {
        Intent intent = new Intent(context, ContentIntentService.class);
        intent.setAction(ACTION_DELETE_IMAGELINK);
        intent.putExtra(EXTRA_IMAGELINK_ID, linkId);
        return intent;
    }

    /**
     * Build an Intent that starts this service to download image to external storage
     */
    public static Intent buildIntentActionDownloadImage(Context context, long linkId, String url, String downloadPath) {
        Intent intent = new Intent(context, ContentIntentService.class);
        intent.setAction(ACTION_DOWNLOAD_IMAGE);
        intent.putExtra(EXTRA_IMAGELINK_ID, linkId);
        intent.putExtra(EXTRA_IMAGE_URL, url);
        intent.putExtra(EXTRA_DOWNLOAD_PATH, downloadPath);
        return intent;
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        Log.d(TAG, "onHandleIntent: " + intent);
        if (intent != null) {
            final String action = intent.getAction();

            if (ACTION_DELETE_IMAGELINK.equals(action)) {

                final long linkId = intent.getLongExtra(EXTRA_IMAGELINK_ID, -1);
                Log.d(TAG, "onHandleIntent ACTION_DELETE_IMAGELINK: " + linkId);

                if (linkId >= 0) {
                    handleDeleteImageLink(linkId);
                } else {
                    throw new IllegalArgumentException("ImageLink ID not specified, or wrong");
                }
            } else if (ACTION_DOWNLOAD_IMAGE.equals(action)) {
                final long linkId = intent.getLongExtra(EXTRA_IMAGELINK_ID, -1);
                final String url = intent.getStringExtra(EXTRA_IMAGE_URL);
                final String downloadPath = intent.getStringExtra(EXTRA_DOWNLOAD_PATH);
                handleDownloadImage(linkId, url, downloadPath);
            }
        }
    }

    /**
     * Handle action Foo in the provided background thread with the provided
     * parameters.
     */
    private void handleDeleteImageLink(long linkId) {
        final LinkContentRepository repository = new LinkContentRepository(getApplicationContext());

        // Service is already running in background, don't need async method
        repository.deleteSync(linkId);

        NotificationCompat.Builder builder = getBuilder().setContentTitle("ImageLink#" + linkId + " is deleted from DB.");

        postNotification(NOTIFICATION_LINK_DELETED_ID + (int) linkId, builder.build());
    }


    private void handleDownloadImage(long linkId, String url, String directory) {
        final DownloadManager mgr = (DownloadManager) getApplicationContext().getSystemService(Context.DOWNLOAD_SERVICE);
        if (mgr == null) return;

        final Uri downloadUri = Uri.parse(url);
        final String fileName = downloadUri.getLastPathSegment();
        final String extension = MimeTypeMap.getFileExtensionFromUrl(url);
        Log.d(TAG, "Extension: " + extension);
        Log.d(TAG, "MimeType: " + MimeTypeMap.getSingleton().getMimeTypeFromExtension(extension));

        final DownloadManager.Request request = new DownloadManager.Request(downloadUri);
        request.setAllowedNetworkTypes(DownloadManager.Request.NETWORK_WIFI | DownloadManager.Request.NETWORK_MOBILE)
                .setTitle(getString(R.string.download_manager_title_format, linkId, fileName))
                .setDescription(url)
                .setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED)
                .setMimeType(MimeTypeMap.getSingleton().getMimeTypeFromExtension(extension));

        File dir;
        if (isExternalStorageWritable()) {
            dir = new File(Environment.getExternalStorageDirectory() + directory);
            if (!dir.exists() && !dir.mkdirs()) {
                Log.e(TAG, "Error: Could not make dir");
                throw new IllegalStateException("Could not make dir: " + dir.getAbsolutePath());
            }
            request.setDestinationInExternalPublicDir(directory, fileName);

        } else {
            dir = new File(Environment.getRootDirectory() + directory);
            if (!dir.exists() && !dir.mkdirs()) {
                Log.e(TAG, "Error: Could not make dir");
                throw new IllegalStateException("Could not make dir: " + dir.getAbsolutePath());
            }
            request.setDestinationUri(Uri.parse(dir.getAbsolutePath()));
        }

        long downloadId = mgr.enqueue(request);
    }

    private boolean isExternalStorageWritable() {
        String state = Environment.getExternalStorageState();
        return Environment.MEDIA_MOUNTED.equals(state);
    }

    private NotificationCompat.Builder getBuilder() {
        final Uri alarmSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

        return new NotificationCompat.Builder(this, Constants.NOTIFICATON_CHANNEL_ID)
                .setAutoCancel(true)
                .setSmallIcon(R.mipmap.ic_launcher_round)
                .setSound(alarmSound)
                .setColor(getResources().getColor(R.color.colorPrimaryDark))
                .setPriority(NotificationCompat.PRIORITY_DEFAULT);
    }

    private void postNotification(int notificationId, Notification notification) {
        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(this);
        notificationManager.notify(notificationId, notification);
    }
}
