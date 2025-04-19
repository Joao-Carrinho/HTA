package com.example.hta;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModelProvider;
import java.util.List;
import lecho.lib.hellocharts.listener.LineChartOnValueSelectListener;
import lecho.lib.hellocharts.model.Axis;
import lecho.lib.hellocharts.model.AxisValue;
import lecho.lib.hellocharts.model.Line;
import lecho.lib.hellocharts.model.LineChartData;
import lecho.lib.hellocharts.model.PointValue;
import lecho.lib.hellocharts.util.ChartUtils;
import lecho.lib.hellocharts.view.LineChartView;

public abstract class BaseGraphFragment extends Fragment {

    protected AddNumberViewModel addNumberViewModel;
    protected LineChartView lineChart;
    protected TextView tvAlertMessage;
    protected ImageButton closeAlertButton;
    protected SharedPreferences sharedPreferences;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_graph, container, false);
        lineChart = view.findViewById(R.id.lineChart);
        tvAlertMessage = view.findViewById(R.id.tvAlertMessage);
        closeAlertButton = view.findViewById(R.id.closeAlertButton); // button to close the alert

        sharedPreferences = requireContext().getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
        addNumberViewModel = new ViewModelProvider(requireActivity()).get(AddNumberViewModel.class);

        lineChart.setOnValueTouchListener(new ValueTouchListener());

        // Button to close the alert
        closeAlertButton.setOnClickListener(v -> {
            tvAlertMessage.setVisibility(View.GONE);
            closeAlertButton.setVisibility(View.GONE);
        });

        return view;
    }

    protected abstract void updateGraph(List<NumberEntity> numbers);

    protected void setupChart(List<Line> lines, List<AxisValue> axisValues, String xLabel, String yLabel) {
        LineChartData data = new LineChartData();
        data.setLines(lines);

        Axis axisX = new Axis(axisValues).setName(xLabel).setHasLines(true).setHasTiltedLabels(true);
        Axis axisY = new Axis().setName(yLabel).setHasLines(true);
        data.setAxisXBottom(axisX);
        data.setAxisYLeft(axisY);

        lineChart.setLineChartData(data);
    }

    protected abstract LiveData<List<NumberEntity>> getNumbersLiveData();

    class ValueTouchListener implements LineChartOnValueSelectListener {
        @Override
        public void onValueSelected(int lineIndex, int pointIndex, PointValue value) {
            Toast.makeText(getContext(), "Value: " + value.getY(), Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onValueDeselected() {
            // Do nothing
        }
    }
}
