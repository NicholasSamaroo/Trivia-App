package com.example.jeopardy.Repository;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.jeopardy.JServiceAPI;
import com.example.jeopardy.Models.CategoryObject;
import com.example.jeopardy.Models.SpecificCategory;
import com.example.jeopardy.RetroFitInstance;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.annotations.NonNull;
import io.reactivex.rxjava3.core.ObservableSource;
import io.reactivex.rxjava3.core.Observer;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.functions.Function;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class TriviaRepository {
    private JServiceAPI jServiceAPI;
    private List<Integer> clueIdData;
    private List<SpecificCategory> categoryData;
    private MutableLiveData<List<Integer>> mutableClueIdData;
    private MutableLiveData<List<SpecificCategory>> mutableCategoryData;
    private MutableLiveData<Boolean> status;

    public TriviaRepository() {
        clueIdData = new ArrayList<>();
        categoryData = new ArrayList<>();
        mutableClueIdData = new MutableLiveData<>();
        mutableCategoryData = new MutableLiveData<>();
        status = new MutableLiveData<>();
        jServiceAPI = RetroFitInstance.getRetrofitInstance().create(JServiceAPI.class);
    }

    public void getBoardClues(int count) {
        jServiceAPI.getCategories(count)
                .flatMapIterable(categoryResponseList -> categoryResponseList) // converts your list of movieResponse into an observable which emits one movieResponse object at a time.
                .flatMap(new Function<CategoryObject, ObservableSource<SpecificCategory>>() {
                    @Override
                    public ObservableSource<SpecificCategory> apply(CategoryObject categoryObject) throws Throwable {
                        clueIdData.add(categoryObject.getId());
                        return jServiceAPI.getClues(categoryObject.getId());
                    }
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<SpecificCategory>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {

                    }

                    @Override
                    public void onNext(@NonNull SpecificCategory specificCategory) {
                        categoryData.add(specificCategory);
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {

                    }

                    @Override
                    public void onComplete() {
                        // Maybe have some sort of message here
                        // Board initialized, good luck!
                        mutableClueIdData.setValue(clueIdData);
                        mutableCategoryData.setValue(categoryData);
                        status.setValue(true);

                    }
                });
    }

    public LiveData<List<Integer>> getMutableClueIdData() {
        return mutableClueIdData;
    }

    public LiveData<List<SpecificCategory>> getMutableCategoryData() {
        return mutableCategoryData;
    }

    public LiveData<Boolean> getCallStatus() {
        return status;
    }
}
