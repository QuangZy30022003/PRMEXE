package com.example.projectprmexe.data.model;

import com.google.gson.annotations.SerializedName;

public class PaymentDto {
    @SerializedName("PaymentId")
    private int paymentId;
    @SerializedName("OrderId")
    private int orderId;
    @SerializedName("PaymentMethod")
    private String paymentMethod;
    @SerializedName("PaidAmount")
    private double paidAmount;
    @SerializedName("PaymentDate")
    private String paymentDate;
    @SerializedName("Status")
    private String status;

    public int getPaymentId() { return paymentId; }
    public int getOrderId() { return orderId; }
    public String getPaymentMethod() { return paymentMethod; }
    public double getPaidAmount() { return paidAmount; }
    public String getPaymentDate() { return paymentDate; }
    public String getStatus() { return status; }

    public void setPaymentId(int paymentId) { this.paymentId = paymentId; }
    public void setOrderId(int orderId) { this.orderId = orderId; }
    public void setPaymentMethod(String paymentMethod) { this.paymentMethod = paymentMethod; }
    public void setPaidAmount(double paidAmount) { this.paidAmount = paidAmount; }
    public void setPaymentDate(String paymentDate) { this.paymentDate = paymentDate; }
    public void setStatus(String status) { this.status = status; }
} 