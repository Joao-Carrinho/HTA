
package com.example.hta;

import org.junit.Test;
import static org.junit.Assert.*;

public class InputFragmentTest {

    private boolean hasEmptyFields(String s1, String s2, String s3){
        return s1.isEmpty() || s2.isEmpty() || s3.isEmpty();
    }

    @Test
    public void testOneFieldEmpty() {
        assertTrue(hasEmptyFields("120", "", "70"));
    }

    @Test
    public void testTwoFieldsEmpty() {
        assertTrue(hasEmptyFields("", "", "75"));
    }

    @Test
    public void testNoFieldsEmpty() {
        assertFalse(hasEmptyFields("120", "80", "75"));
    }

    @Test
    public void testValidSystolicRange() {
        int validValue = 120;
        assertTrue(validValue >= 30 && validValue <= 230);
    }

    @Test
    public void testInvalidSystolicTooLow() {
        int invalidValue = 10;
        assertFalse(invalidValue >= 30 && invalidValue <= 230);
    }

    @Test
    public void testInvalidSystolicTooHigh() {
        int invalidValue = 240;
        assertFalse(invalidValue >= 30 && invalidValue <= 230);


    }

    @Test
    public void testNegativeBpm() {
        int bpm = -40;
        assertFalse(bpm >= 30 && bpm <= 200);
    }

    @Test
    public void testSystolicBoundaryInclusive() {
        int systolic = 30;
        assertTrue(systolic >= 30 && systolic <= 230);
    }

    @Test
    public void testDecimalWithCommaFails() {
        try {
            Float.parseFloat("120,5");
            fail("Should accept only with '.' as decimal separator");
        } catch (NumberFormatException e) {
            assertTrue(true);
        }
    }

    @Test
    public void testNumberFormatException() {
        try {
            Integer.parseInt("abc");
            fail("Should have thrown NumberFormatException");
        } catch (NumberFormatException e) {
            assertTrue(true);
        }
    }
}
