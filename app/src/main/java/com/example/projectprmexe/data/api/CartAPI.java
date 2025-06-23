package com.example.projectprmexe.data.api;

import com.example.projectprmexe.data.model.Cart.CartItemCreateDTO;
import com.example.projectprmexe.data.model.Cart.CartItemResponseDTO;

import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface CartAPI {

    @GET("api/Cart")
    Call<List<CartItemResponseDTO>> getCart(@Header("Authorization") String token);

    @POST("api/Cart")
    Call<ResponseBody> addOrUpdateCart(@Body CartItemCreateDTO cartItem,
                                       @Header("Authorization") String token);

    @DELETE("api/Cart/{productId}")
    Call<ResponseBody> deleteCartItem(@Path("productId") int productId,
                                      @Header("Authorization") String token);
}
