package com.fundall.backend.transaction;

import java.util.List;
import java.util.Map;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class TransactionTypeSummaryResponse {

    private Map<TransactionType, List<Transaction>> transactionsByType;
    private Double totalIncome;
    private Double totalExpense;
    private Double totalCardBalance;
}
