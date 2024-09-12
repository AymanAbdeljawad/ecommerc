package com.order_service.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.Data;

@Entity
@Data
@Table(name = "orders")  // Changed from "order" to "orders"
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "user_id", nullable = false)
    private Long userId;

    @Column(nullable = false)
    private String token;

    @Column(name = "product_id", nullable = false)
    private Long productId;

    @Column(name = "product_name", nullable = false)
    private String productName;

    @Column(length = 500)
    private String description;

    @Column(name = "total_price", nullable = false)
    private Double totalPrice;

    @Column(nullable = false)
    private Integer quantity;
}

