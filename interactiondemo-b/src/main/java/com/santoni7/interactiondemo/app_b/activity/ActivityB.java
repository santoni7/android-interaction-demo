package com.santoni7.interactiondemo.app_b.activity;

import android.Manifest;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.santoni7.interactiondemo.app_b.service.ContentIntentService;
import com.santoni7.interactiondemo.app_b.data.LinkContentRepository;
import com.santoni7.interactiondemo.app_b.R;
import com.santoni7.interactiondemo.lib.AndroidUtils;
import com.santoni7.interactiondemo.lib.CommonConst;
import com.santoni7.interactiondemo.lib.model.ImageLink;

import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ActivityB extends AppCompatActivity implements ContractB.View {

    private static final String TAG = ActivityB.class.getSimpleName();
    private static final int REQUEST_CODE_STORAGE_PERMISSIONS = 1023;

    @BindView(R.id.imageView) ImageView imageView;
    @BindView(R.id.cardView) CardView cardView;
    @BindView(R.id.progressBar) ProgressBar progressBar;
    @BindView(R.id.txtUrl) TextView textViewUrl;
    @BindView(R.id.toolbar) Toolbar toolbar;
    @BindView(R.id.main_content) CoordinatorLayout coordinatorLayout;

    private ContractB.Presenter presenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_app_b);
        ButterKnife.bind(this);


        initView();

        // todo inject repo
        presenter = new PresenterB(new LinkContentRepository(getApplicationContext()));
        presenter.attachView(this);
        presenter.viewReady();

        processIntent(getIntent());

        checkPermissions();
    }


    private void initView() {
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
    }

    private void processIntent(Intent intent) {
        String action = intent.getAction();
        if (CommonConst.ACTION_NEW_LINK.equals(action)) {
            String url = intent.getStringExtra(CommonConst.EXTRA_LINK_URL);

            presenter.onActionNewLink(url);
        } else if (CommonConst.ACTION_OPEN_LINK.equals(action)) {
            long id = intent.getLongExtra(CommonConst.EXTRA_LINK_ID, -1);

            presenter.onActionOpenLink(id);
        } else {
            // TODO: ERROR: Wrong action provided
            errorMessage(R.string.error_wrong_action);
        }
    }

    @Override
    public void showProgressBar() {
        progressBar.setVisibility(View.VISIBLE);
        imageView.setVisibility(View.GONE);
        textViewUrl.setVisibility(View.INVISIBLE);
    }

    @Override
    public void hideProgressBar() {
        progressBar.setVisibility(View.GONE);
        imageView.setVisibility(View.VISIBLE);
        textViewUrl.setVisibility(View.VISIBLE);

        final int FADE_IN_DURATION = 600;
        AndroidUtils.animateViewFadeIn(textViewUrl, FADE_IN_DURATION);
        AndroidUtils.animateViewFadeIn(imageView, FADE_IN_DURATION);
    }

    @Override
    public void errorMessage(int resId) {
        Toast.makeText(this, resId, Toast.LENGTH_LONG).show();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
            return false;
        }

        return super.onOptionsItemSelected(item);
    }



    @Override
    public void displayData(ImageLink imageLink, Bitmap image) {
        textViewUrl.setText(imageLink.getUrl());
        toolbar.setSubtitle("ImageLink#" + imageLink.getLinkId());
        if (image != null) {
            imageView.setImageBitmap(image);
        } else {
            imageView.setImageResource(R.mipmap.icon_warning);
        }
    }

    @Override
    public void scheduleDeleteLink(ImageLink imageLink, long delayMs) {
        Intent deleteLinkIntent = ContentIntentService.buildIntentActionDeleteLink(getApplicationContext(), imageLink.getLinkId());
        PendingIntent pendingIntent = PendingIntent.getService(getApplicationContext(), (int) imageLink.getLinkId(), deleteLinkIntent, 0);

        scheduleIntent(pendingIntent, delayMs);
    }

    @Override
    public void scheduleDownloadImage(ImageLink imageLink, String destinationPath, long delayMs) {
        Intent deleteLinkIntent = ContentIntentService.buildIntentActionDownloadImage(getApplicationContext(),
                imageLink.getLinkId(), imageLink.getUrl(), destinationPath);

        PendingIntent pendingIntent = PendingIntent.getService(getApplicationContext(), (int) imageLink.getLinkId(), deleteLinkIntent, 0);

        scheduleIntent(pendingIntent, delayMs);
    }

    private void scheduleIntent(PendingIntent pendingIntent, long delayMs) {
        AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
        if (alarmManager != null) {
            alarmManager.set(AlarmManager.RTC_WAKEUP, System.currentTimeMillis() + delayMs, pendingIntent);
        }
    }

    @Override
    public void snackbarOk(String message) {
        Snackbar.make(coordinatorLayout, message, Snackbar.LENGTH_LONG).setAction(R.string.ok, v -> {
        }).show();
    }


    private void checkPermissions() {
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {

            // Permission is not granted
            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                // Show an explanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.
            } else {
                // No explanation needed; request the permission
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                        REQUEST_CODE_STORAGE_PERMISSIONS);
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String permissions[], @NonNull int[] grantResults) {
        switch (requestCode) {
            case REQUEST_CODE_STORAGE_PERMISSIONS: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // permission was granted, yay! Do the
                    // contacts-related task you need to do.
                } else {
                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                }
                return;
            }
        }
    }
}
