package com.example.projectprmexe.ui;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.projectprmexe.R;
import com.example.projectprmexe.data.api.ProductAPI;
import com.example.projectprmexe.data.model.Product.ProductDto;
import com.example.projectprmexe.data.model.Product.ProductImageDto;
import com.example.projectprmexe.data.repository.ProductInstance;
import com.example.projectprmexe.ui.adapter.ProductImageAdapter;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProductDetailActivity extends AppCompatActivity {

    private ImageView imgMainProduct;
    private TextView txtName, txtDescription, txtPrice, txtAvailability, txtCreatedAt, txtCategoryId;
    private RecyclerView recyclerImages;
    private ProductImageAdapter imageAdapter;
    private List<ProductImageDto> imageList = new ArrayList<>();
    
    private int productId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_detail);

        // Get product ID from intent
        productId = getIntent().getIntExtra("product_id", -1);
        if (productId == -1) {
            Toast.makeText(this, "Lỗi: Không tìm thấy sản phẩm", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        initViews();
        setupImageRecyclerView();
        loadProductDetail();
    }

    private void initViews() {
        imgMainProduct = findViewById(R.id.imgMainProduct);
        txtName = findViewById(R.id.txtDetailProductName);
        txtDescription = findViewById(R.id.txtDetailProductDescription);
        txtPrice = findViewById(R.id.txtDetailProductPrice);
        txtAvailability = findViewById(R.id.txtDetailProductAvailability);
        txtCreatedAt = findViewById(R.id.txtDetailProductCreatedAt);
        txtCategoryId = findViewById(R.id.txtDetailProductCategory);
        recyclerImages = findViewById(R.id.recyclerProductImages);
    }

    private void setupImageRecyclerView() {
        imageAdapter = new ProductImageAdapter(imageList);
        recyclerImages.setAdapter(imageAdapter);
        recyclerImages.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
    }

    private void loadProductDetail() {
        ProductAPI api = ProductInstance.getApiService();
        api.getProductById(productId).enqueue(new Callback<ProductDto>() {
            @Override
            public void onResponse(Call<ProductDto> call, Response<ProductDto> response) {
                if (response.isSuccessful() && response.body() != null) {
                    displayProductDetail(response.body());
                } else {
                    Toast.makeText(ProductDetailActivity.this, "Không thể tải thông tin sản phẩm", Toast.LENGTH_SHORT).show();
                    // Load sample data for testing
                    loadSampleData();
                }
            }

            @Override
            public void onFailure(Call<ProductDto> call, Throwable t) {
                Toast.makeText(ProductDetailActivity.this, "Lỗi kết nối: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                // Load sample data for testing
                loadSampleData();
            }
        });
    }

    private void displayProductDetail(ProductDto product) {
        txtName.setText(product.getName());
        txtDescription.setText(product.getDescription() != null ? product.getDescription() : "Không có mô tả");
        txtPrice.setText(product.getFormattedPrice());
        txtCategoryId.setText("Danh mục: " + product.getCategoryId());

        // Set availability
        if (product.isAvailable()) {
            txtAvailability.setText("Còn hàng");
            txtAvailability.setTextColor(getResources().getColor(android.R.color.holo_green_dark));
        } else {
            txtAvailability.setText("Hết hàng");
            txtAvailability.setTextColor(getResources().getColor(android.R.color.holo_red_dark));
        }

        // Format and display created date
        if (product.getCreatedAt() != null) {
            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault());
            txtCreatedAt.setText("Ngày tạo: " + sdf.format(product.getCreatedAt()));
        } else {
            txtCreatedAt.setText("Ngày tạo: Không có thông tin");
        }

        // Set main image (placeholder for now)
        imgMainProduct.setImageResource(R.drawable.ic_launcher_foreground);

        // Load additional images
        if (product.getImages() != null && !product.getImages().isEmpty()) {
            imageList.clear();
            imageList.addAll(product.getImages());
            imageAdapter.notifyDataSetChanged();
        }
    }

    private void loadSampleData() {
        // Sample data for testing
        ProductDto sampleProduct = new ProductDto(productId, "Hoa Hồng Đỏ", 
            "Hoa hồng đỏ tươi đẹp, thích hợp làm quà tặng cho người thân yêu. Được trồng trong điều kiện tự nhiên, đảm bảo chất lượng tốt nhất.", 
            50000, "sample_image_url", true);
        
        // Add sample images
        List<ProductImageDto> sampleImages = new ArrayList<>();
        sampleImages.add(new ProductImageDto(1, "sample_image_1"));
        sampleImages.add(new ProductImageDto(2, "sample_image_2"));
        sampleImages.add(new ProductImageDto(3, "sample_image_3"));
        sampleProduct.setImages(sampleImages);
        
        displayProductDetail(sampleProduct);
    }
}