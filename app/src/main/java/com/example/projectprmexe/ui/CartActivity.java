package com.example.projectprmexe.ui;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.projectprmexe.R;
import com.example.projectprmexe.data.api.CartAPI;
import com.example.projectprmexe.data.api.OrderAPI;
import com.example.projectprmexe.data.api.ProductAPI;
import com.example.projectprmexe.data.model.Cart.CartItemResponseDTO;
import com.example.projectprmexe.data.model.Cart.OrderFromCartDTO;
import com.example.projectprmexe.data.model.Cart.OrderResponse;
import com.example.projectprmexe.data.model.Product.ProductDto;
import com.example.projectprmexe.data.repository.CartInstance;
import com.example.projectprmexe.data.repository.OrderInstance;
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
    private EditText edtDeliveryAddress, edtNote, edtPromotionCode;
    private int lastOrderCode = -1;

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
        edtDeliveryAddress = findViewById(R.id.edtDeliveryAddress);
        edtNote = findViewById(R.id.edtNote);
        edtPromotionCode = findViewById(R.id.edtPromotionCode);
        btnCheckout.setOnClickListener(v -> {
            // Lấy danh sách id cart item
            List<Integer> selectedCartItemIds = new ArrayList<>();
            for (CartItemResponseDTO item : cartItems) {
                selectedCartItemIds.add(item.getId());
            }
            String address = edtDeliveryAddress.getText().toString().trim();
            String note = edtNote.getText().toString().trim();
            String promotionCode = edtPromotionCode.getText().toString().trim();
            if (address.isEmpty()) {
                Toast.makeText(this, "Vui lòng nhập địa chỉ nhận hàng!", Toast.LENGTH_SHORT).show();
                return;
            }
            OrderFromCartDTO dto = new OrderFromCartDTO(selectedCartItemIds, address, note, promotionCode);
            OrderAPI orderAPI = OrderInstance.getApiService();
            btnCheckout.setEnabled(false);
            orderAPI.createOrderFromCart("Bearer " + token, dto).enqueue(new retrofit2.Callback<OrderResponse>() {
                @Override
                public void onResponse(Call<OrderResponse> call, Response<OrderResponse> response) {
                    btnCheckout.setEnabled(true);
                    if (response.isSuccessful() && response.body() != null) {
                        String paymentUrl = response.body().getPaymentUrl();
                        lastOrderCode = response.body().getPayOSOrderCode(); // Lưu lại orderCode
                        android.util.Log.d("PAYMENT_URL", "paymentUrl = " + paymentUrl);
                        if (paymentUrl == null || paymentUrl.isEmpty()) {
                            Toast.makeText(CartActivity.this, "Không nhận được link thanh toán từ server! Đơn hàng có thể đã được xử lý hoặc có lỗi.", Toast.LENGTH_LONG).show();
                            return;
                        }
                        Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(paymentUrl));
                        startActivity(browserIntent);
                        // Sau khi mở link thanh toán, chờ user quay lại app rồi gọi callback test
                    } else {
                        Toast.makeText(CartActivity.this, "Tạo đơn hàng thất bại!", Toast.LENGTH_SHORT).show();
                    }
                }
                @Override
                public void onFailure(Call<OrderResponse> call, Throwable t) {
                    btnCheckout.setEnabled(true);
                    Toast.makeText(CartActivity.this, "Lỗi kết nối!", Toast.LENGTH_SHORT).show();
                }
            });
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

    @Override
    protected void onResume() {
        super.onResume();
        // Nếu vừa tạo đơn hàng và có orderCode, tự động gọi test callback
        if (lastOrderCode > 0) {
            callTestPaymentCallback(lastOrderCode);
            lastOrderCode = -1;
        }
    }

    private void callTestPaymentCallback(int orderCode) {
        // Gọi API test-payment-callback để cập nhật trạng thái đơn hàng
        new Thread(() -> {
            try {
                java.net.URL url = new java.net.URL("http://10.0.2.2:5150/api/Orders/test-payment-callback?orderCode=" + orderCode + "&status=success");
                java.net.HttpURLConnection conn = (java.net.HttpURLConnection) url.openConnection();
                conn.setRequestMethod("GET");
                int responseCode = conn.getResponseCode();
                android.util.Log.d("CALLBACK", "Test callback response: " + responseCode);
                conn.disconnect();
            } catch (Exception e) {
                android.util.Log.e("CALLBACK", "Error calling test callback", e);
            }
        }).start();
    }
}
