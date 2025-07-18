package com.example.projectprmexe.data.repository;

import com.example.projectprmexe.data.api.ProductAPI;
import com.example.projectprmexe.BuildConfig;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ProductInstance {
    private static Retrofit retrofit;
    private static final String BASE_URL = "http://10.0.2.2:5150/";

    public static ProductAPI getApiService() {
        if (retrofit == null) {
            retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return retrofit.create(ProductAPI.class);
    }
}