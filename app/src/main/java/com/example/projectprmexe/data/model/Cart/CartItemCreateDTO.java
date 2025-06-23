package com.example.projectprmexe.data.model.Cart;

public class CartItemCreateDTO {
    private int productId;
    private int quantity;

    public CartItemCreateDTO(int productId, int quantity) {
        this.productId = productId;
        this.quantity = quantity;
    }

    public int getProductId() { return productId; }
    public int getQuantity() { return quantity; }
}
