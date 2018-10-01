package com.santoni7.interactiondemo.app_b;

import android.app.Application;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.os.Build;

import com.santoni7.interactiondemo.app_b.injection.ComponentB;
import com.santoni7.interactiondemo.app_b.injection.DataModule;
import com.santoni7.interactiondemo.app_b.injection.ContextModule;
import com.santoni7.interactiondemo.app_b.injection.DaggerComponentB;
import com.santoni7.interactiondemo.app_b.injection.PresenterModule;

public class ApplicationB extends Application {
    private static ComponentB component;

    @Override
    public void onCreate() {
        super.onCreate();

        component = DaggerComponentB.builder()
                .contextModule(new ContextModule(getApplicationContext()))
                .dataModule(new DataModule())
                .presenterModule(new PresenterModule()).build();

        createNotificationChannel();
    }

    private void createNotificationChannel() {
        // Create the NotificationChannel, for API 26+
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = getString(R.string.channel_name);
            String description = getString(R.string.channel_description);
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel(Constants.NOTIFICATON_CHANNEL_ID, name, importance);
            channel.setDescription(description);
            // Register the channel with the system; you can't change the importance
            // or other notification behaviors after this
            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            if (notificationManager != null) {
                notificationManager.createNotificationChannel(channel);
            }
        }
    }

    public static ComponentB getComponent(){
        return component;
    }
}
