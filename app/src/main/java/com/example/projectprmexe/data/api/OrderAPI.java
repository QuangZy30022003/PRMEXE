package com.example.projectprmexe.data.api;

import com.example.projectprmexe.data.model.Cart.OrderFromCartDTO;
import com.example.projectprmexe.data.model.Cart.OrderResponse;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Header;
import retrofit2.http.POST;

public interface OrderAPI {
    @POST("api/Orders/from-cart")
    Call<OrderResponse> createOrderFromCart(
        @Header("Authorization") String bearerToken,
        @Body OrderFromCartDTO dto
    );
} 