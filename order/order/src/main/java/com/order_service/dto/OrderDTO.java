package com.order_service.dto;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OrderDTO {

    @Null(message = "ID should be null when creating a new order")
    private Long orderId;

    @NotNull(message = "User ID cannot be null")
    @Positive(message = "User ID must be a positive number")
    private Long userId;

    @NotNull(message = "Token cannot be null")
    @Size(min = 1, max = 255, message = "Token must be between 1 and 255 characters")
    private String token;

    @NotNull(message = "Product ID cannot be null")
    @Positive(message = "Product ID must be a positive number")
    private Long productId;

    @NotNull(message = "Product Name cannot be null")
    @Size(min = 1, max = 255, message = "Product Name must be between 1 and 255 characters")
    private String productName;

    @Size(max = 500, message = "Description cannot exceed 500 characters")
    private String description;

    @NotNull(message = "unit Price cannot be null")
    private Double unitPrice;

    @NotNull(message = "Quantity cannot be null")
    @Min(value = 1, message = "Quantity must be at least 1")
    @Max(value = 100000, message = "Quantity cannot exceed 1000")
    private Integer quantity;
}
