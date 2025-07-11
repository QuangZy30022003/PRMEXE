package com.example.projectprmexe;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.PUT;

public interface AuthApi {
    @POST("api/Auth/login")
    Call<LoginResponse> login(@Body LoginRequest request);

    @POST("api/Auth/register")
    Call<LoginResponse> register(@Body RegisterRequest request);

    @GET("api/Auth/profile")
    Call<UserProfile> getProfile(@Header("Authorization") String token);

    @PUT("api/Auth/update-profile")
    Call<String> updateProfile(@Header("Authorization") String token, @Body UpdateProfileRequest request);
} 