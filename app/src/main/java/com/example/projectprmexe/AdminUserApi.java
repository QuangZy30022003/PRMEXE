package com.example.projectprmexe;

import com.example.projectprmexe.models.ApiResponse;
import com.example.projectprmexe.models.UserListResponse;
import com.example.projectprmexe.models.UserRoleUpdateRequest;
import com.example.projectprmexe.models.UserStatisticsResponse;

import java.util.Map;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import retrofit2.http.QueryMap;

public interface AdminUserApi {
    
    @GET("api/admin/user")
    Call<UserListResponse> getAllUsers(
        @Header("Authorization") String token,
        @QueryMap Map<String, String> filters
    );
    
    @GET("api/admin/user/{id}")
    Call<UserProfile> getUserById(
        @Header("Authorization") String token,
        @Path("id") int userId
    );
    
    @DELETE("api/admin/user/{id}")
    Call<ApiResponse> deleteUser(
        @Header("Authorization") String token,
        @Path("id") int userId
    );
    
    @PUT("api/admin/user/change-role")
    Call<ApiResponse> changeUserRole(
        @Header("Authorization") String token,
        @Body UserRoleUpdateRequest request
    );
    
    @GET("api/admin/user/statistics")
    Call<UserStatisticsResponse> getUserStatistics(
        @Header("Authorization") String token
    );
}
