package com.example.projectprmexe.models;

import com.google.gson.annotations.SerializedName;
import java.util.List;
import java.util.Map;

public class UserStatisticsResponse {
    @SerializedName("success")
    private boolean success;
    
    @SerializedName("data")
    private UserStatisticsData data;
    
    @SerializedName("message")
    private String message;

    public boolean isSuccess() {
        return success;
    }

    public UserStatisticsData getData() {
        return data;
    }

    public String getMessage() {
        return message;
    }

    public static class UserStatisticsData {
        @SerializedName("totalUsers")
        private int totalUsers;
        
        @SerializedName("verifiedUsers")
        private int verifiedUsers;
        
        @SerializedName("unverifiedUsers")
        private int unverifiedUsers;
        
        @SerializedName("usersByRole")
        private Map<String, Integer> usersByRole;
        
        @SerializedName("newUsersLast7Days")
        private int newUsersLast7Days;
        
        @SerializedName("newUsersLast30Days")
        private int newUsersLast30Days;
        
        @SerializedName("activeUsersLast7Days")
        private int activeUsersLast7Days;
        
        @SerializedName("activeUsersLast30Days")
        private int activeUsersLast30Days;
        
        @SerializedName("monthlyRegistrations")
        private List<MonthlyRegistration> monthlyRegistrations;

        public int getTotalUsers() {
            return totalUsers;
        }

        public int getVerifiedUsers() {
            return verifiedUsers;
        }

        public int getUnverifiedUsers() {
            return unverifiedUsers;
        }

        public Map<String, Integer> getUsersByRole() {
            return usersByRole;
        }

        public int getNewUsersLast7Days() {
            return newUsersLast7Days;
        }

        public int getNewUsersLast30Days() {
            return newUsersLast30Days;
        }

        public int getActiveUsersLast7Days() {
            return activeUsersLast7Days;
        }

        public int getActiveUsersLast30Days() {
            return activeUsersLast30Days;
        }

        public List<MonthlyRegistration> getMonthlyRegistrations() {
            return monthlyRegistrations;
        }
    }

    public static class MonthlyRegistration {
        @SerializedName("month")
        private String month;
        
        @SerializedName("year")
        private int year;
        
        @SerializedName("count")
        private int count;

        public String getMonth() {
            return month;
        }

        public int getYear() {
            return year;
        }

        public int getCount() {
            return count;
        }
    }
}
