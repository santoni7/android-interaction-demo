package com.santoni7.interactiondemo.app_b;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.Toast;

import com.santoni7.interactiondemo.lib.CommonConst;
import com.santoni7.interactiondemo.lib.model.ImageLink;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.util.Calendar;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = MainActivity.class.getSimpleName();

    @BindView(R.id.imageView) ImageView imageView;

    private ImageLink imageLink;

    private LinkContentRepository repository;

    private Callback picassoCallback = new Callback() {
        @Override
        public void onSuccess() {
            Log.d(TAG, "picassoCallback: onSuccess");
            if(imageLink != null){
                imageLink.setStatus(ImageLink.Status.LOADED);
                repository.update(imageLink);
            }
        }

        @Override
        public void onError(Exception e) {
            Log.d(TAG, "picassoCallback: onError");
            e.fillInStackTrace();

            if(imageLink != null){
                imageLink.setStatus(ImageLink.Status.ERROR);
                repository.update(imageLink);
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        Picasso.get().setIndicatorsEnabled(true);

        repository = new LinkContentRepository(getApplicationContext());


        Intent intent = getIntent();
        String action = intent.getAction();
        if (CommonConst.ACTION_NEW_LINK.equals(action)) {
            String url = intent.getStringExtra(CommonConst.EXTRA_LINK_URL);

            imageLink = new ImageLink();
            imageLink.setTimestamp(Calendar.getInstance().getTime());
            imageLink.setUrl(url);
            imageLink.setStatus(ImageLink.Status.UNKNOWN);

            long id = repository.insert(imageLink);
            imageLink.setLinkId(id);
        } else if (CommonConst.ACTION_OPEN_LINK.equals(action)) {
            // todo
            long id = intent.getLongExtra(CommonConst.EXTRA_LINK_ID, -1);
            imageLink = repository.select(id);
        } else {
            //todo error: wrong action
        }
        if (imageLink != null) {
            Picasso.get().load(imageLink.getUrl()).into(imageView, picassoCallback);
        }
    }


    private void toast(String msg) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }
}
