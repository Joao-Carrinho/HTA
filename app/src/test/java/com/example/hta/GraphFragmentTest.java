
package com.example.hta;

import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.junit.Assert.*;


import lecho.lib.hellocharts.model.PointValue;

public class GraphFragmentTest {


    private List<NumberEntity> mockData;

    @Before
    public void setup() {
                mockData = Arrays.asList(
                new NumberEntity(95, 145, 70),  // in-range
                new NumberEntity(91, 160, 75),  // in-range
                new NumberEntity(88, 139, 70),  // systolic out of range
                new NumberEntity(111, 120, 70), // diastolic above 110
                new NumberEntity(115, 130, 70), // diastolic above 110
                new NumberEntity(30, 100, 70),  // extreme min
                new NumberEntity(200, 220, 80)  // extreme max
        );
    }

    @Test
    public void testSystolicInRangeCount() {
        GraphValues stats = new GraphValues();
        for (NumberEntity e : mockData) {
            float max = e.getMaxValue();
            if (max >= 140 && max <= 180) stats.systolicCountInRange++;
        }
        assertEquals(2, stats.systolicCountInRange);
    }

    @Test
    public void testDiastolicAbove110Count() {
        GraphValues stats = new GraphValues();
        for (NumberEntity e : mockData) {
            if (e.getMinValue() > 110) stats.diastolicCountAbove110++;
        }
        assertEquals(3, stats.diastolicCountAbove110);
    }

    @Test
    public void testComputeAverages() {
        GraphValues stats = new GraphValues();
        for (NumberEntity e : mockData) {
            stats.totalMin += e.getMinValue();
            stats.totalMax += e.getMaxValue();
        }
        stats.computeAverages(mockData.size());
        assertEquals(104.2, stats.averageMin, 1.0);
        assertEquals(145.0, stats.averageMax, 1.0);
    }

    @Test
    public void testSystolicAbove180Count() {
        GraphValues stats = new GraphValues();
        for (NumberEntity e : mockData) {
            if (e.getMaxValue() > 180) stats.systolicCountAbove180++;
        }
        assertEquals(1, stats.systolicCountAbove180);
    }

    @Test
    public void testAverageWithExtremeValues() {
        List<NumberEntity> mockData = Arrays.asList(
                new NumberEntity(30, 100, 70),
                new NumberEntity(200, 220, 80)
        );

        GraphValues stats = new GraphValues();

        for (NumberEntity entry : mockData) {
            stats.totalMin += entry.getMinValue();
            stats.totalMax += entry.getMaxValue();
        }

        stats.computeAverages(mockData.size());

        assertEquals(115.0, stats.averageMin, 0.01);
        assertEquals(160.0, stats.averageMax, 0.01);
    }

    @Test
    public void testPointValuesGeneratedCorrectly() {
        List<NumberEntity> mockData = Arrays.asList(
                new NumberEntity(100, 150, 70),
                new NumberEntity(110, 160, 80)
        );

        GraphValues stats = new GraphValues();

        for (int i = 0; i < mockData.size(); i++) {
            stats.minValues.add(new PointValue(i, mockData.get(i).getMinValue()));
            stats.maxValues.add(new PointValue(i, mockData.get(i).getMaxValue()));
        }

        assertEquals(2, stats.minValues.size());
        assertEquals(2, stats.maxValues.size());
        assertEquals(100, stats.minValues.get(0).getY(), 0.01);
        assertEquals(160, stats.maxValues.get(1).getY(), 0.01);
    }

    @Test
    // diastolic in range = 90.0 <= diastolic <= 109.0
    // systolic in range = 140.0 <= systolic <= 180.0
    public void testMixedValuesWithPartialDismissedIndices() {
        List<NumberEntity> mockData = Arrays.asList(
                new NumberEntity(95, 190, 70),  // i = 0 — dismissed (systolic > 180)
                new NumberEntity(95, 160, 75),  // i = 1 — dismissed (systolic + diastolic in range)
                new NumberEntity(80, 150, 80), // i = 2 — dismissed (systolic in range)
                new NumberEntity(91, 135, 70),  // i = 3 — dismissed (diastolic in range)
                new NumberEntity(89, 145, 70),  // i = 4 — systolic in range
                new NumberEntity(95, 160, 75),  // i = 5 — systolic + diastolic in range
                new NumberEntity(85, 185, 90),  // i = 6 — systolic > 180
                new NumberEntity(80, 120, 70),  // i = 7 — normal
                new NumberEntity(85, 130, 70)   // i = 8 — normal
        );

        Set<Integer> dismissed = new HashSet<>(Arrays.asList(0, 1, 2, 3));
        Set<Integer> actualProcessed = new HashSet<>();
        Set<Integer> expectedProcessed = Set.of(4, 5, 6, 7, 8);
        GraphValues stats = new GraphValues();

        for (int i = 0; i < mockData.size(); i++) {
            if (dismissed.contains(i)) continue;

            actualProcessed.add(i);

            float min = mockData.get(i).getMinValue();
            float max = mockData.get(i).getMaxValue();

            if (max > 180.0) stats.systolicCountAbove180++;
            if (max >= 140.0 && max <= 180.0) stats.systolicCountInRange++;
            if (min >= 90.0 && min <= 109.0) stats.diastolicCountInRange++;
        }

        assertEquals(Set.of(0, 1, 2, 3), dismissed);
        assertEquals(expectedProcessed, actualProcessed);
        assertEquals(1, stats.systolicCountAbove180);     // index 6 analyzed (index 0 was dismissed)
        assertEquals(2, stats.systolicCountInRange);       // index 4 and 5 analyzed (index 1 and 2 dismissed )
        assertEquals(1, stats.diastolicCountInRange);      // index 4 and 5 analyzed (index 1 and 3 dismissed )
    }


}
