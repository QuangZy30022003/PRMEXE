package com.example.projectprmexe.data.api;

import com.example.projectprmexe.data.model.Promotion.PromotionCreateUpdateDTO;
import com.example.projectprmexe.data.model.Promotion.PromotionDTO;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

public interface PromotionAPI {
    @GET("api/Promotion")
    Call<List<PromotionDTO>> getAll(@Header("Authorization") String token);

    @GET("api/Promotion/{id}")
    Call<PromotionDTO> getById(@Header("Authorization") String token, @Path("id") int id);

    @POST("api/Promotion")
    Call<PromotionDTO> create(@Header("Authorization") String token, @Body PromotionCreateUpdateDTO dto);

    @PUT("api/Promotion/{id}")
    Call<Void> update(@Header("Authorization") String token, @Path("id") int id, @Body PromotionCreateUpdateDTO dto);

    @DELETE("api/Promotion/{id}")
    Call<Void> delete(@Header("Authorization") String token, @Path("id") int id);
} 