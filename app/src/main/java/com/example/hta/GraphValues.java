package com.example.hta;

import java.util.ArrayList;
import java.util.List;
import lecho.lib.hellocharts.model.AxisValue;
import lecho.lib.hellocharts.model.PointValue;

public class GraphValues {
    public final List<PointValue> minValues = new ArrayList<>();
    public final List<PointValue> maxValues = new ArrayList<>();
    public final List<AxisValue> axisValues = new ArrayList<>();

    public float totalMin = 0;
    public float totalMax = 0;
    public int systolicCountInRange = 0;
    public int diastolicCountInRange = 0;
    public int systolicCountAbove180 = 0;
    public int diastolicCountAbove110 = 0;

    public float averageMin = 0;
    public float averageMax = 0;

    public void computeAverages(int size) {
        if (size > 0) {
            averageMin = totalMin / size;
            averageMax = totalMax / size;
        }
    }
}
