package com.example.jeopardy.ViewModel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.example.jeopardy.Models.SpecificCategory;
import com.example.jeopardy.Models.Random;
import com.example.jeopardy.Repository.TriviaRepository;

import java.util.List;

public class TriviaViewModel extends AndroidViewModel {
    private TriviaRepository triviaRepository;
    private LiveData<List<SpecificCategory>> categoryData;
    private LiveData<List<Random>> blitzData;

    public TriviaViewModel(@NonNull Application application) {
        super(application);
        triviaRepository = new TriviaRepository();
        categoryData = triviaRepository.getMutableCategoryData();
        blitzData = triviaRepository.getBlitzData();
    }

    public void initializeBoard(int count, int offset) {
        triviaRepository.getBoardClues(count, offset);
    }

    public void startBlitz(int count) {
        triviaRepository.getBlitzQuestions(count);
    }

    public LiveData<List<SpecificCategory>> getCategoryData() {
        return categoryData;
    }

    public LiveData<List<Random>> getBlitzData() {
        return blitzData;
    }
}
