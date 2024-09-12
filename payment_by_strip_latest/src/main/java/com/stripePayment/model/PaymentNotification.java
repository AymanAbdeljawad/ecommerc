package com.stripePayment.model;

public class PaymentNotification {
    private String amount;
    private String productName;

    // Constructors, getters, and setters
    public PaymentNotification(String amount, String productName) {
        this.amount = amount;
        this.productName = productName;
    }

    // Getters and Setters
    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }
}
