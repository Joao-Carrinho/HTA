package com.example.hta;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class TimeZoneHandler extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        if (Intent.ACTION_TIMEZONE_CHANGED.equals(intent.getAction())) {
            NotificationHandler.reScheduleAllNotifications(context);
        }
    }
}