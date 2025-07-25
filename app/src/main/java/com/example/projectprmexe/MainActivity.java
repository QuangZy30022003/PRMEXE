package com.example.projectprmexe;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;

import com.example.projectprmexe.ui.CartActivity;
import com.example.projectprmexe.ui.ProductListActivity;
import com.example.projectprmexe.ui.ProductManagementActivity;
import com.example.projectprmexe.ui.PromotionCreateActivity;
import android.widget.LinearLayout;
import com.example.projectprmexe.ui.PaymentActivity;

public class MainActivity extends AppCompatActivity {

    private Button btnShowProducts, btnLogin, btnRegister, btnCart, btnLogout, btnProfile;
    private Button btnProductManagement, btnCreatePromotion, btnUserManagement;
    private Button btnUserStatistics;
    private Button btnViewPayment;
    private LinearLayout adminPanel;
    private TextView tvUserStatus;
    private ActivityResultLauncher<Intent> loginLauncher;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        loginLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == RESULT_OK) {
                        updateUIBasedOnLoginStatus();
                    }
                }
        );

        initViews();
        setupClickListeners();
    }

    private void initViews() {
        btnShowProducts = findViewById(R.id.btnShowProducts);
        btnLogin = findViewById(R.id.btnLogin);
        btnRegister = findViewById(R.id.btnRegister);
        btnCart = findViewById(R.id.btnCart);
        btnLogout = findViewById(R.id.btnLogout);
        btnProfile = findViewById(R.id.btnProfile);
        btnProductManagement = findViewById(R.id.btnProductManagement);
        btnCreatePromotion = findViewById(R.id.btnCreatePromotion);
        btnUserManagement = findViewById(R.id.btnUserManagement);
        btnUserStatistics = findViewById(R.id.btnUserStatistics);
        btnViewPayment = findViewById(R.id.btnViewPayment);
        adminPanel = findViewById(R.id.adminPanel);
        tvUserStatus = findViewById(R.id.tvUserStatus);

        if (btnProductManagement != null) btnProductManagement.setVisibility(android.view.View.GONE);
        if (btnCreatePromotion != null) btnCreatePromotion.setVisibility(android.view.View.GONE);
        if (btnUserManagement != null) btnUserManagement.setVisibility(android.view.View.GONE);

        updateUIBasedOnLoginStatus();
    }

    private void setupClickListeners() {
        btnShowProducts.setOnClickListener(v -> {
            SharedPreferences prefs = getSharedPreferences("MyPrefs", MODE_PRIVATE);
            String token = prefs.getString("token", null);

            if (token != null && !token.isEmpty()) {
                Intent intent = new Intent(MainActivity.this, ProductListActivity.class);
                startActivity(intent);
            } else {
                Toast.makeText(MainActivity.this, "Vui lòng đăng nhập để xem sản phẩm!", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                loginLauncher.launch(intent);
            }
        });

        btnLogin.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, LoginActivity.class);
            loginLauncher.launch(intent);
        });

        btnRegister.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, RegisterActivity.class);
            startActivity(intent);
        });

        btnCart.setOnClickListener(v -> {
            SharedPreferences prefs = getSharedPreferences("MyPrefs", MODE_PRIVATE);
            String token = prefs.getString("token", null);

            if (token != null && !token.isEmpty()) {
                Intent intent = new Intent(MainActivity.this, CartActivity.class);
                startActivity(intent);
            } else {
                Toast.makeText(MainActivity.this, "Vui lòng đăng nhập để xem giỏ hàng!", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                startActivity(intent);
            }
        });

        btnProfile.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, ProfileActivity.class);
            startActivity(intent);
        });

        btnLogout.setOnClickListener(v -> {
            SharedPreferences prefs = getSharedPreferences("MyPrefs", MODE_PRIVATE);
            prefs.edit().remove("token").remove("role").apply();
            Toast.makeText(MainActivity.this, "Đã đăng xuất thành công!", Toast.LENGTH_SHORT).show();
            updateUIBasedOnLoginStatus();
        });

        if (btnProductManagement != null) {
            btnProductManagement.setOnClickListener(v -> {
                Intent intent = new Intent(MainActivity.this, ProductManagementActivity.class);
                startActivity(intent);
            });
        }

        if (btnCreatePromotion != null) {
            btnCreatePromotion.setOnClickListener(v -> {
                Intent intent = new Intent(MainActivity.this, PromotionCreateActivity.class);
                startActivity(intent);
            });
        }

        if (btnUserManagement != null) {
            btnUserManagement.setOnClickListener(v -> {
                Intent intent = new Intent(MainActivity.this, UserManagementActivity.class);
                startActivity(intent);
            });

        btnUserStatistics.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, UserStatisticsActivity.class);
            startActivity(intent);
        });
        }

        btnViewPayment.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, PaymentActivity.class);
            startActivity(intent);
        });
    }

    private void updateUIBasedOnLoginStatus() {
        SharedPreferences prefs = getSharedPreferences("MyPrefs", MODE_PRIVATE);
        String token = prefs.getString("token", null);
        String role = prefs.getString("role", "user");

        if (token != null && !token.isEmpty()) {
            // Role-based UI
            if ("4".equals(role)) { // Admin
                if (btnProductManagement != null) btnProductManagement.setVisibility(android.view.View.VISIBLE);
                if (adminPanel != null) adminPanel.setVisibility(android.view.View.VISIBLE);
                btnCart.setVisibility(android.view.View.GONE);
                if (btnCreatePromotion != null) btnCreatePromotion.setVisibility(android.view.View.GONE);
            } else if ("2".equals(role)) { // Staff
                if (btnProductManagement != null) btnProductManagement.setVisibility(android.view.View.VISIBLE);
                btnCart.setVisibility(android.view.View.GONE);
                if (btnCreatePromotion != null) btnCreatePromotion.setVisibility(android.view.View.VISIBLE);
            } else {
                if (btnProductManagement != null) btnProductManagement.setVisibility(android.view.View.GONE);
                btnCart.setVisibility(android.view.View.VISIBLE);
                if (btnCreatePromotion != null) btnCreatePromotion.setVisibility(android.view.View.GONE);
            }

            tvUserStatus.setText("🌟 Chào mừng bạn trở lại!");
            btnLogin.setVisibility(android.view.View.GONE);
            btnRegister.setVisibility(android.view.View.GONE);
            btnLogout.setVisibility(android.view.View.VISIBLE);
            btnShowProducts.setText("🛍️ Xem Tất Cả Sản Phẩm");
            btnCart.setText("🛒 Giỏ Hàng Của Tôi");
        } else {
            if (btnProductManagement != null) btnProductManagement.setVisibility(android.view.View.GONE);
            btnCart.setVisibility(android.view.View.VISIBLE);
            if (btnCreatePromotion != null) btnCreatePromotion.setVisibility(android.view.View.GONE);

            tvUserStatus.setText("Tài khoản của bạn");
            btnLogin.setVisibility(android.view.View.VISIBLE);
            btnRegister.setVisibility(android.view.View.VISIBLE);
            btnLogout.setVisibility(android.view.View.GONE);
            btnShowProducts.setText("🛍️ Xem Sản Phẩm (Cần đăng nhập)");
            btnCart.setText("🛒 Giỏ Hàng (Cần đăng nhập)");
        }

        // Ẩn/hiện nút thống kê người dùng và xem payment theo role
        if (btnUserStatistics != null) {
            if ("Admin".equalsIgnoreCase(role) || "4".equals(role)) {
                btnUserStatistics.setVisibility(android.view.View.VISIBLE);
            } else {
                btnUserStatistics.setVisibility(android.view.View.GONE);
            }
        }
        if (btnViewPayment != null) {
            btnViewPayment.setVisibility(android.view.View.VISIBLE);
        }

        if (adminPanel != null) {
            if ("Admin".equalsIgnoreCase(role) || "4".equals(role)) {
                adminPanel.setVisibility(android.view.View.VISIBLE);
            } else {
                adminPanel.setVisibility(android.view.View.GONE);
            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        updateUIBasedOnLoginStatus();
    }
}
