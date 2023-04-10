package com.fundall.backend.transaction;

import java.util.List;
import java.util.Map;

import com.fundall.backend.merchant.Merchant;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class TransactionByMerchantResponse {
    
    private Map<Merchant, List<Transaction>> transactionsByMerchant;

    private Integer numMerchants;
}
