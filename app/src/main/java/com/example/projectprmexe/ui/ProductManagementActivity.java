package com.example.projectprmexe.ui;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.projectprmexe.R;
import com.example.projectprmexe.data.api.ProductAPI;
import com.example.projectprmexe.data.model.Product.ProductDto;
import com.example.projectprmexe.data.repository.ProductInstance;
import com.example.projectprmexe.ui.adapter.ProductManagementAdapter;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProductManagementActivity extends AppCompatActivity implements ProductManagementAdapter.OnProductManagementListener {

    private RecyclerView recyclerView;
    private ProductManagementAdapter adapter;
    private List<ProductDto> productList = new ArrayList<>();
    private List<ProductDto> filteredList = new ArrayList<>();
    private EditText searchEditText;
    private FloatingActionButton fabAddProduct;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_management);

        initViews();
        setupRecyclerView();
        setupSearch();
        setupFAB();
        loadProducts();
    }

    private void initViews() {
        recyclerView = findViewById(R.id.recyclerProductsManagement);
        searchEditText = findViewById(R.id.etSearchProductManagement);
        fabAddProduct = findViewById(R.id.fabAddProduct);
    }

    private void setupRecyclerView() {
        adapter = new ProductManagementAdapter(filteredList, this);
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

    private void setupFAB() {
        fabAddProduct.setOnClickListener(v -> {
            Intent intent = new Intent(ProductManagementActivity.this, ProductCreateActivity.class);
            startActivity(intent);
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
        Toast.makeText(this, "Loading products for management...", Toast.LENGTH_SHORT).show();
        
        ProductAPI api = ProductInstance.getApiService();
        Call<List<ProductDto>> call = api.getAllProducts();
        
        call.enqueue(new Callback<List<ProductDto>>() {
            @Override
            public void onResponse(Call<List<ProductDto>> call, Response<List<ProductDto>> response) {
                if (response.isSuccessful()) {
                    List<ProductDto> products = response.body();
                    if (products != null && !products.isEmpty()) {
                        Toast.makeText(ProductManagementActivity.this, "Loaded " + products.size() + " products for management", Toast.LENGTH_SHORT).show();
                        
                        productList.clear();
                        productList.addAll(products);
                        filteredList.clear();
                        filteredList.addAll(productList);
                        adapter.notifyDataSetChanged();
                    } else {
                        Toast.makeText(ProductManagementActivity.this, "No products found", Toast.LENGTH_SHORT).show();
                        addSampleData();
                    }
                } else {
                    Toast.makeText(ProductManagementActivity.this, "Server error: " + response.code(), Toast.LENGTH_LONG).show();
                    addSampleData();
                }
            }

            @Override
            public void onFailure(Call<List<ProductDto>> call, Throwable t) {
                Toast.makeText(ProductManagementActivity.this, "Network error: " + t.getMessage(), Toast.LENGTH_LONG).show();
                addSampleData();
            }
        });
    }

    private void addSampleData() {
        Toast.makeText(this, "Adding sample data for testing", Toast.LENGTH_SHORT).show();
        productList.clear();
        productList.add(new ProductDto(1, "Red Rose", "Beautiful red rose", 50000, "sample_image_url", true));
        productList.add(new ProductDto(2, "White Lily", "Elegant white lily", 75000, "sample_image_url", true));
        productList.add(new ProductDto(3, "Orange Juice", "Fresh orange juice", 25000, "sample_image_url", true));
        filteredList.clear();
        filteredList.addAll(productList);
        adapter.notifyDataSetChanged();
    }

    @Override
    public void onProductView(ProductDto product) {
        Intent intent = new Intent(this, ProductDetailActivity.class);
        intent.putExtra("product_id", product.getProductId());
        startActivity(intent);
    }

    @Override
    public void onProductEdit(ProductDto product) {
        Intent intent = new Intent(this, ProductEditActivity.class);
        intent.putExtra("product_id", product.getProductId());
        intent.putExtra("product_name", product.getName());
        startActivity(intent);
    }

    @Override
    public void onProductDelete(ProductDto product) {
        showDeleteConfirmationDialog(product);
    }

    private void showDeleteConfirmationDialog(ProductDto product) {
        new AlertDialog.Builder(this)
                .setTitle("Delete Product")
                .setMessage("Are you sure you want to delete \"" + product.getName() + "\"?\n\nThis action cannot be undone.")
                .setPositiveButton("Delete", (dialog, which) -> {
                    deleteProduct(product);
                })
                .setNegativeButton("Cancel", null)
                .setIcon(android.R.drawable.ic_dialog_alert)
                .show();
    }

    private void deleteProduct(ProductDto product) {
        Toast.makeText(this, "Deleting " + product.getName() + "...", Toast.LENGTH_SHORT).show();
        
        ProductAPI api = ProductInstance.getApiService();
        Call<Void> call = api.deleteProduct(product.getProductId());
        
        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(ProductManagementActivity.this, "Product deleted successfully!", Toast.LENGTH_SHORT).show();
                    // Refresh the list
                    loadProducts();
                } else {
                    Toast.makeText(ProductManagementActivity.this, "Failed to delete product: " + response.code(), Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Toast.makeText(ProductManagementActivity.this, "Delete failed: " + t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        // Refresh products when returning from create/edit activities
        loadProducts();
    }
}