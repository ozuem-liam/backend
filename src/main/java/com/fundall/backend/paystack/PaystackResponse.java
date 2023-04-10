package com.fundall.backend.paystack;

import java.math.BigDecimal;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PaystackResponse {
    
    private String email;

    private BigDecimal amount;

    private String reference;
}
