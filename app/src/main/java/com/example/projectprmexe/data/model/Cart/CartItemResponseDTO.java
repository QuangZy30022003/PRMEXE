package com.example.projectprmexe.data.model.Cart;

import com.google.gson.annotations.SerializedName;

public class CartItemResponseDTO {
    @SerializedName("Id")
    private int id;

    @SerializedName("ProductId")
    private int productId;

    @SerializedName("ProductName")
    private String productName;

    @SerializedName("Quantity")
    private int quantity;

    @SerializedName("Price")
    private double price;

    private int originalQuantity; // Số lượng gốc để tính delta

    // ✅ Constructor để dùng test tạm thời (giả lập)
    public CartItemResponseDTO(String productName, int quantity) {
        this.productName = productName;
        this.quantity = quantity;
        this.originalQuantity = quantity;
    }

    // ❗ Có thể bạn cần thêm constructor mặc định nếu dùng Retrofit
    public CartItemResponseDTO() {}

    public int getId() { return id; }
    public int getProductId() { return productId; }
    public String getProductName() { return productName; }
    public int getQuantity() { return quantity; }
    public double getPrice() { return price; }
    public int getOriginalQuantity() {
        return originalQuantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public void setOriginalQuantity(int originalQuantity) {
        this.originalQuantity = originalQuantity;
    }
}
