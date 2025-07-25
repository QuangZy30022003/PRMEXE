package com.example.projectprmexe;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.projectprmexe.models.UserStatisticsResponse;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import android.util.Log;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class UserStatisticsActivity extends AppCompatActivity {
    private TextView tvTotalUsers, tvVerifiedUsers, tvUnverifiedUsers;
    // Bỏ tvActiveUsers7Days, tvActiveUsers30Days
    
    private AdminUserApi adminUserApi;
    private String authToken;
    private int totalUsersCount = 0;
    private int confirmedUsersCount = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_statistics);

        initViews();
        setupRetrofit();
        loadAuthToken();
        loadTotalUsersFromAllUsers();
        loadConfirmedUsers();
    }

    private void initViews() {
        tvTotalUsers = findViewById(R.id.tvTotalUsers);
        tvVerifiedUsers = findViewById(R.id.tvVerifiedUsers);
        tvUnverifiedUsers = findViewById(R.id.tvUnverifiedUsers);
        // Đã xóa tvNewUsers7Days, tvNewUsers30Days
    }

    private void setupRetrofit() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://10.0.2.2:5150/") // Adjust URL as needed
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        adminUserApi = retrofit.create(AdminUserApi.class);
    }

    private void loadAuthToken() {
        SharedPreferences prefs = getSharedPreferences("MyPrefs", MODE_PRIVATE);
        String token = prefs.getString("token", "");
        authToken = "Bearer " + token;
    }

    private void loadStatistics() {
        Call<UserStatisticsResponse> call = adminUserApi.getUserStatistics(authToken);
        call.enqueue(new Callback<UserStatisticsResponse>() {
            @Override
            public void onResponse(Call<UserStatisticsResponse> call, Response<UserStatisticsResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    UserStatisticsResponse statsResponse = response.body();
                    if (statsResponse.isSuccess()) {
                        displayStatistics(statsResponse.getData());
                    } else {
                        Toast.makeText(UserStatisticsActivity.this, 
                            statsResponse.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(UserStatisticsActivity.this, 
                        "Failed to load statistics", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<UserStatisticsResponse> call, Throwable t) {
                Toast.makeText(UserStatisticsActivity.this, 
                    "Network error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void loadTotalUsersFromAllUsers() {
        retrofit2.Call<List<UserProfile>> call = adminUserApi.getAllUsersRaw(authToken);
        call.enqueue(new retrofit2.Callback<List<UserProfile>>() {
            @Override
            public void onResponse(retrofit2.Call<List<UserProfile>> call, retrofit2.Response<List<UserProfile>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    List<UserProfile> users = response.body();
                    totalUsersCount = users.size();
                    tvTotalUsers.setText(String.valueOf(totalUsersCount));
                    // Sau khi có tổng, cập nhật lại số chưa xác thực
                    updateUnverifiedUsers();
                    // Cập nhật số người dùng mới
                    // Xóa các đoạn code sử dụng tvNewUsers7Days, tvNewUsers30Days và hàm updateNewUsers
                }
            }
            @Override
            public void onFailure(retrofit2.Call<List<UserProfile>> call, Throwable t) {
                // Không làm gì, giữ nguyên giá trị cũ
            }
        });
    }

    private void loadConfirmedUsers() {
        retrofit2.Call<List<UserProfile>> call = adminUserApi.getConfirmedUsers(authToken);
        call.enqueue(new retrofit2.Callback<List<UserProfile>>() {
            @Override
            public void onResponse(retrofit2.Call<List<UserProfile>> call, retrofit2.Response<List<UserProfile>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    List<UserProfile> users = response.body();
                    confirmedUsersCount = users.size();
                    tvVerifiedUsers.setText(String.valueOf(confirmedUsersCount));
                    // Sau khi có số đã xác thực, cập nhật lại số chưa xác thực
                    updateUnverifiedUsers();
                }
            }
            @Override
            public void onFailure(retrofit2.Call<List<UserProfile>> call, Throwable t) {
                // Không làm gì, giữ nguyên giá trị cũ
            }
        });
    }

    private void updateUnverifiedUsers() {
        int unverified = totalUsersCount - confirmedUsersCount;
        if (unverified < 0) unverified = 0;
        tvUnverifiedUsers.setText(String.valueOf(unverified));
    }

    // Bỏ hàm loadUsersByRole và getRoleName

    private void displayStatistics(UserStatisticsResponse.UserStatisticsData data) {
        tvTotalUsers.setText(String.valueOf(data.getTotalUsers()));
        tvVerifiedUsers.setText(String.valueOf(data.getVerifiedUsers()));
        tvUnverifiedUsers.setText(String.valueOf(data.getUnverifiedUsers()));
        // Bỏ tvActiveUsers7Days, tvActiveUsers30Days

        // Display users by role
        StringBuilder roleStats = new StringBuilder();
        for (Map.Entry<String, Integer> entry : data.getUsersByRole().entrySet()) {
            roleStats.append(entry.getKey()).append(": ").append(entry.getValue()).append("\n");
        }
        // tvUsersByRole.setText(roleStats.toString()); // Bỏ phần này
    }
}
