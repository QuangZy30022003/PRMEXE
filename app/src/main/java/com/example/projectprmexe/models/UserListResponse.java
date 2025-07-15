package com.example.projectprmexe.models;

import com.google.gson.annotations.SerializedName;
import java.util.List;

public class UserListResponse {
    @SerializedName("success")
    private boolean success;
    
    @SerializedName("data")
    private UserListData data;
    
    @SerializedName("message")
    private String message;

    public boolean isSuccess() {
        return success;
    }

    public UserListData getData() {
        return data;
    }

    public String getMessage() {
        return message;
    }

    public static class UserListData {
        @SerializedName("users")
        private List<UserItem> users;
        
        @SerializedName("totalCount")
        private int totalCount;
        
        @SerializedName("pageNumber")
        private int pageNumber;
        
        @SerializedName("pageSize")
        private int pageSize;
        
        @SerializedName("totalPages")
        private int totalPages;

        public List<UserItem> getUsers() {
            return users;
        }

        public int getTotalCount() {
            return totalCount;
        }

        public int getPageNumber() {
            return pageNumber;
        }

        public int getPageSize() {
            return pageSize;
        }

        public int getTotalPages() {
            return totalPages;
        }
    }

    public static class UserItem {
        @SerializedName("userId")
        private int userId;
        
        @SerializedName("fullName")
        private String fullName;
        
        @SerializedName("email")
        private String email;
        
        @SerializedName("phone")
        private String phone;
        
        @SerializedName("address")
        private String address;
        
        @SerializedName("gender")
        private String gender;
        
        @SerializedName("birthDate")
        private String birthDate;
        
        @SerializedName("roleId")
        private int roleId;
        
        @SerializedName("roleName")
        private String roleName;
        
        @SerializedName("createdAt")
        private String createdAt;
        
        @SerializedName("isEmailConfirmed")
        private boolean isEmailConfirmed;

        public int getUserId() {
            return userId;
        }

        public String getFullName() {
            return fullName;
        }

        public String getEmail() {
            return email;
        }

        public String getPhone() {
            return phone;
        }

        public String getAddress() {
            return address;
        }

        public String getGender() {
            return gender;
        }

        public String getBirthDate() {
            return birthDate;
        }

        public int getRoleId() {
            return roleId;
        }

        public String getRoleName() {
            return roleName;
        }

        public String getCreatedAt() {
            return createdAt;
        }

        public boolean isEmailConfirmed() {
            return isEmailConfirmed;
        }
    }
}
