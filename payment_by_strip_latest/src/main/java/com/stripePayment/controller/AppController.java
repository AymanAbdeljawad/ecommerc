package com.stripePayment.controller;


import com.stripePayment.model.PaymentNotification;
import com.stripePayment.model.Request;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;

@Controller
public class AppController {
    @Value("${stripe.api.publicKey}")
    private String publicKey;

    @GetMapping("/")
    public String home(Model model) {
        Request request = new Request();
        request.setAmount((long) 1300.1);
        model.addAttribute("request", request);
        return "index";
    }

    @PostMapping("/")
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


    private final WebClient webClient;

    @Autowired
    public AppController(WebClient.Builder webClientBuilder) {
        this.webClient = webClientBuilder.baseUrl("http://localhost:8091").build();
    }

    @GetMapping("/payment-success")
    public String paymentSuccess(@RequestParam(value = "amount", defaultValue = "N/A") String amount,
                                 @RequestParam(value = "productName", defaultValue = "N/A") String productName,
                                 @RequestParam(value = "payment_intent", required = false) String paymentIntentId,
                                 Model model) {
        if (paymentIntentId != null) {
            // Logic to retrieve PaymentIntent and check status (omitted for brevity)
            boolean paymentSucceeded = checkPaymentStatus(paymentIntentId);
            if (paymentSucceeded) {
                notifyPaymentSuccess(amount, productName);
            } else {
                notifyPaymentFailure();
            }
        }

        model.addAttribute("amount", amount);
        model.addAttribute("productName", productName);
        return "success";
    }

    private boolean checkPaymentStatus(String paymentIntentId) {
        // Implement your logic to check payment status
        return true; // Assume payment succeeded for example purposes
    }

    private void notifyPaymentSuccess(String amount, String productName) {
        webClient.post()
                .uri("/pay-success")
                .header(HttpHeaders.CONTENT_TYPE, "application/json")
                .body(BodyInserters.fromValue(new PaymentNotification(amount, productName)))
                .retrieve()
                .bodyToMono(String.class)
                .doOnNext(response -> System.out.println("Payment success notification sent successfully."))
                .doOnError(error -> System.err.println("Failed to send payment success notification. Error: " + error.getMessage()))
                .subscribe();
    }

    private void notifyPaymentFailure() {
        webClient.post()
                .uri("/pay-failure")
                .retrieve()
                .bodyToMono(String.class)
                .doOnNext(response -> System.out.println("Payment failure notification sent successfully."))
                .doOnError(error -> System.err.println("Failed to send payment failure notification. Error: " + error.getMessage()))
                .subscribe();
    }

    @PostMapping("/pay-success")
    public String paymentSuccess(@RequestParam(value = "amount", required = false) String amount,
                                 @RequestParam(value = "productName", required = false) String productName,
                                 Model model) {
        // معالجة البيانات وتمريرها إلى الصفحة
        model.addAttribute("amount", amount != null ? amount : "Not available");
        model.addAttribute("productName", productName != null ? productName : "Not available");
        return "success";
    }

    @GetMapping("/pay-failure")
    public String paymentFailure(@RequestParam("errorMessage") String errorMessage, Model model) {
        model.addAttribute("errorMessage", errorMessage);
        return "failure";
    }
}