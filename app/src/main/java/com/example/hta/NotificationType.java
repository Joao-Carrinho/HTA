package com.example.hta;

import android.content.Context;


public enum NotificationType {
    MEDICATION,
    MEASUREMENT,
    UNKNOWN;

    public static NotificationType fromKey(Context context, String key) {
        if (key.equals(context.getString(R.string.notification_key_medication))) {
            return MEDICATION;
        } else if (key.equals(context.getString(R.string.notification_key_measurement))) {
            return MEASUREMENT;
        } else {
            return UNKNOWN;
        }
    }
}