package com.fundall.backend.merchant;

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
public class MerchantResponse {
    
    private String name;

    private User user;
    
    private List<Transaction> transactions;
}
