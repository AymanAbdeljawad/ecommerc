package com.order_service.entity;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class RequestPayment {
    @NotNull
    @Min(4)
    private Double amount;

    @Email
    private String email;

    @NotBlank
    @Size(min = 5, max = 200)
    private String productName;

    private String currency;
}
