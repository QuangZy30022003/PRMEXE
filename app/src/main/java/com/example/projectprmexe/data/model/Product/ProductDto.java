package com.example.projectprmexe.data.model.Product;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class ProductDto {
    private int productId;
    private String name;
    private String description;
    private double price;
    private String imageUrl;
    private int categoryId;
    private boolean isAvailable;
    private Date createdAt;
    private List<ProductImageDto> images = new ArrayList<>();

    // Default constructor
    public ProductDto() {}

    // Constructor for testing
    public ProductDto(int productId, String name, String description, double price, String imageUrl, boolean isAvailable) {
        this.productId = productId;
        this.name = name;
        this.description = description;
        this.price = price;
        this.imageUrl = imageUrl;
        this.isAvailable = isAvailable;
    }

    // Getters and Setters
    public int getProductId() { return productId; }
    public void setProductId(int productId) { this.productId = productId; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public double getPrice() { return price; }
    public void setPrice(double price) { this.price = price; }

    public String getImageUrl() { return imageUrl; }
    public void setImageUrl(String imageUrl) { this.imageUrl = imageUrl; }

    public int getCategoryId() { return categoryId; }
    public void setCategoryId(int categoryId) { this.categoryId = categoryId; }

    public boolean isAvailable() { return isAvailable; }
    public void setAvailable(boolean available) { isAvailable = available; }

    public Date getCreatedAt() { return createdAt; }
    public void setCreatedAt(Date createdAt) { this.createdAt = createdAt; }

    public List<ProductImageDto> getImages() { return images; }
    public void setImages(List<ProductImageDto> images) { this.images = images; }

    // Helper method to get the first available image URL
    public String getFirstImageUrl() {
        if (imageUrl != null && !imageUrl.isEmpty()) {
            return imageUrl;
        }
        if (images != null && !images.isEmpty()) {
            return images.get(0).getImageUrl();
        }
        return null;
    }

    // Helper method to format price with currency
    public String getFormattedPrice() {
        return String.format("%.0f VND", price);
    }
}