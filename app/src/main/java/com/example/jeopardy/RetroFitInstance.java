package com.example.jeopardy;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetroFitInstance {
    private static Retrofit retrofit = null;
    private static final String BASE_URL = "https://jservice.io";

    public static Retrofit getRetrofitInstance() {
        /*Gson builder = new GsonBuilder()
                .disableHtmlEscaping()
                .create();*/
        if(retrofit == null) {
            retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return retrofit;
    }
}
