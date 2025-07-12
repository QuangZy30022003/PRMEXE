package com.example.projectprmexe.data.model.Promotion;

import com.google.gson.annotations.SerializedName;

public class PromotionCreateUpdateDTO {
    @SerializedName("Code")
    private String code;
    @SerializedName("Description")
    private String description;
    @SerializedName("DiscountPercent")
    private int discountPercent;
    @SerializedName("MaxDiscount")
    private double maxDiscount;
    @SerializedName("StartDate")
    private String startDate;
    @SerializedName("EndDate")
    private String endDate;
    @SerializedName("IsActive")
    private boolean isActive;

    public String getCode() { return code; }
    public void setCode(String code) { this.code = code; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public int getDiscountPercent() { return discountPercent; }
    public void setDiscountPercent(int discountPercent) { this.discountPercent = discountPercent; }

    public double getMaxDiscount() { return maxDiscount; }
    public void setMaxDiscount(double maxDiscount) { this.maxDiscount = maxDiscount; }

    public String getStartDate() { return startDate; }
    public void setStartDate(String startDate) { this.startDate = startDate; }

    public String getEndDate() { return endDate; }
    public void setEndDate(String endDate) { this.endDate = endDate; }

    public boolean isIsActive() { return isActive; }
    public void setIsActive(boolean isActive) { this.isActive = isActive; }
} 