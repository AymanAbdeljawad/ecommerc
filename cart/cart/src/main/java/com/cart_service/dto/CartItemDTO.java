package com.cart_service.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CartItemDTO {
    private String productId;
    private String productName;
    private String productDescription;
    private int quantity;
    private double unitPrice;

    public double getTotalPrice() {
        return this.unitPrice * this.quantity;
    }
}

