package com.example.projectprmexe.data.repository;
import com.example.projectprmexe.data.api.CartAPI;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class CartInstance {
    private static final String BASE_URL = "http://10.0.2.2:7262/";

    private static Retrofit retrofit;

    public static CartAPI getApiService() {
        if (retrofit == null) {
            retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return retrofit.create(CartAPI.class);
    }
}
