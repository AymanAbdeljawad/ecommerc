package com.product_service.dto;

import jakarta.validation.constraints.*;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class ProductDTO {

    private Long productId;

    @NotBlank(message = "Name is mandatory")
    private String name;

    @Size(max = 500, message = "Description must be less than or equal to 500 characters")
    private String description;

    @NotNull(message = "Price is mandatory")
    @PositiveOrZero(message = "Price must be zero or positive")
    private Double price;

    @NotNull(message = "Quantity is mandatory")
    @Min(value = 0, message = "Quantity must be zero or positive")
    private Integer quantity;

    private LocalDateTime createdDate;
    private LocalDateTime updatedDate;
}

