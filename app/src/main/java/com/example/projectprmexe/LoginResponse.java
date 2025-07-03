package com.example.projectprmexe;

import com.google.gson.annotations.SerializedName;

public class LoginResponse {
    @SerializedName("Token")
    private String token;

    @SerializedName("RefreshToken")
    private String refreshToken;

    private String message;

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getRefreshToken() {
        return refreshToken;
    }

    public void setRefreshToken(String refreshToken) {
        this.refreshToken = refreshToken;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
} 