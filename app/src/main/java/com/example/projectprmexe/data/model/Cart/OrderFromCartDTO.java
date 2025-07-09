package com.example.projectprmexe.data.model.Cart;

import java.util.List;

public class OrderFromCartDTO {
    private List<Integer> selectedCartItemIds;
    private String deliveryAddress;
    private String note;
    private String promotionCode;

    public OrderFromCartDTO(List<Integer> selectedCartItemIds, String deliveryAddress, String note, String promotionCode) {
        this.selectedCartItemIds = selectedCartItemIds;
        this.deliveryAddress = deliveryAddress;
        this.note = note;
        this.promotionCode = promotionCode;
    }

    public List<Integer> getSelectedCartItemIds() {
        return selectedCartItemIds;
    }

    public void setSelectedCartItemIds(List<Integer> selectedCartItemIds) {
        this.selectedCartItemIds = selectedCartItemIds;
    }

    public String getDeliveryAddress() {
        return deliveryAddress;
    }

    public void setDeliveryAddress(String deliveryAddress) {
        this.deliveryAddress = deliveryAddress;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public String getPromotionCode() {
        return promotionCode;
    }

    public void setPromotionCode(String promotionCode) {
        this.promotionCode = promotionCode;
    }
} 