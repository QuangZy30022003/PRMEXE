package com.example.projectprmexe;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.projectprmexe.models.UserStatisticsResponse;

import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class UserStatisticsActivity extends AppCompatActivity {
    private TextView tvTotalUsers, tvVerifiedUsers, tvUnverifiedUsers;
    private TextView tvNewUsers7Days, tvNewUsers30Days;
    private TextView tvActiveUsers7Days, tvActiveUsers30Days;
    private TextView tvUsersByRole;
    
    private AdminUserApi adminUserApi;
    private String authToken;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_statistics);

        initViews();
        setupRetrofit();
        loadAuthToken();
        loadStatistics();
    }

    private void initViews() {
        tvTotalUsers = findViewById(R.id.tvTotalUsers);
        tvVerifiedUsers = findViewById(R.id.tvVerifiedUsers);
        tvUnverifiedUsers = findViewById(R.id.tvUnverifiedUsers);
        tvNewUsers7Days = findViewById(R.id.tvNewUsers7Days);
        tvNewUsers30Days = findViewById(R.id.tvNewUsers30Days);
        tvActiveUsers7Days = findViewById(R.id.tvActiveUsers7Days);
        tvActiveUsers30Days = findViewById(R.id.tvActiveUsers30Days);
        tvUsersByRole = findViewById(R.id.tvUsersByRole);
    }

    private void setupRetrofit() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://10.0.2.2:5150/") // Adjust URL as needed
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        adminUserApi = retrofit.create(AdminUserApi.class);
    }

    private void loadAuthToken() {
        SharedPreferences prefs = getSharedPreferences("auth", MODE_PRIVATE);
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

    private void displayStatistics(UserStatisticsResponse.UserStatisticsData data) {
        tvTotalUsers.setText(String.valueOf(data.getTotalUsers()));
        tvVerifiedUsers.setText(String.valueOf(data.getVerifiedUsers()));
        tvUnverifiedUsers.setText(String.valueOf(data.getUnverifiedUsers()));
        tvNewUsers7Days.setText(String.valueOf(data.getNewUsersLast7Days()));
        tvNewUsers30Days.setText(String.valueOf(data.getNewUsersLast30Days()));
        tvActiveUsers7Days.setText(String.valueOf(data.getActiveUsersLast7Days()));
        tvActiveUsers30Days.setText(String.valueOf(data.getActiveUsersLast30Days()));

        // Display users by role
        StringBuilder roleStats = new StringBuilder();
        for (Map.Entry<String, Integer> entry : data.getUsersByRole().entrySet()) {
            roleStats.append(entry.getKey()).append(": ").append(entry.getValue()).append("\n");
        }
        tvUsersByRole.setText(roleStats.toString());
    }
}
