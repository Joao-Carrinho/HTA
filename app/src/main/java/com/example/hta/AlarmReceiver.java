package com.example.hta;

import android.app.Notification;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

public class AlarmReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        String key = intent.getStringExtra(context.getString(R.string.key_notification_type));
        NotificationType type = NotificationType.fromKey(context, key);

        String title = context.getString(R.string.notif_title);
        String message;

        switch (type) {
            case MEDICATION:
                message = context.getString(R.string.notif_medication);
                break;
            case MEASUREMENT:
                message = context.getString(R.string.notif_measurement);
                break;
            default:
                message = context.getString(R.string.notif_generic);
                break;
        }

        createNotification(context, title, message, context.getString(R.string.channel_warning));
    }

    private void createNotification(Context context, String title, String message, String channelId) {
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, channelId)
                .setSmallIcon(R.drawable.ic_notification)
                .setContentTitle(title)
                .setContentText(message)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setDefaults(Notification.DEFAULT_ALL)
                .setAutoCancel(true);

        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(context);

        int notificationId = 1;
        notificationManager.notify(notificationId, builder.build());
    }


private void requestNotificationPermission(Context context) {
        Intent intent = new Intent(context, MainActivity.class);
    intent.putExtra(context.getString(R.string.key_request_permission), true);
    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }
}
