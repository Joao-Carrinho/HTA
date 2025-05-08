package com.example.hta;

import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
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
        String utc = currentNumber.getTimestamp();
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            try {
                ZonedDateTime localTime = Instant.parse(utc).atZone(ZoneId.systemDefault());
                String date = localTime.toLocalDate().format(DateTimeFormatter.ofPattern("dd-MM-yy"));
                String time = localTime.toLocalTime().format(DateTimeFormatter.ofPattern("HH:mm"));
                holder.textViewDate.setText(date);
                holder.textViewTime.setText(time);
            } catch (Exception e) {
                holder.textViewDate.setText("");
                holder.textViewTime.setText("");
            }
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
        Collections.sort(numbers, new Comparator<NumberEntity>() {
            @Override
            public int compare(NumberEntity n1, NumberEntity n2) {
                try {
                    return Instant.parse(n1.getTimestamp()).compareTo(Instant.parse(n2.getTimestamp()));
                } catch (Exception e) {
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

    public float getAverageMin() {
        return averageMin;
    }
    public float getAverageMax() {
        return averageMax;
    }
    public float getAverageBpm() {
        return averageBpm;
    }
    public List<NumberEntity> getNumbers() {
        return numbers;
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
