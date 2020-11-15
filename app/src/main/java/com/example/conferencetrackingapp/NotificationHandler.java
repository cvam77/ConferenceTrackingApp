package com.example.conferencetrackingapp;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.core.content.ContextCompat;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;

public class NotificationHandler {
    private static final int REMINDER_NOTIFICATION_ID = 77;

    private static final int REMINDER_PENDING_INTENT_ID = 88;

    private static final String REMINDER_CHANNEL_ID = "reminder";

    public static void clearNotification(Context context)
    {
        NotificationManager notificationManager = (NotificationManager)
                context.getSystemService(Context.NOTIFICATION_SERVICE);

        notificationManager.cancelAll();;
    }

    public static void notificationCreator(Context context, String conferenceName, String guestNAme) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O)
        {
            CharSequence channelName = "Reminder Notification";
            String channelDescription = "Creating Notification for Aims";
            int channelImportance = NotificationManager.IMPORTANCE_HIGH;

            NotificationChannel notificationChannel = new NotificationChannel(REMINDER_CHANNEL_ID,
                    channelName,channelImportance);

            notificationChannel.setDescription(channelDescription);

            NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

            notificationManager.createNotificationChannel(notificationChannel);
        }



        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(context,REMINDER_CHANNEL_ID);

        notificationBuilder.setSmallIcon(R.drawable.common_google_signin_btn_icon_dark)
                .setContentTitle("Conference Title: " + conferenceName)
                .setContentText("YO")
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setColor(ContextCompat.getColor(context,R.color.black))
                .setLargeIcon(bitmap(context))
                .setStyle(new NotificationCompat.BigTextStyle().bigText(guestNAme + " will be attending!"))
                .setDefaults(Notification.DEFAULT_VIBRATE)
                .setContentIntent(getPendingIntent(context))
                .setAutoCancel(true);

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN
                && Build.VERSION.SDK_INT < Build.VERSION_CODES.O)
        {
            notificationBuilder.setPriority(NotificationCompat.PRIORITY_HIGH);
        }
        NotificationManagerCompat notificationManagerCompat = NotificationManagerCompat.from(context);

        Random rand = new Random();
        int randomNumber = rand.nextInt(9999 - 1000) + 1000;

        notificationManagerCompat.notify(randomNumber,notificationBuilder.build());
    }

    private static PendingIntent getPendingIntent(Context context)
    {
        Intent notificationPendingIntent = new Intent(context,MyConferences.class);

        return PendingIntent.getActivity(context,
                REMINDER_PENDING_INTENT_ID,
                notificationPendingIntent,
                PendingIntent.FLAG_UPDATE_CURRENT);
    }

    private static Bitmap bitmap(Context context)
    {
        Resources resources = context.getResources();
        Bitmap icon = BitmapFactory.decodeResource(resources, R.drawable.ic_launcher_foreground);
        return icon;
    }
}
