package com.example.hta;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;

public class TableAdapter extends RecyclerView.Adapter<TableAdapter.NumberViewHolder> {

    private List<NumberEntity> numbers = new ArrayList<>();
    private float averageMin = 0;
    private float averageMax = 0;
    private float averageBpm = 0;

    @NonNull
    @Override
    public NumberViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.table_item, parent, false);
        return new NumberViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull NumberViewHolder holder, int position) {
        NumberEntity currentNumber = numbers.get(position);

        // Display systolic, diastolic and BPM values
        holder.textViewMax.setText(String.valueOf(currentNumber.getMaxValue()));
        holder.textViewMin.setText(String.valueOf(currentNumber.getMinValue()));
        holder.textViewBpm.setText(String.valueOf(currentNumber.getBpm()));

        // Split timestamp string into date and time
        String[] dateTimeParts = currentNumber.getTimestamp().split(" ");
        if (dateTimeParts.length == 2) {
            holder.textViewDate.setText(dateTimeParts[0]);  // Display date
            holder.textViewTime.setText(dateTimeParts[1]);  // Display time
        } else {
            holder.textViewTime.setText(currentNumber.getTimestamp());
        }

        // Display averages only on the first row (position 0)
        if (position == 0) {
            holder.textViewAverageMax.setText(String.format(Locale.getDefault(), "%.1f", averageMax));
            holder.textViewAverageMin.setText(String.format(Locale.getDefault(), "%.1f", averageMin));
            holder.textViewAverageBpm.setText(String.format(Locale.getDefault(), "%.1f", averageBpm));
            holder.textViewAverageMax.setVisibility(View.VISIBLE);
            holder.textViewAverageMin.setVisibility(View.VISIBLE);
            holder.textViewAverageBpm.setVisibility(View.VISIBLE);
        } else {
            // Hide average fields in other rows
            holder.textViewAverageMax.setText("");
            holder.textViewAverageMin.setText("");
            holder.textViewAverageBpm.setText("");
        }
    }

    @Override
    public int getItemCount() {
        return numbers.size();
    }

    public void setNumbers(List<NumberEntity> numbers) {
        // Sort the list of `NumberEntity` by year > month > day
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yy HH:mm", Locale.getDefault());

        Collections.sort(numbers, new Comparator<NumberEntity>() {
            @Override
            public int compare(NumberEntity n1, NumberEntity n2) {
                try {
                    return dateFormat.parse(n1.getTimestamp()).compareTo(dateFormat.parse(n2.getTimestamp()));
                } catch (ParseException e) {
                    e.printStackTrace();
                    return 0;
                }
            }
        });

        this.numbers = numbers;
        calculateAverages();
        notifyDataSetChanged();
    }

    private void calculateAverages() {
        float totalMin = 0;
        float totalMax = 0;
        float totalBpm = 0;

        for (NumberEntity number : numbers) {
            totalMin += number.getMinValue();
            totalMax += number.getMaxValue();
            totalBpm += number.getBpm();
        }

        if (!numbers.isEmpty()) {
            averageMin = totalMin / numbers.size();
            averageMax = totalMax / numbers.size();
            averageBpm = totalBpm / numbers.size();
        }
    }

    static class NumberViewHolder extends RecyclerView.ViewHolder {
        private TextView textViewMin;
        private TextView textViewMax;
        private TextView textViewBpm;
        private TextView textViewDate;
        private TextView textViewTime;
        private TextView textViewAverageMin;
        private TextView textViewAverageMax;
        private TextView textViewAverageBpm;

        public NumberViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewMin = itemView.findViewById(R.id.text_view_min);
            textViewMax = itemView.findViewById(R.id.text_view_max);
            textViewBpm = itemView.findViewById(R.id.text_view_bpm);
            textViewDate = itemView.findViewById(R.id.text_view_date);
            textViewTime = itemView.findViewById(R.id.text_view_time);
            textViewAverageMin = itemView.findViewById(R.id.text_view_average_min);
            textViewAverageMax = itemView.findViewById(R.id.text_view_average_max);
            textViewAverageBpm = itemView.findViewById(R.id.text_view_average_bpm);
        }
    }
}
