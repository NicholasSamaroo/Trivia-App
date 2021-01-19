package com.example.jeopardy;

import hu.akarnokd.rxjava3.retrofit.RxJava3CallAdapterFactory;
import io.reactivex.rxjava3.schedulers.Schedulers;
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
                    .addCallAdapterFactory(RxJava3CallAdapterFactory.createWithScheduler(Schedulers.io()))
                    .build();
        }
        return retrofit;
    }
}
