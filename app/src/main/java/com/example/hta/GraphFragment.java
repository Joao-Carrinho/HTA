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
            if (numbers != null) {
                for (int i = 0; i < numbers.size(); i++) {
                    dismissedAlertIndices.add(i);
                }
                saveDismissedAlertIndices();
            } else {
                Log.w(TAG, "No numbers available to add to dismissed indices.");
            }
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
                Log.d(TAG, "Table cleared, clearing dismissed alert indices.");
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
        Log.d(TAG, "Dismissed alert indices loaded: " + dismissedAlertIndices.toString());
    }

    private void clearDismissedAlertIndices() {
        dismissedAlertIndices.clear();
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.remove(DISMISSED_ALERTS_KEY);
        editor.apply();
        Log.d(TAG, "Dismissed alert indices have been cleared.");
    }

    @Override
    protected void updateGraph(List<NumberEntity> numbers) {
        if (numbers == null || numbers.isEmpty()) {
            Log.d(TAG, "No data available to update the graph.");
            return;
        }

        this.numbers = numbers; // Updates current list of numbers
        List<PointValue> minValues = new ArrayList<>();
        List<PointValue> maxValues = new ArrayList<>();
        List<AxisValue> axisValues = new ArrayList<>();

        float totalMin = 0;
        float totalMax = 0;
        int systolicCountInRange = 0;
        int diastolicCountInRange = 0;
        int systolicCountAbove180 = 0;
        int diastolicCountAbove110 = 0;

        for (int i = 0; i < numbers.size(); i++) {
            float minValue = numbers.get(i).getMinValue();
            float maxValue = numbers.get(i).getMaxValue();
            minValues.add(new PointValue(i, minValue));
            maxValues.add(new PointValue(i, maxValue));

            axisValues.add(new AxisValue(i).setLabel(numbers.get(i).getTimestamp().split(" ")[1]));

            totalMin += minValue;
            totalMax += maxValue;

            if (dismissedAlertIndices.contains(i)) {
                continue;
            }

            if (maxValue >= 140.0 && maxValue <= 180.0) {
                systolicCountInRange++;
            }
            if (minValue >= 90.0 && minValue <= 109.0) {
                diastolicCountInRange++;
            }
            if (maxValue > 180.0) {
                systolicCountAbove180++;
            }
            if (minValue > 110.0) {
                diastolicCountAbove110++;
            }
        }

        float averageMin = totalMin / numbers.size();
        float averageMax = totalMax / numbers.size();

        List<Line> lines = new ArrayList<>();
        lines.add(new Line(minValues).setColor(ChartUtils.COLOR_BLUE).setCubic(false));
        lines.add(new Line(maxValues).setColor(ChartUtils.COLOR_GREEN).setCubic(false));

        setupChart(lines, axisValues, "Hora", "Valores");

        if (systolicCountAbove180 > 0 || diastolicCountAbove110 > 0) {
            Log.d(TAG, "Show alert: Seek medical evaluation.");
            tvAlertMessage.setText("Procure avaliação em cuidados de saúde");
            tvAlertMessage.setVisibility(View.VISIBLE);
            closeAlertButton.setVisibility(View.VISIBLE);
        } else if (systolicCountInRange >= 1 || diastolicCountInRange >= 1) {
            if (numbers.size() >= 3 &&
                    (averageMax >= 140.0 && averageMax <= 180.0) &&
                    (averageMin >= 90.0 && averageMin <= 109.0)) {
                Log.d(TAG, "Show alert: Contact Family Doctor.");
                tvAlertMessage.setText("Valores de tensão arterial acima do valor desejado. Deverá contactar o seu Médico de Família");
            } else {
                Log.d(TAG, "Show alert: Repeat measurement 2 more times today.");
                tvAlertMessage.setText("Deve repetir a medição mais 2 vezes hoje. Se os valores se mantiverem acima dos 140/90mmHg, deve medir a tensão de 3/3 dias. Se a média continuar neste intervalo deverá contactar o seu Médico Assistente/agendar consulta.");
            }
            tvAlertMessage.setVisibility(View.VISIBLE);
            closeAlertButton.setVisibility(View.VISIBLE);
        } else {
            Log.d(TAG, "No alert being shown.");
            tvAlertMessage.setVisibility(View.GONE);
            closeAlertButton.setVisibility(View.GONE);
        }

        Log.d(TAG, "Systolic Count in Range: " + systolicCountInRange);
        Log.d(TAG, "Diastolic Count in Range: " + diastolicCountInRange);
        Log.d(TAG, "Systolic Count Above 180: " + systolicCountAbove180);
        Log.d(TAG, "Diastolic Count Above 110: " + diastolicCountAbove110);
        Log.d(TAG, "Average Max: " + averageMax);
        Log.d(TAG, "Average Min: " + averageMin);
    }

    @Override
    protected LiveData<List<NumberEntity>> getNumbersLiveData() {
        return addNumberViewModel.getAllNumbers();
    }
}
