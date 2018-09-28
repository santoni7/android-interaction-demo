package com.santoni7.interactiondemo.app_b.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.santoni7.interactiondemo.app_b.LinkContentRepository;
import com.santoni7.interactiondemo.app_b.R;
import com.santoni7.interactiondemo.lib.AndroidUtils;
import com.santoni7.interactiondemo.lib.CommonConst;
import com.santoni7.interactiondemo.lib.model.ImageLink;

import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ActivityB extends AppCompatActivity implements ContractB.View {

    private static final String TAG = ActivityB.class.getSimpleName();

    @BindView(R.id.imageView) ImageView imageView;
    @BindView(R.id.cardView) CardView cardView;
    @BindView(R.id.progressBar) ProgressBar progressBar;
    @BindView(R.id.txtUrl) TextView textViewUrl;
    @BindView(R.id.toolbar) Toolbar toolbar;
//    @BindView(R.id.appbar) AppBarLayout appBarLayout;

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
    }

    private void initView() {
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
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

        // Fade in textCountdown and imageView:
        AndroidUtils.animateViewFadeIn(textViewUrl, 600);
        AndroidUtils.animateViewFadeIn(imageView, 600);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
            return false;
        }

        return super.onOptionsItemSelected(item);
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
        }
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
}
