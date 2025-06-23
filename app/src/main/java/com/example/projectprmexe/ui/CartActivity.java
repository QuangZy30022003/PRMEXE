package com.example.projectprmexe.ui;

import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.projectprmexe.R;
import com.example.projectprmexe.data.api.CartAPI;
import com.example.projectprmexe.data.model.Cart.CartItemResponseDTO;
import com.example.projectprmexe.data.repository.CartInstance;
import com.example.projectprmexe.ui.adapter.CartAdapter;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class CartActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private CartAdapter adapter;
    private List<CartItemResponseDTO> cartItems = new ArrayList<>();
    private String token = "Bearer your_jwt_token_here"; // lấy từ SharedPreferences nếu có

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);

        recyclerView = findViewById(R.id.recyclerCart);
        adapter = new CartAdapter(cartItems);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        cartItems.add(new CartItemResponseDTO("Hoa Hồng", 3));
        cartItems.add(new CartItemResponseDTO("Hoa Ly", 2));
        adapter.notifyDataSetChanged();
       // loadCart();
    }

    private void loadCart() {
        CartAPI api = CartInstance.getApiService();
        api.getCart(token).enqueue(new Callback<List<CartItemResponseDTO>>() {
            @Override
            public void onResponse(Call<List<CartItemResponseDTO>> call, Response<List<CartItemResponseDTO>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    cartItems.clear();
                    cartItems.addAll(response.body());
                    adapter.notifyDataSetChanged();
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
}
