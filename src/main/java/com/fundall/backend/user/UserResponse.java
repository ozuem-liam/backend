package com.fundall.backend.user;

import java.util.List;

import com.fundall.backend.card.Card;
import com.fundall.backend.transaction.Transaction;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserResponse {
    
    private String firstname;
    private String lastname;
    private String phone;
    private String email;
    private List<Card> cards;
    private List<Transaction> transactions;
}
