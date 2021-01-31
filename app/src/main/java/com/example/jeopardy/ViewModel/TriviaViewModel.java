package com.example.jeopardy.ViewModel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.example.jeopardy.Models.SpecificCategory;
import com.example.jeopardy.Repository.TriviaRepository;

import java.util.List;

public class TriviaViewModel extends AndroidViewModel {
    private TriviaRepository triviaRepository;
    private LiveData<List<Integer>> clueData;
    private LiveData<List<SpecificCategory>> categoryData;
    private LiveData<Boolean> complete;

    public TriviaViewModel(@NonNull Application application) {
        super(application);
        triviaRepository = new TriviaRepository();
        clueData = triviaRepository.getMutableClueIdData();
        categoryData = triviaRepository.getMutableCategoryData();
        complete = triviaRepository.getCallStatus();
    }

    public void initializeBoard(int count) {
        triviaRepository.getBoardClues(count);
    }

    public LiveData<List<Integer>> getClueData() {
        return clueData;
    }

    public LiveData<List<SpecificCategory>> getCategoryData() {
        return categoryData;
    }

    public LiveData<Boolean> getComplete() {
        return complete;
    }
}
