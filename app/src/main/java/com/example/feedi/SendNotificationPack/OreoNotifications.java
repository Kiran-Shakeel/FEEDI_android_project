package com.example.feedi.SendNotificationPack;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.ContextWrapper;
import android.os.Build;

import androidx.annotation.RequiresApi;
import androidx.core.app.NotificationManagerCompat;

import com.example.feedi.MainActivity;
import com.example.feedi.R;
import com.google.firebase.messaging.CommonNotificationBuilder;

public class OreoNotifications extends ContextWrapper {

    public static final String CHANNEL_ID = "MESSAGE";
    public static final String CHANNEL_NAME = "MESSAGE";

    private NotificationManager manager;

    public OreoNotifications(Context base) {
        super(base);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            createChannel();
        }
    }


    @RequiresApi(api = Build.VERSION_CODES.O)
    private void createChannel() {

        NotificationChannel channel=new NotificationChannel(CHANNEL_ID,CHANNEL_NAME,NotificationManager.IMPORTANCE_DEFAULT);
        manager.createNotificationChannel(channel);

    }

    public NotificationManager managers()
    {
        if(manager==null)
        {
            manager=(NotificationManager)getSystemService(Context.NOTIFICATION_SERVICE);
        }
        return manager;
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public Notification.Builder getnoti(String title, String message)
    {
        return new Notification.Builder(OreoNotifications.this,CHANNEL_ID)
                .setSmallIcon(R.drawable.chat)
                .setContentTitle("TitleTB.getText().toString()")
                .setContentText("MessageTB.getText().toString()");


    }
}


