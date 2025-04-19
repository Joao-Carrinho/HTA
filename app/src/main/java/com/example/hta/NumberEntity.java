package com.example.hta;

import androidx.room.Entity;
import androidx.room.PrimaryKey;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

@Entity(tableName = "number_table")
public class NumberEntity {
    @PrimaryKey(autoGenerate = true)
    private int id;
    private float minValue; // Sistólica
    private float maxValue; // Diastólica
    private int bpm;        // Frequência Cardíaca
    private String timestamp;

    public NumberEntity(float minValue, float maxValue, int bpm) {
        this.minValue = minValue;
        this.maxValue = maxValue;
        this.bpm = bpm;
        this.timestamp = new SimpleDateFormat("dd-MM-yy HH:mm", Locale.getDefault()).format(new Date());
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public float getMinValue() {
        return minValue;
    }

    public void setMinValue(float minValue) {
        this.minValue = minValue;
    }

    public float getMaxValue() {
        return maxValue;
    }

    public void setMaxValue(float maxValue) {
        this.maxValue = maxValue;
    }

    public int getBpm() {
        return bpm;
    }

    public void setBpm(int bpm) {
        this.bpm = bpm;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }
}
