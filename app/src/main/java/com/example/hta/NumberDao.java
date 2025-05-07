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

    @Query(Queries.SELECT_NUMBERS_BOX1)
    LiveData<List<NumberEntity>> getAllNumbersBox1();

    @Query(Queries.SELECT_NUMBERS_BOX2)
    LiveData<List<NumberEntity>> getAllNumbersBox2();

    @Query(Queries.SELECT_ALL_NUMBERS)
    LiveData<List<NumberEntity>> getAllNumbers();
}
