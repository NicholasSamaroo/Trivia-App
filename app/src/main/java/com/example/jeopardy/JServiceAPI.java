package com.example.jeopardy;

import com.example.jeopardy.Models.CategoryObject;
import com.example.jeopardy.Models.SpecificCategory;
import com.example.jeopardy.Models.TriviaWrapper;

import java.util.List;

import io.reactivex.rxjava3.core.Observable;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface JServiceAPI {

    @GET("/api/random")
    Call<List<TriviaWrapper>> getQuestions(@Query("count") int count);

    @GET("/api/categories")
    Observable<List<CategoryObject>> getCategories(@Query("count") int count);

    @GET("/api/category")
    Observable<SpecificCategory> getClues(@Query("id") int id);
}
