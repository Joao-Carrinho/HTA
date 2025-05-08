package com.example.hta;

import android.app.AlarmManager;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import java.util.Calendar;

public class SettingsFragment extends Fragment {

    private TimePicker timePicker;
    private Button setMedButton, setMeasureButton;
    private TextView successMessage;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_settings, container, false);


        timePicker = view.findViewById(R.id.timePicker);
        setMedButton = view.findViewById(R.id.setMedButton);
        setMeasureButton = view.findViewById(R.id.setMeasureButton);
        successMessage = view.findViewById(R.id.successMessage);

        // Configurar botões
        setMedButton.setOnClickListener(v -> configureNotification("medication"));
        setMeasureButton.setOnClickListener(v -> configureNotification("measurement"));

        createNotificationChannel();

        return view;
    }

    private void configureNotification(String notificationType) {
        int hour=0, minute=0;

        SharedPreferences prefs = requireContext().getSharedPreferences("hta_prefs", Context.MODE_PRIVATE);
        prefs.edit()
                .putInt(notificationType + "_hour", hour)
                .putInt(notificationType + "_minute", minute)
                .apply();


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            hour = timePicker.getHour();
            minute = timePicker.getMinute();
        } else {
            hour = timePicker.getCurrentHour();
            minute = timePicker.getCurrentMinute();
        }

        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, hour);
        calendar.set(Calendar.MINUTE, minute);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);

        long triggerTime = calendar.getTimeInMillis();

        if (triggerTime < System.currentTimeMillis()) {
            triggerTime += AlarmManager.INTERVAL_DAY;
        }

        scheduleNotification(triggerTime, notificationType);
    }

    private void scheduleNotification(long triggerTime, String notificationType) {
        AlarmManager alarmManager = (AlarmManager) requireContext().getSystemService(Context.ALARM_SERVICE);

        Intent intent = new Intent(getContext(), AlarmReceiver.class);
        intent.putExtra(getString(R.string.key_notification_type), notificationType);

        PendingIntent pendingIntent = PendingIntent.getBroadcast(
                getContext(),
                notificationType.hashCode(),
                intent,
                PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE);

        if (alarmManager != null) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                alarmManager.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, triggerTime, pendingIntent);
            }
            String message = getString(notificationType.equals("medication")
                    ? R.string.notif_config_med
                    : R.string.notif_config_meas);

            Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show();
            showSuccessMessage(message);
        } else {
            Log.e("SettingsFragment", "AlarmManager não está disponível.");
        }
    }

    private void showSuccessMessage(String message) {
        successMessage.setText(message);
        successMessage.setVisibility(View.VISIBLE);
    }

    private void createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            String name = "Warning Channel";
            String description = getString(R.string.notif_channel_desc);
            int importance = NotificationManager.IMPORTANCE_HIGH;
            NotificationChannel channel = new NotificationChannel(getString(R.string.notif_channel_name), name, importance);
            channel.setDescription(description);

            NotificationManager notificationManager = requireActivity().getSystemService(NotificationManager.class);
            if (notificationManager != null) {
                notificationManager.createNotificationChannel(channel);
            }
        }
    }
}
