package com.example.jeopardy;

import com.example.jeopardy.Models.TriviaWrapper;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface JServiceAPI {

    @GET("/api/random")
    Call<List<TriviaWrapper>> getQuestions(@Query("count") int count);
}
