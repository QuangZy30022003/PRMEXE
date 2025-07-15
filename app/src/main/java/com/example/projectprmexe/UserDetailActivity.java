package com.example.projectprmexe;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class UserDetailActivity extends AppCompatActivity {
    private TextView tvUserName, tvUserEmail, tvUserPhone, tvUserAddress;
    private TextView tvUserGender, tvUserBirthDate, tvUserRole, tvUserStatus;
    private TextView tvUserCreatedAt;
    
    private AdminUserApi adminUserApi;
    private String authToken;
    private int userId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_detail);

        initViews();
        setupRetrofit();
        loadAuthToken();
        loadIntentData();
        loadUserDetails();
    }

    private void initViews() {
        tvUserName = findViewById(R.id.tvUserName);
        tvUserEmail = findViewById(R.id.tvUserEmail);
        tvUserPhone = findViewById(R.id.tvUserPhone);
        tvUserAddress = findViewById(R.id.tvUserAddress);
        tvUserGender = findViewById(R.id.tvUserGender);
        tvUserBirthDate = findViewById(R.id.tvUserBirthDate);
        tvUserRole = findViewById(R.id.tvUserRole);
        tvUserStatus = findViewById(R.id.tvUserStatus);
        tvUserCreatedAt = findViewById(R.id.tvUserCreatedAt);
    }

    private void setupRetrofit() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://10.0.2.2:5150/") // Updated to match API port
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        adminUserApi = retrofit.create(AdminUserApi.class);
    }

    private void loadAuthToken() {
        SharedPreferences prefs = getSharedPreferences("auth", MODE_PRIVATE);
        String token = prefs.getString("token", "");
        authToken = "Bearer " + token;
    }

    private void loadIntentData() {
        userId = getIntent().getIntExtra("userId", 0);
    }

    private void loadUserDetails() {
        Call<UserProfile> call = adminUserApi.getUserById(authToken, userId);
        call.enqueue(new Callback<UserProfile>() {
            @Override
            public void onResponse(Call<UserProfile> call, Response<UserProfile> response) {
                if (response.isSuccessful() && response.body() != null) {
                    UserProfile user = response.body();
                    displayUserDetails(user);
                } else {
                    Toast.makeText(UserDetailActivity.this, 
                        "Failed to load user details", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<UserProfile> call, Throwable t) {
                Toast.makeText(UserDetailActivity.this, 
                    "Network error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void displayUserDetails(UserProfile user) {
        tvUserName.setText(user.getFullName());
        tvUserEmail.setText(user.getEmail());
        tvUserPhone.setText(user.getPhone() != null ? user.getPhone() : "Chưa cập nhật");
        tvUserAddress.setText(user.getAddress() != null ? user.getAddress() : "Chưa cập nhật");
        tvUserGender.setText(user.getGender() != null ? user.getGender() : "Chưa cập nhật");
        tvUserBirthDate.setText(user.getBirthDate() != null ? user.getBirthDate() : "Chưa cập nhật");
        
        // Map role ID to role name
        String roleName = getRoleName(user.getRoleId());
        tvUserRole.setText(roleName);
        
        // Note: We don't have status and created date in UserProfile, 
        // so we'll show placeholder text
        tvUserStatus.setText("N/A");
        tvUserCreatedAt.setText("N/A");
    }

    private String getRoleName(int roleId) {
        switch (roleId) {
            case 1: return "User";
            case 2: return "Staff";
            case 3: return "Guest";
            case 4: return "Admin";
            default: return "Unknown";
        }
    }
}
