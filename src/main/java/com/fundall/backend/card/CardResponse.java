package com.fundall.backend.card;

import java.math.BigDecimal;
import java.util.List;

import com.fundall.backend.transaction.Transaction;
import com.fundall.backend.user.User;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CardResponse {
    
    private String name;

    private BigDecimal balance;

    private BigDecimal creditLimit;

    private CardType cardType;

    private List<Transaction> transactions;

    private User user;
}
