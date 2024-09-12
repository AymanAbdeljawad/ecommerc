package com.stripePayment.controller;


import com.stripe.exception.StripeException;
import com.stripe.model.PaymentIntent;
import com.stripe.param.PaymentIntentCreateParams;
import com.stripePayment.model.Request;
import com.stripePayment.model.Response;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class PaymentIntentController {

    private static final long MAX_AMOUNT_CENTS = 99999999L;

    @PostMapping("/create-payment-intent")
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



//    @PostMapping("/create-payment-intent")
//    public Response createPaymentIntent(@RequestBody Request request)
//            throws StripeException {
//        PaymentIntentCreateParams params = PaymentIntentCreateParams.builder()
//                .setAmount(request.getAmount() * 100L)
//                .putMetadata("productName", request.getProductName())
//                .setCurrency("usd")
//                .setAutomaticPaymentMethods(PaymentIntentCreateParams
//                        .AutomaticPaymentMethods
//                        .builder()
//                        .setEnabled(true)
//                        .build()).build();
//        PaymentIntent intent = PaymentIntent.create(params);
//        return new Response(intent.getId(), intent.getClientSecret());
//    }
}




