package com.fundall.backend.paystack;

import java.math.BigDecimal;
import java.util.Optional;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.fundall.backend.card.Card;
import com.fundall.backend.card.CardRepository;
import com.fundall.backend.user.User;
import com.fundall.backend.user.UserService;

@RestController
@RequestMapping("/webhook")
public class PaystackWebhookController {

    @Autowired
    private UserService userService;

    @Autowired
    private CardRepository cardRepository;

    @Autowired
    private PaystackService paystackService;

    @PostMapping("/payment/success")
    public ResponseEntity<?> handlePaymentSuccess(@RequestBody JSONObject payload) {
        String reference = payload.getString("reference");
        JSONObject verifyResponse = paystackService.verifyTransaction(reference);
        if (verifyResponse == null) {
            return ResponseEntity.badRequest().build();
        }
        String status = verifyResponse.getString("status");
        if (!status.equals("success")) {
            return ResponseEntity.badRequest().build();
        }
        int amountPaid = verifyResponse.getInt("amount");
        String email = verifyResponse.getJSONObject("data").getJSONObject("customer").getString("email");
        User user = userService.findUserByEmail(email);
        if (user == null) {
            return ResponseEntity.badRequest().build();
        }
        Optional<Card> optionalCard = cardRepository.findByIdAndUserEmail(user.getCardToTopUp(), email);
        if (optionalCard.isPresent()) {
            Card card = optionalCard.get();
            card.setBalance(card.getBalance().add(BigDecimal.valueOf(amountPaid).divide(BigDecimal.valueOf(100))));
            cardRepository.save(card);
        }
        return ResponseEntity.ok().build();
    }
}
