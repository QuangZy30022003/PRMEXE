package com.example.projectprmexe.ui;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.projectprmexe.R;
import com.example.projectprmexe.data.api.ProductAPI;
import com.example.projectprmexe.data.model.Product.ProductDto;
import com.example.projectprmexe.data.repository.ProductInstance;
import com.example.projectprmexe.ui.adapter.ProductAdapter;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.appbar.MaterialToolbar;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProductListActivity extends AppCompatActivity implements ProductAdapter.OnProductClickListener {

    private RecyclerView recyclerView;
    private ProductAdapter adapter;
    private List<ProductDto> productList = new ArrayList<>();
    private List<ProductDto> filteredList = new ArrayList<>();
    private EditText searchEditText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_list);
        MaterialToolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        // Xử lý deep link payment-result
        Uri data = getIntent().getData();
        if (data != null && "payment-result".equals(data.getHost())) {
            String success = data.getQueryParameter("success");
            String orderId = data.getQueryParameter("orderId");
            String amount = data.getQueryParameter("amount");
            String message = data.getQueryParameter("message");
            String resultMsg = "Kết quả thanh toán: " + ("true".equals(success) ? "THÀNH CÔNG" : "THẤT BẠI") +
                    "\nMã đơn: " + orderId +
                    (amount != null ? ("\nSố tiền: " + amount) : "") +
                    (message != null ? ("\n" + message) : "");
            Toast.makeText(this, resultMsg, Toast.LENGTH_LONG).show();
        }

        initViews();
        setupRecyclerView();
        setupSearch();
        loadProducts();

        FloatingActionButton fabCart = findViewById(R.id.fabCart);
        fabCart.setOnClickListener(v -> {
            Intent intent = new Intent(ProductListActivity.this, com.example.projectprmexe.ui.CartActivity.class);
            startActivity(intent);
        });
    }

    private void initViews() {
        recyclerView = findViewById(R.id.recyclerProducts);
        searchEditText = findViewById(R.id.etSearchProduct);
    }

    private void setupRecyclerView() {
        adapter = new ProductAdapter(filteredList, this);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
    }

    private void setupSearch() {
        searchEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                filterProducts(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });
    }

    private void filterProducts(String query) {
        filteredList.clear();
        if (query.isEmpty()) {
            filteredList.addAll(productList);
        } else {
            for (ProductDto product : productList) {
                if (product.getName().toLowerCase().contains(query.toLowerCase())) {
                    filteredList.add(product);
                }
            }
        }
        adapter.notifyDataSetChanged();
    }

    private void loadProducts() {
        ProductAPI api = ProductInstance.getApiService();
        Call<List<ProductDto>> call = api.getAllProducts();
        
        call.enqueue(new Callback<List<ProductDto>>() {
            @Override
            public void onResponse(Call<List<ProductDto>> call, Response<List<ProductDto>> response) {
                if (response.isSuccessful()) {
                    List<ProductDto> products = response.body();
                    if (products != null && !products.isEmpty()) {
                        Toast.makeText(ProductListActivity.this, "SUCCESS! Got " + products.size() + " products", Toast.LENGTH_LONG).show();
                        
                        // Debug: Check first product data
                        ProductDto firstProduct = products.get(0);
                        Toast.makeText(ProductListActivity.this, "✅ FIXED: " + firstProduct.getName() + " - " + firstProduct.getPrice() + " VND", Toast.LENGTH_LONG).show();
                        
                        productList.clear();
                        productList.addAll(products);
                        filteredList.clear();
                        filteredList.addAll(productList);
                        adapter.notifyDataSetChanged();
                        
                        Toast.makeText(ProductListActivity.this, "UI Updated! FilteredList size: " + filteredList.size(), Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(ProductListActivity.this, "Empty response from server", Toast.LENGTH_LONG).show();
                        addSampleData();
                    }
                } else {
                    Toast.makeText(ProductListActivity.this, "Server error: " + response.code(), Toast.LENGTH_LONG).show();
                    addSampleData();
                }
            }

            @Override
            public void onFailure(Call<List<ProductDto>> call, Throwable t) {
                Toast.makeText(ProductListActivity.this, "Network error: " + t.getMessage(), Toast.LENGTH_LONG).show();
                addSampleData();
            }
        });
    }

    private void addSampleData() {
        Toast.makeText(this, "⚠️ Adding sample data (API failed)", Toast.LENGTH_LONG).show();
        productList.clear(); // Clear any existing data first
        productList.add(new ProductDto(1, "Hoa Hồng Đỏ", "Hoa hồng đỏ tươi đẹp", 50000, "sample_image_url", true));
        productList.add(new ProductDto(2, "Hoa Ly Trắng", "Hoa ly trắng tinh khôi", 75000, "sample_image_url", true));
        productList.add(new ProductDto(3, "Nước Ép Cam", "Nước ép cam tươi nguyên chất", 25000, "sample_image_url", true));
        filteredList.clear();
        filteredList.addAll(productList);
        adapter.notifyDataSetChanged();
    }

    @Override
    public void onProductClick(ProductDto product) {
        Intent intent = new Intent(this, ProductDetailActivity.class);
        intent.putExtra("product_id", product.getProductId());
        startActivity(intent);
    }

    @Override
    public void onProductLongClick(ProductDto product) {
        // Long click could be used for admin actions in the future
        Toast.makeText(this, "Long clicked: " + product.getName(), Toast.LENGTH_SHORT).show();
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }
}