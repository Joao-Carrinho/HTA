package com.example.hta;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.LiveData;

import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import lecho.lib.hellocharts.model.AxisValue;
import lecho.lib.hellocharts.model.Line;
import lecho.lib.hellocharts.model.PointValue;
import lecho.lib.hellocharts.util.ChartUtils;

public class GraphFragment extends BaseGraphFragment {

    private static final String TAG = "GraphFragment";
    private static final String DISMISSED_ALERTS_KEY = "dismissed_alerts";
    private Set<Integer> dismissedAlertIndices = new HashSet<>(); // Stores dismissed alert indices
    private ImageButton closeAlertButton; // Button to close the alert
    private List<NumberEntity> numbers = new ArrayList<>(); // Stores current numbers

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = super.onCreateView(inflater, container, savedInstanceState);

        // Initializing the button to close the alert
        closeAlertButton = view.findViewById(R.id.closeAlertButton);
        closeAlertButton.setOnClickListener(v -> {
            tvAlertMessage.setVisibility(View.GONE); // Hide alert message
            closeAlertButton.setVisibility(View.GONE); // Hide button

            // Add valid indices to dismissed set and save
            if (numbers == null) {
                return;
            }

            for (int i = 0; i < numbers.size(); i++) {
                dismissedAlertIndices.add(i);
            }
            saveDismissedAlertIndices();
        });

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();

        // Load dismissed indices from SharedPreferences
        loadDismissedAlertIndices();

        // Fetch table values and draw the graph
        addNumberViewModel.getAllNumbers().observe(getViewLifecycleOwner(), numbers -> {
            if (numbers != null && !numbers.isEmpty()) {
                updateGraph(numbers);
            } else {
                clearDismissedAlertIndices();
            }
        });
    }

    private void saveDismissedAlertIndices() {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        Set<String> dismissedSet = new HashSet<>();
        for (Integer index : dismissedAlertIndices) {
            dismissedSet.add(index.toString());
        }
        editor.putStringSet(DISMISSED_ALERTS_KEY, dismissedSet);
        editor.apply();
    }

    private void loadDismissedAlertIndices() {
        SharedPreferences prefs = sharedPreferences;
        Set<String> dismissedSet = prefs.getStringSet(DISMISSED_ALERTS_KEY, new HashSet<>());
        dismissedAlertIndices.clear(); // Clear to avoid duplicate data
        for (String index : dismissedSet) {
            dismissedAlertIndices.add(Integer.parseInt(index)); // Convert back to integer
        }
    }

    private void clearDismissedAlertIndices() {
        dismissedAlertIndices.clear();
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.remove(DISMISSED_ALERTS_KEY);
        editor.apply();
    }

    @Override
    protected void updateGraph(List<NumberEntity> numbers) {
        if (numbers == null || numbers.isEmpty()) {
            return;
        }

        this.numbers = numbers; // Updates current list of numbers
        GraphValues stats = new GraphValues();

        for (int i = 0; i < numbers.size(); i++) {
            float minValue = numbers.get(i).getMinValue();
            float maxValue = numbers.get(i).getMaxValue();
            stats.minValues.add(new PointValue(i, minValue));
            stats.maxValues.add(new PointValue(i, maxValue));

            String utc = numbers.get(i).getTimestamp();
            ZonedDateTime localTime = null;
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                localTime = Instant.parse(utc).atZone(ZoneId.systemDefault());
            }
            String hour = null;
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                hour = localTime.format(DateTimeFormatter.ofPattern("HH:mm"));
            }
            stats.axisValues.add(new AxisValue(i).setLabel(hour));

            stats.totalMin += minValue;
            stats.totalMax += maxValue;

            if (dismissedAlertIndices.contains(i)) {
                continue;
            }

            if (maxValue >= 140.0 && maxValue <= 180.0) {
                stats.systolicCountInRange++;
            }
            if (minValue >= 90.0 && minValue <= 109.0) {
                stats.diastolicCountInRange++;
            }
            if (maxValue > 180.0) {
                stats.systolicCountAbove180++;
            }
            if (minValue > 110.0) {
                stats.diastolicCountAbove110++;
            }
        }

        float averageMin = stats.totalMin / numbers.size();
        float averageMax = stats.totalMax / numbers.size();

        List<Line> lines = new ArrayList<>();
        lines.add(new Line(stats.minValues).setColor(ChartUtils.COLOR_BLUE).setCubic(false));
        lines.add(new Line(stats.maxValues).setColor(ChartUtils.COLOR_GREEN).setCubic(false));

        setupChart(lines, stats.axisValues, getString(R.string.label_time), getString(R.string.label_values));



        if (stats.systolicCountAbove180 > 0 || stats.diastolicCountAbove110 > 0) {
            Log.d(TAG, "Show alert: Seek medical evaluation.");
            tvAlertMessage.setText(getString(R.string.alert_high_urgency));
            tvAlertMessage.setVisibility(View.VISIBLE);
            closeAlertButton.setVisibility(View.VISIBLE);
        } else if (stats.systolicCountInRange >= 1 || stats.diastolicCountInRange >= 1) {
            if (numbers.size() >= 3 &&
                    (averageMax >= 140.0 && averageMax <= 180.0) &&
                    (averageMin >= 90.0 && averageMin <= 109.0)) {
                Log.d(TAG, "Show alert: Contact Family Doctor.");
                tvAlertMessage.setText(getString(R.string.alert_doctor_recommended));
            } else {
                Log.d(TAG, "Show alert: Repeat measurement 2 more times today.");
                tvAlertMessage.setText(getString(R.string.alert_repeat_measurements)
                );
            }
            tvAlertMessage.setVisibility(View.VISIBLE);
            closeAlertButton.setVisibility(View.VISIBLE);
        } else {
            Log.d(TAG, "No alert being shown.");
            tvAlertMessage.setVisibility(View.GONE);
            closeAlertButton.setVisibility(View.GONE);
        }


    }

    @Override
    protected LiveData<List<NumberEntity>> getNumbersLiveData() {
        return addNumberViewModel.getAllNumbers();
    }
}
