package com.fundall.backend.card;

import java.math.BigDecimal;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fundall.backend.transaction.Transaction;
import com.fundall.backend.user.User;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity(name = "Card")
@Table(name = "card")
public class Card {
    
    @Id
    @SequenceGenerator(
        name = "card_sequence",
        sequenceName = "card_sequence",
        allocationSize = 1
    )
    @GeneratedValue(
        strategy = GenerationType.SEQUENCE,
        generator = "card_sequence"
    )
    @Column(
        name = "id",
        updatable = false
    )
    private Long id;

    @Column(
        name = "card_name",
        nullable = false,
        columnDefinition = "TEXT"
    )
    private String name;

    @Column(
        name = "balance",
        nullable = false,
        columnDefinition = "INTEGER default 0"
    )
    private BigDecimal balance;

    @Column(
        name = "credit_limit",
        nullable = false,
        columnDefinition = "INTEGER default 50000"
    )
    private BigDecimal creditLimit;

    @Enumerated(EnumType.STRING)
    @Column(
        name = "card_type",
        nullable = false,
        columnDefinition = "TEXT"
    )
    private CardType cardType;

    @OneToMany(mappedBy = "card")
    @JsonManagedReference
    private List<Transaction> transactions;

    @ManyToOne
    @JoinColumn(name = "user_id")
    @JsonBackReference
    private User user;
}
