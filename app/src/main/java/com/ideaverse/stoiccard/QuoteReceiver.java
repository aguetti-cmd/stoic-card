package com.ideaverse.stoiccard;

import android.app.AlarmManager;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;

import java.util.Calendar;

public class QuoteReceiver extends BroadcastReceiver {
    private static final String CHANNEL_ID = "daily_quote";
    private static final String ACTION_SHOW = "com.ideaverse.stoiccard.SHOW_QUOTE";
    private static final int NOTIFICATION_ID = 1;
    private static final int ALARM_HOUR = 8;

    @Override
    public void onReceive(Context context, Intent intent) {
        if (Intent.ACTION_BOOT_COMPLETED.equals(intent.getAction())) {
            if (morningQuoteEnabled(context)) {
                schedule(context);
            }
            return;
        }
        showNotification(context);
    }

    static boolean morningQuoteEnabled(Context context) {
        SharedPreferences prefs = context.getSharedPreferences(
                MainActivity.PREFS_NAME, Context.MODE_PRIVATE);
        return prefs.getBoolean(MainActivity.MORNING_QUOTE_KEY, false);
    }

    static void schedule(Context context) {
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        if (alarmManager == null) {
            return;
        }
        Calendar next = Calendar.getInstance();
        next.set(Calendar.HOUR_OF_DAY, ALARM_HOUR);
        next.set(Calendar.MINUTE, 0);
        next.set(Calendar.SECOND, 0);
        next.set(Calendar.MILLISECOND, 0);
        if (next.getTimeInMillis() <= System.currentTimeMillis()) {
            next.add(Calendar.DAY_OF_MONTH, 1);
        }
        alarmManager.setInexactRepeating(AlarmManager.RTC_WAKEUP,
                next.getTimeInMillis(), AlarmManager.INTERVAL_DAY, alarmIntent(context));
    }

    static void cancel(Context context) {
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        if (alarmManager != null) {
            alarmManager.cancel(alarmIntent(context));
        }
    }

    private static PendingIntent alarmIntent(Context context) {
        Intent intent = new Intent(context, QuoteReceiver.class).setAction(ACTION_SHOW);
        return PendingIntent.getBroadcast(context, 0, intent,
                PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE);
    }

    private void showNotification(Context context) {
        NotificationManager manager =
                (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        if (manager == null || !manager.areNotificationsEnabled()) {
            return;
        }

        Notification.Builder builder;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID,
                    "Morning quote", NotificationManager.IMPORTANCE_DEFAULT);
            channel.setDescription("One Stoic quote every morning");
            manager.createNotificationChannel(channel);
            builder = new Notification.Builder(context, CHANNEL_ID);
        } else {
            builder = new Notification.Builder(context);
        }

        String[] quote = Quotes.today();
        PendingIntent openApp = PendingIntent.getActivity(context, 0,
                new Intent(context, MainActivity.class),
                PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE);

        builder.setSmallIcon(R.drawable.ic_notification)
                .setContentTitle(quote[1] + ", " + quote[2])
                .setContentText(quote[0])
                .setStyle(new Notification.BigTextStyle().bigText(quote[0]))
                .setContentIntent(openApp)
                .setAutoCancel(true);

        manager.notify(NOTIFICATION_ID, builder.build());
    }
}
