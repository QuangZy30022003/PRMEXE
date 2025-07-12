package com.example.projectprmexe;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;

import com.example.projectprmexe.ui.CartActivity;
import com.example.projectprmexe.ui.ProductListActivity;

public class MainActivity extends AppCompatActivity {

<<<<<<< Updated upstream
    private Button btnProducts, btnCart;
=======
    private Button btnShowProducts, btnLogin, btnRegister, btnCart, btnLogout;
    private TextView tvUserStatus;
    private ActivityResultLauncher<Intent> loginLauncher;
    private Button btnProductManagement;
    private Button btnCreatePromotion;
>>>>>>> Stashed changes

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

        // Đăng ký launcher cho đăng nhập
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
        btnProducts = findViewById(R.id.btnProducts);
        btnCart = findViewById(R.id.btnCart);
<<<<<<< Updated upstream
    }

    private void setupClickListeners() {
        btnProducts.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, ProductListActivity.class);
            startActivity(intent);
        });

        btnCart.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, CartActivity.class);
            startActivity(intent);
        });
=======
        btnLogout = findViewById(R.id.btnLogout);
        tvUserStatus = findViewById(R.id.tvUserStatus);
        btnProductManagement = findViewById(R.id.btnProductManagement); // Thêm dòng này
        btnCreatePromotion = findViewById(R.id.btnCreatePromotion);
        if (btnProductManagement != null) btnProductManagement.setVisibility(android.view.View.GONE);
        if (btnCreatePromotion != null) btnCreatePromotion.setVisibility(android.view.View.GONE);
        updateUIBasedOnLoginStatus();
    }

    private void setupClickListeners() {
        // Nút xem sản phẩm - kiểm tra đăng nhập
        btnShowProducts.setOnClickListener(v -> {
            SharedPreferences prefs = getSharedPreferences("MyPrefs", MODE_PRIVATE);
            String token = prefs.getString("token", null);
            
            if (token != null && !token.isEmpty()) {
                // Đã đăng nhập, chuyển đến ProductList
                Intent intent = new Intent(MainActivity.this, ProductListActivity.class);
                startActivity(intent);
            } else {
                // Chưa đăng nhập, yêu cầu đăng nhập
                Toast.makeText(MainActivity.this, "Vui lòng đăng nhập để xem sản phẩm!", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                loginLauncher.launch(intent);
            }
        });

        // Nút đăng nhập
        btnLogin.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, LoginActivity.class);
            loginLauncher.launch(intent);
        });

        // Nút đăng ký
        btnRegister.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, RegisterActivity.class);
            startActivity(intent);
        });

        // Nút giỏ hàng - cần đăng nhập
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
        
        // Nút đăng xuất
        btnLogout.setOnClickListener(v -> {
            SharedPreferences prefs = getSharedPreferences("MyPrefs", MODE_PRIVATE);
            prefs.edit().remove("token").apply();
            Toast.makeText(MainActivity.this, "Đã đăng xuất thành công!", Toast.LENGTH_SHORT).show();
            updateUIBasedOnLoginStatus();
        });

        if (btnProductManagement != null) {
            btnProductManagement.setOnClickListener(v -> {
                Intent intent = new Intent(MainActivity.this, com.example.projectprmexe.ui.ProductManagementActivity.class);
                startActivity(intent);
            });
        }

        if (btnCreatePromotion != null) {
            btnCreatePromotion.setOnClickListener(v -> {
                Intent intent = new Intent(MainActivity.this, com.example.projectprmexe.ui.PromotionCreateActivity.class);
                startActivity(intent);
            });
        }
    }
    
    private void updateUIBasedOnLoginStatus() {
        SharedPreferences prefs = getSharedPreferences("MyPrefs", MODE_PRIVATE);
        String token = prefs.getString("token", null);
        String role = prefs.getString("role", "user");

        if (token != null && !token.isEmpty()) {
            if ("4".equals(role)) { // admin
                if (btnProductManagement != null) btnProductManagement.setVisibility(android.view.View.VISIBLE);
                btnCart.setVisibility(android.view.View.GONE);
                if (btnCreatePromotion != null) btnCreatePromotion.setVisibility(android.view.View.GONE);
            } else if ("2".equals(role)) { // staff
                if (btnProductManagement != null) btnProductManagement.setVisibility(android.view.View.VISIBLE);
                btnCart.setVisibility(android.view.View.GONE);
                if (btnCreatePromotion != null) btnCreatePromotion.setVisibility(android.view.View.VISIBLE); // staff có thêm nút này
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
    }
    
    @Override
    protected void onResume() {
        super.onResume();
        updateUIBasedOnLoginStatus();
>>>>>>> Stashed changes
    }
}