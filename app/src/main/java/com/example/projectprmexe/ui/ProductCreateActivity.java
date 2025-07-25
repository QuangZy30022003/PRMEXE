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
import com.example.projectprmexe.data.repository.ProductInstance;
import com.google.android.material.appbar.MaterialToolbar;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProductCreateActivity extends AppCompatActivity {

    private EditText etName, etDescription, etPrice, etImageUrl, etCategoryId;
    private CheckBox cbIsAvailable;
    private Button btnSaveProduct, btnCancel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_create);
        MaterialToolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        initViews();
        setupClickListeners();
    }

    private void initViews() {
        etName = findViewById(R.id.etProductName);
        etDescription = findViewById(R.id.etProductDescription);
        etPrice = findViewById(R.id.etProductPrice);
        etImageUrl = findViewById(R.id.etProductImageUrl);
        etCategoryId = findViewById(R.id.etProductCategoryId);
        cbIsAvailable = findViewById(R.id.cbProductIsAvailable);
        btnSaveProduct = findViewById(R.id.btnSaveProduct);
        btnCancel = findViewById(R.id.btnCancelProduct);

        // Set default values
        cbIsAvailable.setChecked(true);
        etCategoryId.setText("1"); // Default to category 1
    }

    private void setupClickListeners() {
        btnSaveProduct.setOnClickListener(v -> {
            if (validateInput()) {
                createProduct();
            }
        });

        btnCancel.setOnClickListener(v -> {
            finish();
        });
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

    private void createProduct() {
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

        Toast.makeText(this, "Creating product: " + name, Toast.LENGTH_SHORT).show();

        // Lấy token từ SharedPreferences
        android.content.SharedPreferences prefs = getSharedPreferences("MyPrefs", MODE_PRIVATE);
        String token = prefs.getString("token", null);
        if (token == null) {
            Toast.makeText(this, "Vui lòng đăng nhập lại để tạo sản phẩm!", Toast.LENGTH_LONG).show();
            return;
        }
        String authHeader = "Bearer " + token;

        ProductAPI api = ProductInstance.getApiService();
        retrofit2.Call<ProductCreateUpdateDto> call = api.createProduct(productDto, authHeader);

        call.enqueue(new retrofit2.Callback<ProductCreateUpdateDto>() {
            @Override
            public void onResponse(retrofit2.Call<ProductCreateUpdateDto> call, retrofit2.Response<ProductCreateUpdateDto> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(ProductCreateActivity.this, "Product created successfully!", Toast.LENGTH_LONG).show();
                    finish(); // Return to management activity
                } else {
                    Toast.makeText(ProductCreateActivity.this, "Failed to create product: " + response.code(), Toast.LENGTH_LONG).show();
                    System.out.println("Create product error: " + response.code() + " - " + response.message());
                }
            }

            @Override
            public void onFailure(retrofit2.Call<ProductCreateUpdateDto> call, Throwable t) {
                Toast.makeText(ProductCreateActivity.this, "Network error: " + t.getMessage(), Toast.LENGTH_LONG).show();
                System.out.println("Create product network error: " + t.getMessage());
            }
        });
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }
}