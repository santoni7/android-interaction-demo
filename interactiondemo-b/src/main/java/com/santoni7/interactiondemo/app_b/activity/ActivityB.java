package com.santoni7.interactiondemo.app_b.activity;

import android.Manifest;
import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.os.Bundle;
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

import com.santoni7.interactiondemo.app_b.ApplicationB;
import com.santoni7.interactiondemo.app_b.R;
import com.santoni7.interactiondemo.app_b.service.ContentIntentService;
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
    @BindView(R.id.txtStatus) TextView textViewStatus;
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
        presenter = ApplicationB.getComponent().providePresenter();
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
            showMessage(ContractB.Message.ERR_WRONG_ACTIVITY_ACTION);
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

        switch (imageLink.getStatus()) {
            case UNKNOWN:
                textViewStatus.setText(R.string.status_unknown);
                break;
            case ERROR:
                textViewStatus.setText(R.string.status_error);
                break;
            case LOADED:
                textViewStatus.setText(R.string.status_loaded);
                break;
        }

        toolbar.setSubtitle(getString(R.string.toolbar_subtitle_format, imageLink.getLinkId()));
        if (image != null) {
            cardView.setCardElevation(8);
            imageView.setImageBitmap(image);
        } else {
            cardView.setCardElevation(0);
            imageView.setImageResource(R.mipmap.icon_warning);
        }

        final int FADE_IN_DURATION = 700;
        AndroidUtils.animateViewFadeIn(textViewUrl, FADE_IN_DURATION);
        AndroidUtils.animateViewFadeIn(cardView, FADE_IN_DURATION);
        AndroidUtils.animateViewFadeIn(textViewStatus, FADE_IN_DURATION);
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

    public void snackbarOk(int resId) {
        Snackbar.make(coordinatorLayout, resId, Snackbar.LENGTH_LONG).setAction(R.string.ok, v -> {
        }).show();
    }

    @Override
    public void showMessage(ContractB.Message message) {
        snackbarOk(message.getId());
    }

    private void checkPermissions() {
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {

            // Permission is not granted
            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                // Show alert dialog
                new AlertDialog.Builder(this)
                        .setMessage(R.string.alert_ask_permissions)
                        .setNeutralButton(R.string.ok, (dialogInterface, i) -> dialogInterface.dismiss())
                        .show();
            } else {
                // No explanation needed; request the permission
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                        REQUEST_CODE_STORAGE_PERMISSIONS);
            }
        }
    }
}
