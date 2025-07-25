package com.example.projectprmexe;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.projectprmexe.models.ApiResponse;
import com.example.projectprmexe.models.UserRoleUpdateRequest;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ChangeRoleActivity extends AppCompatActivity {
    private TextView tvCurrentRole;
    private Spinner spinnerNewRole;
    private EditText etReason;
    private Button btnChangeRole, btnCancel;
    
    private AdminUserApi adminUserApi;
    private String authToken;
    private int userId;
    private String currentRole;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_role);

        initViews();
        setupRetrofit();
        loadAuthToken();
        loadIntentData();
        setupSpinner();
        setupClickListeners();
    }

    private void initViews() {
        tvCurrentRole = findViewById(R.id.tvCurrentRole);
        spinnerNewRole = findViewById(R.id.spinnerNewRole);
        etReason = findViewById(R.id.etReason);
        btnChangeRole = findViewById(R.id.btnChangeRole);
        btnCancel = findViewById(R.id.btnCancel);
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

    private void loadIntentData() {
        userId = getIntent().getIntExtra("userId", 0);
        currentRole = getIntent().getStringExtra("currentRole");
        
        tvCurrentRole.setText("Vai trò hiện tại: " + currentRole);
    }

    private void setupSpinner() {
        String[] roles = {"User", "Staff", "Guest", "Admin"};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, 
            android.R.layout.simple_spinner_item, roles);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerNewRole.setAdapter(adapter);
    }

    private void setupClickListeners() {
        btnChangeRole.setOnClickListener(v -> changeUserRole());
        btnCancel.setOnClickListener(v -> finish());
    }

    private void changeUserRole() {
        String selectedRole = spinnerNewRole.getSelectedItem().toString();
        String reason = etReason.getText().toString().trim();
        
        // Map role names to role IDs
        int newRoleId = getRoleId(selectedRole);
        
        if (newRoleId == 0) {
            Toast.makeText(this, "Invalid role selected", Toast.LENGTH_SHORT).show();
            return;
        }

        UserRoleUpdateRequest request = new UserRoleUpdateRequest(userId, newRoleId, reason);
        
        Call<ApiResponse> call = adminUserApi.changeUserRole(authToken, request);
        call.enqueue(new Callback<ApiResponse>() {
            @Override
            public void onResponse(Call<ApiResponse> call, Response<ApiResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    ApiResponse apiResponse = response.body();
                    if (apiResponse.isSuccess()) {
                        Toast.makeText(ChangeRoleActivity.this, 
                            apiResponse.getMessage(), Toast.LENGTH_SHORT).show();
                        setResult(RESULT_OK);
                        finish();
                    } else {
                        Toast.makeText(ChangeRoleActivity.this, 
                            apiResponse.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(ChangeRoleActivity.this, 
                        "Failed to change role", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ApiResponse> call, Throwable t) {
                Toast.makeText(ChangeRoleActivity.this, 
                    "Network error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private int getRoleId(String roleName) {
        switch (roleName) {
            case "User": return 1;
            case "Staff": return 2;
            case "Guest": return 3;
            case "Admin": return 4;
            default: return 0;
        }
    }
}
