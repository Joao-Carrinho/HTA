package com.example.hta;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import java.util.List;

@Dao
public interface NumberDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(NumberEntity number);

    @Query("SELECT * FROM number_table WHERE minValue != 0 ORDER BY timestamp ASC")
    LiveData<List<NumberEntity>> getAllNumbersBox1();

    @Query("SELECT * FROM number_table WHERE maxValue != 0 ORDER BY timestamp ASC")
    LiveData<List<NumberEntity>> getAllNumbersBox2();

    @Query("SELECT * FROM number_table ORDER BY timestamp ASC")
    LiveData<List<NumberEntity>> getAllNumbers();
}
