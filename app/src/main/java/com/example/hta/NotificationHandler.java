package com.example.hta;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;

import java.util.Calendar;

public class NotificationHandler {

    public static void reScheduleAllNotifications(Context context) {
        SharedPreferences prefs = context.getSharedPreferences("hta_prefs", Context.MODE_PRIVATE);

        for (String type : new String[]{"medication", "measurement"}) {
            int hour = prefs.getInt(type + "_hour", -1);
            int minute = prefs.getInt(type + "_minute", -1);
            if (hour == -1 || minute == -1) continue;

            Calendar calendar = Calendar.getInstance();
            calendar.set(Calendar.HOUR_OF_DAY, hour);
            calendar.set(Calendar.MINUTE, minute);
            calendar.set(Calendar.SECOND, 0);
            calendar.set(Calendar.MILLISECOND, 0);

            if (calendar.getTimeInMillis() < System.currentTimeMillis()) {
                calendar.add(Calendar.DAY_OF_YEAR, 1);
            }

            AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);

            Intent intent = new Intent(context, AlarmReceiver.class);
            intent.putExtra(context.getString(R.string.key_notification_type), type);

            PendingIntent pendingIntent = PendingIntent.getBroadcast(
                    context,
                    type.hashCode(),
                    intent,
                    PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE
            );

            if (alarmManager != null) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    alarmManager.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pendingIntent);
                }
            }
        }
    }
}
