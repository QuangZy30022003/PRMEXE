package com.example.projectprmexe.data.model.Product;

public class ProductCreateUpdateDto {
    private String name;
    private String description;
    private double price;
    private String imageUrl;
    private int categoryId;
    private boolean isAvailable;

    // Default constructor
    public ProductCreateUpdateDto() {}

    // Constructor
    public ProductCreateUpdateDto(String name, String description, double price, String imageUrl, int categoryId, boolean isAvailable) {
        this.name = name;
        this.description = description;
        this.price = price;
        this.imageUrl = imageUrl;
        this.categoryId = categoryId;
        this.isAvailable = isAvailable;
    }

    // Getters and Setters
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
}