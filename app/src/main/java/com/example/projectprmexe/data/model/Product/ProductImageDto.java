package com.example.projectprmexe.data.model.Product;

public class ProductImageDto {
    private int id;
    private String imageUrl;

    // Default constructor
    public ProductImageDto() {}

    // Constructor
    public ProductImageDto(int id, String imageUrl) {
        this.id = id;
        this.imageUrl = imageUrl;
    }

    // Getters and Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getImageUrl() { return imageUrl; }
    public void setImageUrl(String imageUrl) { this.imageUrl = imageUrl; }
}