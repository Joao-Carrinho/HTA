package com.example.hta;

import android.app.Application;
import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.room.Room;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class AddNumberViewModel extends AndroidViewModel {

    private final NumberDao numberDao;
    private final LiveData<List<NumberEntity>> allNumbers;
    private final LiveData<List<NumberEntity>> allNumbersBox1;
    private final LiveData<List<NumberEntity>> allNumbersBox2;
    private final ExecutorService executorService;

    public AddNumberViewModel(@NonNull Application application) {
        super(application);
        AppDatabase db = Room.databaseBuilder(application, AppDatabase.class, "number_database")
                .fallbackToDestructiveMigration()
                .build();
        numberDao = db.numberDao();
        allNumbers = numberDao.getAllNumbers();
        allNumbersBox1 = numberDao.getAllNumbersBox1();
        allNumbersBox2 = numberDao.getAllNumbersBox2();
        executorService = Executors.newSingleThreadExecutor();
    }

    public LiveData<List<NumberEntity>> getAllNumbers() {
        return allNumbers;
    }

    public LiveData<List<NumberEntity>> getNumbersBox1() {
        return allNumbersBox1;
    }

    public LiveData<List<NumberEntity>> getNumbersBox2() {
        return allNumbersBox2;
    }

    public void addNumber(float minValue, float maxValue, int bpm) {
        executorService.execute(() -> {
            NumberEntity number = new NumberEntity(minValue, maxValue, bpm);
            numberDao.insert(number);
        });
    }
}
