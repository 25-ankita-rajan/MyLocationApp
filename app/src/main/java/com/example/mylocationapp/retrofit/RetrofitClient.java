package com.example.mylocationapp.retrofit;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitClient {
  //  public static final String BASE_URL = "https://api.openweathermap.org/data/2.5/";
    private static Retrofit instance;
   // private static OkHttpClient okHttpClient;

    public static Retrofit getInstance()
    {
        if(instance==null)
           instance =new Retrofit.Builder()
                   .baseUrl("https://api.openweathermap.org/data/2.5/")
                   //.client(okHttpClient)
                   .addConverterFactory(GsonConverterFactory.create())
                   .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                   .build();
        return instance;
    }
}
