package com.fundall.backend.card;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface CardRepository extends JpaRepository<Card, Long> {
    @Query("SELECT c FROM Card c WHERE c.id = :cardId AND c.user.email = :userEmail")
    Optional<Card> findByIdAndUserEmail(@Param("cardId") Long cardId, @Param("userEmail") String userEmail);
}
