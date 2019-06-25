package com.example.zaimzamrii.psmmasjid;

import android.annotation.TargetApi;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.ContextWrapper;
import android.os.Build;
import android.support.v4.app.NotificationCompat;

public class NotificationHelper extends ContextWrapper {
    public static final String channelID = "channelID";
    public static final String channelName = "Channel Name";

    private NotificationManager mManager;

    public NotificationHelper(Context base) {
        super(base);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            createChannel();
        }
    }

    @TargetApi(Build.VERSION_CODES.O)
    private void createChannel() {
        NotificationChannel channel = new NotificationChannel(channelID, channelName, NotificationManager.IMPORTANCE_HIGH);

        getManager().createNotificationChannel(channel);
    }

    public NotificationManager getManager() {
        if (mManager == null) {
            mManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        }

        return mManager;
    }

    public NotificationCompat.Builder getChannelNotification(String an, String Loc,String time,String date, String ustaz) {
        String message = an+" By "+ustaz+" @ "+ Loc +" "+date+" "+time;
        return new NotificationCompat.Builder(getApplicationContext(), channelID)
                .setContentTitle("Upcoming Activity!")
                .setStyle(new NotificationCompat.BigTextStyle().bigText(message))
                .setContentText(message)

                .setSmallIcon(R.mipmap.ic_launcher_mosque);
    }
}
