package com.example.projectprmexe.ui;

import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.projectprmexe.R;
import com.example.projectprmexe.data.api.ProductAPI;
import com.example.projectprmexe.data.model.Product.ProductCreateUpdateDto;
import com.example.projectprmexe.data.model.Product.ProductDto;
import com.example.projectprmexe.data.repository.ProductInstance;
import com.google.android.material.appbar.MaterialToolbar;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProductEditActivity extends AppCompatActivity {

    private EditText etName, etDescription, etPrice, etImageUrl, etCategoryId;
    private CheckBox cbIsAvailable;
    private Button btnUpdateProduct, btnCancel;
    
    private int productId;
    private String productName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_edit);
        MaterialToolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        // Get product info from intent
        productId = getIntent().getIntExtra("product_id", -1);
        productName = getIntent().getStringExtra("product_name");

        if (productId == -1) {
            Toast.makeText(this, "Error: Product ID not found", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        initViews();
        setupClickListeners();
        loadProductData();
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }

    private void initViews() {
        etName = findViewById(R.id.etEditProductName);
        etDescription = findViewById(R.id.etEditProductDescription);
        etPrice = findViewById(R.id.etEditProductPrice);
        etImageUrl = findViewById(R.id.etEditProductImageUrl);
        etCategoryId = findViewById(R.id.etEditProductCategoryId);
        cbIsAvailable = findViewById(R.id.cbEditProductIsAvailable);
        btnUpdateProduct = findViewById(R.id.btnUpdateProduct);
        btnCancel = findViewById(R.id.btnCancelEdit);
    }

    private void setupClickListeners() {
        btnUpdateProduct.setOnClickListener(v -> {
            if (validateInput()) {
                updateProduct();
            }
        });

        btnCancel.setOnClickListener(v -> {
            finish();
        });
    }

    private void loadProductData() {
        Toast.makeText(this, "Loading product data for: " + productName, Toast.LENGTH_SHORT).show();

        ProductAPI api = ProductInstance.getApiService();
        Call<ProductDto> call = api.getProductById(productId);

        call.enqueue(new Callback<ProductDto>() {
            @Override
            public void onResponse(Call<ProductDto> call, Response<ProductDto> response) {
                if (response.isSuccessful() && response.body() != null) {
                    ProductDto product = response.body();
                    populateFields(product);
                    Toast.makeText(ProductEditActivity.this, "Product data loaded", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(ProductEditActivity.this, "Failed to load product data: " + response.code(), Toast.LENGTH_LONG).show();
                    // Load sample data for testing
                    loadSampleData();
                }
            }

            @Override
            public void onFailure(Call<ProductDto> call, Throwable t) {
                Toast.makeText(ProductEditActivity.this, "Network error: " + t.getMessage(), Toast.LENGTH_LONG).show();
                // Load sample data for testing
                loadSampleData();
            }
        });
    }

    private void populateFields(ProductDto product) {
        etName.setText(product.getName());
        etDescription.setText(product.getDescription() != null ? product.getDescription() : "");
        etPrice.setText(String.valueOf(product.getPrice()));
        etImageUrl.setText(product.getImageUrl() != null ? product.getImageUrl() : "");
        etCategoryId.setText(String.valueOf(product.getCategoryId()));
        cbIsAvailable.setChecked(product.isAvailable());
    }

    private void loadSampleData() {
        // Sample data for testing when API fails
        etName.setText(productName != null ? productName : "Sample Product");
        etDescription.setText("Sample description for testing");
        etPrice.setText("50000");
        etImageUrl.setText("https://images.unsplash.com/photo-1518895949257-7621c3c786d7?w=400&h=400&fit=crop");
        etCategoryId.setText("1");
        cbIsAvailable.setChecked(true);
        Toast.makeText(this, "Loaded sample data for testing", Toast.LENGTH_SHORT).show();
    }

    private boolean validateInput() {
        String name = etName.getText().toString().trim();
        String priceStr = etPrice.getText().toString().trim();
        String categoryIdStr = etCategoryId.getText().toString().trim();

        if (TextUtils.isEmpty(name)) {
            etName.setError("Product name is required");
            etName.requestFocus();
            return false;
        }

        if (TextUtils.isEmpty(priceStr)) {
            etPrice.setError("Price is required");
            etPrice.requestFocus();
            return false;
        }

        try {
            double price = Double.parseDouble(priceStr);
            if (price < 0) {
                etPrice.setError("Price must be positive");
                etPrice.requestFocus();
                return false;
            }
        } catch (NumberFormatException e) {
            etPrice.setError("Invalid price format");
            etPrice.requestFocus();
            return false;
        }

        if (TextUtils.isEmpty(categoryIdStr)) {
            etCategoryId.setError("Category ID is required");
            etCategoryId.requestFocus();
            return false;
        }

        try {
            int categoryId = Integer.parseInt(categoryIdStr);
            if (categoryId < 1) {
                etCategoryId.setError("Category ID must be positive");
                etCategoryId.requestFocus();
                return false;
            }
        } catch (NumberFormatException e) {
            etCategoryId.setError("Invalid category ID format");
            etCategoryId.requestFocus();
            return false;
        }

        return true;
    }

    private void updateProduct() {
        String name = etName.getText().toString().trim();
        String description = etDescription.getText().toString().trim();
        double price = Double.parseDouble(etPrice.getText().toString().trim());
        String imageUrl = etImageUrl.getText().toString().trim();
        int categoryId = Integer.parseInt(etCategoryId.getText().toString().trim());
        boolean isAvailable = cbIsAvailable.isChecked();

        // Create DTO
        ProductCreateUpdateDto productDto = new ProductCreateUpdateDto();
        productDto.setName(name);
        productDto.setDescription(description.isEmpty() ? null : description);
        productDto.setPrice(price);
        productDto.setImageUrl(imageUrl.isEmpty() ? null : imageUrl);
        productDto.setCategoryId(categoryId);
        productDto.setAvailable(isAvailable);

        Toast.makeText(this, "Updating product: " + name, Toast.LENGTH_SHORT).show();

        // Lấy token từ SharedPreferences
        android.content.SharedPreferences prefs = getSharedPreferences("MyPrefs", MODE_PRIVATE);
        String token = prefs.getString("token", null);
        if (token == null) {
            Toast.makeText(this, "Vui lòng đăng nhập lại để cập nhật sản phẩm!", Toast.LENGTH_LONG).show();
            return;
        }
        String authHeader = "Bearer " + token;

        ProductAPI api = ProductInstance.getApiService();
        retrofit2.Call<okhttp3.ResponseBody> call = api.updateProduct(productId, productDto, authHeader);

        call.enqueue(new retrofit2.Callback<okhttp3.ResponseBody>() {
            @Override
            public void onResponse(retrofit2.Call<okhttp3.ResponseBody> call, retrofit2.Response<okhttp3.ResponseBody> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(ProductEditActivity.this, "Product updated successfully!", Toast.LENGTH_LONG).show();
                    finish(); // Return to management activity
                } else {
                    Toast.makeText(ProductEditActivity.this, "Failed to update product: " + response.code(), Toast.LENGTH_LONG).show();
                    System.out.println("Update product error: " + response.code() + " - " + response.message());
                }
            }

            @Override
            public void onFailure(retrofit2.Call<okhttp3.ResponseBody> call, Throwable t) {
                Toast.makeText(ProductEditActivity.this, "Network error: " + t.getMessage(), Toast.LENGTH_LONG).show();
                System.out.println("Update product network error: " + t.getMessage());
            }
        });
    }
}