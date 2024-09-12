package com.stripePayment.controller;

import com.stripe.exception.StripeException;
import com.stripe.model.PaymentIntent;
import com.stripe.param.PaymentIntentCreateParams;
import com.stripePayment.model.Request;
import com.stripePayment.model.Response;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.ui.Model;

@RestController
public class PaymentCallbackController {

    //    @GetMapping("/payment-success")
//    public String paymentSuccess(@RequestParam("payment_intent") String paymentIntentId, Model model) {
//        // Retrieve payment details from the database using paymentIntentId
//        // Example: PaymentDetails details = paymentService.getPaymentDetails(paymentIntentId);
//
//        model.addAttribute("amount", "Retrieved amount");
//        model.addAttribute("productName", "Retrieved product name");
//        return "success"; // The name of the view template (e.g., success.html)
//    }
    @Value("${stripe.api.publicKey}")
    private String publicKey;

    @PostMapping("/pay")
    public String showCard(@ModelAttribute @Valid Request request,
                           BindingResult bindingResult,
                           Model model) {
        if (bindingResult.hasErrors()) {
            return "index";
        }
        model.addAttribute("publicKey", publicKey);
        model.addAttribute("amount", request.getAmount());
        model.addAttribute("email", request.getEmail());
        model.addAttribute("productName", request.getProductName());
        return "checkout";
    }

    private static final long MAX_AMOUNT_CENTS = 99999999L;

    @PostMapping("/cr-payment-intent")
    public Response createPaymentIntent(@RequestBody Request request) throws StripeException {
        long amountInCents = request.getAmount() * 100L;
        if (amountInCents <= 0 || amountInCents > MAX_AMOUNT_CENTS) {
            throw new IllegalArgumentException("Amount is either negative or too large.");
        }

        PaymentIntentCreateParams params = PaymentIntentCreateParams.builder()
                .setAmount(amountInCents)
                .putMetadata("productName", request.getProductName())
                .setCurrency("usd")
                .setAutomaticPaymentMethods(PaymentIntentCreateParams
                        .AutomaticPaymentMethods
                        .builder()
                        .setEnabled(true)
                        .build())
                .build();

        PaymentIntent intent = PaymentIntent.create(params);
        return new Response(intent.getId(), intent.getClientSecret());
    }

}

