package com.example.projectprmexe.models;

import com.google.gson.annotations.SerializedName;

public class UserRoleUpdateRequest {
    @SerializedName("userId")
    private int userId;
    
    @SerializedName("newRoleId")
    private int newRoleId;
    
    @SerializedName("reason")
    private String reason;

    public UserRoleUpdateRequest(int userId, int newRoleId, String reason) {
        this.userId = userId;
        this.newRoleId = newRoleId;
        this.reason = reason;
    }
}
