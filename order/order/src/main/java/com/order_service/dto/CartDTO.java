package com.order_service.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.HashMap;
import java.util.Map;
@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class CartDTO {
    private String id;
    private Map<String, CartItemDTO> items = new HashMap<>();

    public void addItem(CartItemDTO item) {
        items.put(item.getProductId(), item);
    }

    public void removeItem(String productId) {
        items.remove(productId);
    }

    public CartItemDTO getItem(String productId) {
        return items.get(productId);
    }
}

