package com.example.projectprmexe;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.projectprmexe.adapters.UserListAdapter;
import com.example.projectprmexe.models.UserListResponse;

import java.util.HashMap;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class UserManagementActivity extends AppCompatActivity {
    private RecyclerView recyclerViewUsers;
    private UserListAdapter userAdapter;
    private Button btnStatistics, btnRefresh, btnExportData;
    private AdminUserApi adminUserApi;
    private String authToken;
    
    private int currentPage = 1;
    private final int pageSize = 10;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_management);

        initViews();
        setupRetrofit();
        loadAuthToken();
        loadUsers();

        // Check if we should show role management focus
        boolean showRoleManagement = getIntent().getBooleanExtra("show_role_management", false);
        if (showRoleManagement) {
            // Show a toast or highlight role management features
            Toast.makeText(this, "🔄 Role Management Mode - Click on users to change their roles", Toast.LENGTH_LONG).show();
        }
    }

    private void initViews() {
        recyclerViewUsers = findViewById(R.id.recyclerViewUsers);
        btnStatistics = findViewById(R.id.btnStatistics);
        btnRefresh = findViewById(R.id.btnRefresh);
        btnExportData = findViewById(R.id.btnExportData);

        recyclerViewUsers.setLayoutManager(new LinearLayoutManager(this));
        
        btnStatistics.setOnClickListener(v -> {
            Intent intent = new Intent(this, UserStatisticsActivity.class);
            startActivity(intent);
        });
        
        btnRefresh.setOnClickListener(v -> {
            currentPage = 1;
            loadUsers();
        });

        btnExportData.setOnClickListener(v -> {
            showExportDialog();
        });
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

    private void loadUsers() {
        Map<String, String> filters = new HashMap<>();
        filters.put("pageNumber", String.valueOf(currentPage));
        filters.put("pageSize", String.valueOf(pageSize));

        Call<UserListResponse> call = adminUserApi.getAllUsers(authToken, filters);
        call.enqueue(new Callback<UserListResponse>() {
            @Override
            public void onResponse(Call<UserListResponse> call, Response<UserListResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    UserListResponse userResponse = response.body();
                    if (userResponse.isSuccess()) {
                        setupUserAdapter(userResponse.getData());
                    } else {
                        Toast.makeText(UserManagementActivity.this, 
                            userResponse.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(UserManagementActivity.this, 
                        "Failed to load users", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<UserListResponse> call, Throwable t) {
                Toast.makeText(UserManagementActivity.this, 
                    "Network error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void setupUserAdapter(UserListResponse.UserListData data) {
        if (userAdapter == null) {
            userAdapter = new UserListAdapter(data.getUsers(), new UserListAdapter.OnUserActionListener() {
                @Override
                public void onDeleteUser(int userId) {
                    deleteUser(userId);
                }

                @Override
                public void onChangeRole(int userId, String currentRole) {
                    showRoleChangeDialog(userId, currentRole);
                }

                @Override
                public void onViewUser(int userId) {
                    // Navigate to user details
                    Intent intent = new Intent(UserManagementActivity.this, UserDetailActivity.class);
                    intent.putExtra("userId", userId);
                    startActivity(intent);
                }
            });
            recyclerViewUsers.setAdapter(userAdapter);
        } else {
            userAdapter.updateUsers(data.getUsers());
        }
    }

    private void deleteUser(int userId) {
        Call<com.example.projectprmexe.models.ApiResponse> call = adminUserApi.deleteUser(authToken, userId);
        call.enqueue(new Callback<com.example.projectprmexe.models.ApiResponse>() {
            @Override
            public void onResponse(Call<com.example.projectprmexe.models.ApiResponse> call, 
                                 Response<com.example.projectprmexe.models.ApiResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    com.example.projectprmexe.models.ApiResponse apiResponse = response.body();
                    if (apiResponse.isSuccess()) {
                        Toast.makeText(UserManagementActivity.this, 
                            apiResponse.getMessage(), Toast.LENGTH_SHORT).show();
                        loadUsers(); // Refresh the list
                    } else {
                        Toast.makeText(UserManagementActivity.this, 
                            apiResponse.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(UserManagementActivity.this, 
                        "Failed to delete user", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<com.example.projectprmexe.models.ApiResponse> call, Throwable t) {
                Toast.makeText(UserManagementActivity.this, 
                    "Network error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void showRoleChangeDialog(int userId, String currentRole) {
        // This will be implemented with a dialog to select new role
        Intent intent = new Intent(this, ChangeRoleActivity.class);
        intent.putExtra("userId", userId);
        intent.putExtra("currentRole", currentRole);
        startActivityForResult(intent, 100);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 100 && resultCode == RESULT_OK) {
            // Role changed successfully, refresh the list
            loadUsers();
        }
    }

    private void showExportDialog() {
        android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(this);
        builder.setTitle("📤 Export User Data");

        String[] exportOptions = {
            "📊 Export All Users (CSV)",
            "📈 Export User Statistics",
            "🔄 Export Role Distribution",
            "📅 Export Recent Activity"
        };

        builder.setItems(exportOptions, (dialog, which) -> {
            switch (which) {
                case 0:
                    exportUserData();
                    break;
                case 1:
                    exportStatistics();
                    break;
                case 2:
                    exportRoleDistribution();
                    break;
                case 3:
                    exportRecentActivity();
                    break;
            }
        });

        builder.setNegativeButton("❌ Cancel", null);
        builder.show();
    }

    private void exportUserData() {
        Toast.makeText(this, "📊 Exporting user data... (Feature coming soon)", Toast.LENGTH_SHORT).show();
        // TODO: Implement CSV export functionality
    }

    private void exportStatistics() {
        Toast.makeText(this, "📈 Exporting statistics... (Feature coming soon)", Toast.LENGTH_SHORT).show();
        // TODO: Implement statistics export
    }

    private void exportRoleDistribution() {
        Toast.makeText(this, "🔄 Exporting role distribution... (Feature coming soon)", Toast.LENGTH_SHORT).show();
        // TODO: Implement role distribution export
    }

    private void exportRecentActivity() {
        Toast.makeText(this, "📅 Exporting recent activity... (Feature coming soon)", Toast.LENGTH_SHORT).show();
        // TODO: Implement recent activity export
    }
}
