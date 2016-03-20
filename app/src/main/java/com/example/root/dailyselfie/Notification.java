package com.example.root.dailyselfie;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

/**
 * Created by root on 22/8/15.
 */

public class Notification extends BroadcastReceiver {

    Intent selfieIntent;
    PendingIntent nContentIntent;
    private final CharSequence contentTitle = "Daily Selfie";
    private final CharSequence contentText = "Time for another selfie!";
    @Override
    public void onReceive(Context context, Intent intent) {
        selfieIntent = new Intent(context, MainActivity.class);
        nContentIntent = PendingIntent.getActivity(context, 0, selfieIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        android.app.Notification.Builder notificationBuilder = new android.app.Notification.Builder(context)
                .setContentText(contentText)
                .setContentTitle(contentTitle)
                .setSmallIcon(android.R.drawable.ic_menu_camera)
                .setContentIntent(nContentIntent)
                .setAutoCancel(true);
        NotificationManager mNotificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        mNotificationManager.notify(1, notificationBuilder.build());

    }
}
