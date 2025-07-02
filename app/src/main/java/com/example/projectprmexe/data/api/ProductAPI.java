package com.example.projectprmexe.data.api;

import com.example.projectprmexe.data.model.Product.ProductCreateUpdateDto;
import com.example.projectprmexe.data.model.Product.ProductDto;

import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface ProductAPI {

    @GET("api/Products")
    Call<List<ProductDto>> getAllProducts();

    @GET("api/Products/{id}")
    Call<ProductDto> getProductById(@Path("id") int id);

    @GET("api/Products/categoryId")
    Call<ResponseBody> getProductsByCategory(@Query("categoryId") int categoryId);

    @POST("api/Products")
    Call<ProductCreateUpdateDto> createProduct(@Body ProductCreateUpdateDto product,
                                               @Header("Authorization") String token);

    @PUT("api/Products/{id}")
    Call<ResponseBody> updateProduct(@Path("id") int id,
                                     @Body ProductCreateUpdateDto product,
                                     @Header("Authorization") String token);

    @DELETE("api/Products/{id}")
    Call<ResponseBody> deleteProduct(@Path("id") int id,
                                     @Header("Authorization") String token);

    // Testing versions without authorization (remove when auth is implemented)
    @POST("api/Products")
    Call<ProductCreateUpdateDto> createProduct(@Body ProductCreateUpdateDto product);

    @PUT("api/Products/{id}")
    Call<Void> updateProduct(@Path("id") int id, @Body ProductCreateUpdateDto product);

    @DELETE("api/Products/{id}")
    Call<Void> deleteProduct(@Path("id") int id);
}