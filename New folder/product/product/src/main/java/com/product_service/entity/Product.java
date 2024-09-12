package com.product_service.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import java.time.LocalDateTime;

@Entity
@Data
@Table(name = "products")
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long productId;

    @NotBlank(message = "Name is mandatory")
    @Column(unique = true, nullable = false)
    private String name;

    @Size(max = 500, message = "Description must be less than or equal to 500 characters")
    private String description;

    @Min(value = 0, message = "Price must be greater than or equal to zero")
    private Double price;

    @Min(value = 0, message = "Quantity must be greater than or equal to zero")
    private Integer quantity;

    @CreatedDate
    @Column(updatable = false, nullable = false)
    private LocalDateTime createdDate;

    @LastModifiedDate
    @Column(nullable = false)
    private LocalDateTime updatedDate;

}
