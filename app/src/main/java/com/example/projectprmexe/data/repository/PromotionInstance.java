package com.example.projectprmexe.data.repository;

import com.example.projectprmexe.data.api.PromotionAPI;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class PromotionInstance {
    private static PromotionAPI apiService;

    public static PromotionAPI getApiService() {s
        if (apiService == null) {
            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl("http://10.0.2.2:5150/") // Đổi thành baseUrl của bạn nếu khác
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
            apiService = retrofit.create(PromotionAPI.class);
        }
        return apiService;
    }
} 