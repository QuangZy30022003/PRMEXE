package com.example.projectprmexe.data.model.Cart;

import com.google.gson.annotations.SerializedName;

public class OrderResponse {
    @SerializedName("OrderId")
    private int orderId;

    @SerializedName("PaymentUrl")
    private String paymentUrl;

    @SerializedName("PayOSOrderCode")
    private int payOSOrderCode;

    public int getOrderId() {
        return orderId;
    }

    public void setOrderId(int orderId) {
        this.orderId = orderId;
    }

    public String getPaymentUrl() {
        return paymentUrl;
    }

    public void setPaymentUrl(String paymentUrl) {
        this.paymentUrl = paymentUrl;
    }

    public int getPayOSOrderCode() {
        return payOSOrderCode;
    }

    public void setPayOSOrderCode(int payOSOrderCode) {
        this.payOSOrderCode = payOSOrderCode;
    }
} 