package com.product_service.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class QuantityDTO {
    private Long productId;
    @NotNull(message = "Quantity is mandatory")
    @Min(value = 0, message = "Quantity must be zero or positive")
    private Integer quantity;
}
