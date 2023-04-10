package com.fundall.backend.transaction;

import org.springframework.data.jpa.repository.JpaRepository;

public interface Transactionrepository extends JpaRepository<Transaction, Long> {
    
}
