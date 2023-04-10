package com.fundall.backend.paystack;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/payment")
public class PaystackController {

    @Autowired
    private PaystackService paystackService;

    @PostMapping("/initialize-payment/{cardId}")
    public ResponseEntity<?> initializePayment(@RequestParam String email, @RequestParam int amount, @PathVariable Long cardId) {
        // Create transaction
        JSONObject initializeResponse = paystackService.createTransaction(email, amount, cardId);

        // Return authorization URL to client
        String authorizationUrl = initializeResponse.getString("authorization_url");
        return ResponseEntity.ok().body(authorizationUrl);
    }

    @GetMapping("/verify-payment")
    public ResponseEntity<?> verifyPayment(@RequestParam String reference) {
        // Verify payment
        JSONObject verifyResponse = paystackService.verifyTransaction(reference);
        String status = verifyResponse.getString("status");
        int amountPaid = verifyResponse.getInt("amount");
        return ResponseEntity.ok().body("Payment status: " + status + ", Amount paid: " + amountPaid / 100.0 + " Naira");
    }
}