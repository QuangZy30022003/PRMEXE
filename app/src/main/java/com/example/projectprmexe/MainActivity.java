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

import com.example.projectprmexe.ui.CartActivity;
import com.example.projectprmexe.ui.ProductListActivity;

public class MainActivity extends AppCompatActivity {

    private Button btnShowProducts, btnLogin, btnRegister, btnCart, btnLogout;
    private TextView tvUserStatus;

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

        initViews();
        setupClickListeners();
    }

    private void initViews() {
        btnShowProducts = findViewById(R.id.btnShowProducts);
        btnLogin = findViewById(R.id.btnLogin);
        btnRegister = findViewById(R.id.btnRegister);
        btnCart = findViewById(R.id.btnCart);
        btnLogout = findViewById(R.id.btnLogout);
        tvUserStatus = findViewById(R.id.tvUserStatus);
        
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
                startActivity(intent);
            }
        });

        // Nút đăng nhập
        btnLogin.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, LoginActivity.class);
            startActivity(intent);
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
    }
    
    private void updateUIBasedOnLoginStatus() {
        SharedPreferences prefs = getSharedPreferences("MyPrefs", MODE_PRIVATE);
        String token = prefs.getString("token", null);
        
        if (token != null && !token.isEmpty()) {
            // Đã đăng nhập
            tvUserStatus.setText("🌟 Chào mừng bạn trở lại!");
            btnLogin.setVisibility(android.view.View.GONE);
            btnRegister.setVisibility(android.view.View.GONE);
            btnLogout.setVisibility(android.view.View.VISIBLE);
            btnShowProducts.setText("🛍️ Xem Tất Cả Sản Phẩm");
            btnCart.setText("🛒 Giỏ Hàng Của Tôi");
        } else {
            // Chưa đăng nhập
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
    }
}