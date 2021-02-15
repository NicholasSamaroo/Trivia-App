package com.example.jeopardy.Repository;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.jeopardy.JServiceAPI;
import com.example.jeopardy.Models.CategoryObject;
import com.example.jeopardy.Models.Random;
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
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TriviaRepository {
    private JServiceAPI jServiceAPI;
    private List<SpecificCategory> categoryData;
    private MutableLiveData<List<SpecificCategory>> mutableCategoryData;
    private MutableLiveData<List<Random>> blitzData;

    public TriviaRepository() {
        categoryData = new ArrayList<>();
        mutableCategoryData = new MutableLiveData<>();
        blitzData = new MutableLiveData<>();
        jServiceAPI = RetroFitInstance.getRetrofitInstance().create(JServiceAPI.class);
    }

    public void getBoardClues(int count, int offset) {
        jServiceAPI.getCategories(count, offset)
                .flatMapIterable(categoryResponseList -> categoryResponseList)
                .flatMap(new Function<CategoryObject, ObservableSource<SpecificCategory>>() {
                    @Override
                    public ObservableSource<SpecificCategory> apply(CategoryObject categoryObject) throws Throwable {
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
                        mutableCategoryData.setValue(categoryData);
                    }
                });
    }

    public void getBlitzQuestions(int count) {
        jServiceAPI.getQuestions(count).enqueue(new Callback<List<Random>>() {
            @Override
            public void onResponse(Call<List<Random>> call, Response<List<Random>> response) {
                if(response.isSuccessful()) {
                    blitzData.setValue(response.body());
                }
            }

            @Override
            public void onFailure(Call<List<Random>> call, Throwable t) {

            }
        });

    }

    public LiveData<List<SpecificCategory>> getMutableCategoryData() {
        return mutableCategoryData;
    }

    public LiveData<List<Random>> getBlitzData() {
        return blitzData;
    }
}
