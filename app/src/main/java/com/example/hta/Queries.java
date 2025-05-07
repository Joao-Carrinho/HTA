package com.example.hta;

public class Queries {
    public static final String SELECT_ALL_NUMBERS =
            "SELECT * FROM number_table ORDER BY timestamp ASC";

    public static final String SELECT_NUMBERS_BOX1 =
            "SELECT * FROM number_table WHERE minValue != 0 ORDER BY timestamp ASC";

    public static final String SELECT_NUMBERS_BOX2 =
            "SELECT * FROM number_table WHERE maxValue != 0 ORDER BY timestamp ASC";
}
