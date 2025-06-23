package com.example.projectprmexe.data.model.Cart;

public class CartItemResponseDTO {
    private int id;
    private int productId;
    private String productName;
    private int quantity;

    // ✅ Constructor để dùng test tạm thời (giả lập)
    public CartItemResponseDTO(String productName, int quantity) {
        this.productName = productName;
        this.quantity = quantity;
    }

    // ❗ Có thể bạn cần thêm constructor mặc định nếu dùng Retrofit
    public CartItemResponseDTO() {}

    public int getId() { return id; }
    public int getProductId() { return productId; }
    public String getProductName() { return productName; }
    public int getQuantity() { return quantity; }
}
