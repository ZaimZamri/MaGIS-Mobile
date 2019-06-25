package com.example.zaimzamrii.psmmasjid;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;

public class ItemBroadcastReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {

        String ActivityName = intent.getExtras().get("ActivityName").toString();
        String Location = intent.getExtras().get("Location").toString();
        String time = intent.getExtras().get("time").toString();
        String date = intent.getExtras().get("date").toString();
        String ustaz = intent.getExtras().get("ustaz").toString();

        NotificationHelper notificationHelper = new NotificationHelper(context);
        NotificationCompat.Builder nb = notificationHelper.getChannelNotification(ActivityName, Location,time,date,ustaz);
        notificationHelper.getManager().notify(1, nb.build());


    }
}
