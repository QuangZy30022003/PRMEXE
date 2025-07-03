package com.example.projectprmexe.ui;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.projectprmexe.R;
import com.example.projectprmexe.data.api.CartAPI;
import com.example.projectprmexe.data.api.ProductAPI;
import com.example.projectprmexe.data.model.Cart.CartItemResponseDTO;
import com.example.projectprmexe.data.model.Product.ProductDto;
import com.example.projectprmexe.data.repository.CartInstance;
import com.example.projectprmexe.data.repository.ProductInstance;
import com.example.projectprmexe.ui.adapter.CartAdapter;
import com.example.projectprmexe.LoginActivity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class CartActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private CartAdapter adapter;
    private List<CartItemResponseDTO> cartItems = new ArrayList<>();
    private TextView txtCartTotal;
    private Button btnCheckout;
    private Map<Integer, Double> productPriceMap = new HashMap<>();
    private String token;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);

        recyclerView = findViewById(R.id.recyclerCart);
        adapter = new CartAdapter(cartItems, this::updateCartTotal);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        txtCartTotal = findViewById(R.id.txtCartTotal);
        btnCheckout = findViewById(R.id.btnCheckout);
        btnCheckout.setOnClickListener(v -> {
            Toast.makeText(this, "Chức năng thanh toán đang phát triển!", Toast.LENGTH_SHORT).show();
        });

        // Lấy token từ SharedPreferences
        SharedPreferences prefs = getSharedPreferences("MyPrefs", MODE_PRIVATE);
        token = prefs.getString("token", null);

        if (token != null) {
            loadAllProductsAndCart();
        } else {
            Toast.makeText(this, "Bạn chưa đăng nhập!", Toast.LENGTH_SHORT).show();
        }
    }

    private void loadAllProductsAndCart() {
        ProductAPI productAPI = ProductInstance.getApiService();
        productAPI.getAllProducts().enqueue(new Callback<List<ProductDto>>() {
            @Override
            public void onResponse(Call<List<ProductDto>> call, Response<List<ProductDto>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    for (ProductDto product : response.body()) {
                        productPriceMap.put(product.getProductId(), product.getPrice());
                    }
                    // Sau khi có map giá, load cart
                    loadCart(token);
                }
            }
            @Override
            public void onFailure(Call<List<ProductDto>> call, Throwable t) {
                Toast.makeText(CartActivity.this, "Không thể tải danh sách sản phẩm", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void loadCart(String token) {
        CartAPI api = CartInstance.getApiService();
        api.getCart("Bearer " + token).enqueue(new Callback<List<CartItemResponseDTO>>() {
            @Override
            public void onResponse(Call<List<CartItemResponseDTO>> call, Response<List<CartItemResponseDTO>> response) {
                if (response.code() == 401) {
                    // Token hết hạn, xóa token và chuyển về LoginActivity
                    SharedPreferences prefs = getSharedPreferences("MyPrefs", MODE_PRIVATE);
                    prefs.edit().remove("token").apply();
                    Intent intent = new Intent(CartActivity.this, LoginActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);
                    finish();
                    return;
                }
                if (response.isSuccessful() && response.body() != null) {
                    cartItems.clear();
                    cartItems.addAll(response.body());
                    adapter.notifyDataSetChanged();
                    updateCartTotal();
                } else {
                    Toast.makeText(CartActivity.this, "Không thể tải giỏ hàng", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<CartItemResponseDTO>> call, Throwable t) {
                Toast.makeText(CartActivity.this, "Lỗi: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void updateCartTotal() {
        double total = 0;
        for (CartItemResponseDTO item : cartItems) {
            double price = productPriceMap.containsKey(item.getProductId()) ? productPriceMap.get(item.getProductId()) : 0;
            total += price * item.getQuantity();
        }
        txtCartTotal.setText("Tổng: " + String.format("%.0f VND", total));
    }
}
