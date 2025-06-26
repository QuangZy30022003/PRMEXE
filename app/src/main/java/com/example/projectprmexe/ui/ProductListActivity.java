package com.example.projectprmexe.ui;

import android.content.Intent;
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

        initViews();
        setupRecyclerView();
        setupSearch();
        loadProducts();
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
        Toast.makeText(this, "Đang tải sản phẩm từ API...", Toast.LENGTH_SHORT).show();
        
        api.getAllProducts().enqueue(new Callback<List<ProductDto>>() {
            @Override
            public void onResponse(Call<List<ProductDto>> call, Response<List<ProductDto>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    Toast.makeText(ProductListActivity.this, "API thành công! Có " + response.body().size() + " sản phẩm", Toast.LENGTH_SHORT).show();
                    productList.clear();
                    productList.addAll(response.body());
                    filteredList.clear();
                    filteredList.addAll(productList);
                    adapter.notifyDataSetChanged();
                } else {
                    Toast.makeText(ProductListActivity.this, "API lỗi - Response code: " + response.code(), Toast.LENGTH_LONG).show();
                    addSampleData();
                }
            }

            @Override
            public void onFailure(Call<List<ProductDto>> call, Throwable t) {
                Toast.makeText(ProductListActivity.this, "Lỗi kết nối: " + t.getMessage(), Toast.LENGTH_LONG).show();
                // Add some sample data for testing
                addSampleData();
            }
        });
    }

    private void addSampleData() {
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
}