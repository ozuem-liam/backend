package com.fundall.backend.transaction;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fundall.backend.card.Card;
import com.fundall.backend.merchant.Merchant;
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
@Entity(name = "Transaction")
@Table(name = "transaction")
public class Transaction {
    
    @Id
    @SequenceGenerator(
        name = "transaction_sequence",
        sequenceName = "transaction_sequence",
        allocationSize = 1
    )
    @GeneratedValue(
        strategy = GenerationType.SEQUENCE,
        generator = "transaction_sequence"
    )
    @Column(
        name = "id",
        updatable = false
    )
    private Long id;

    @Column(
        name = "amount",
        nullable = false,
        columnDefinition = "INT"
    )
    private Integer amount;

    @Column(
        name = "description",
        nullable = false,
        columnDefinition = "TEXT"
    )
    private String description;

    @Column(
        name = "date",
        nullable = false,
        columnDefinition = "DATE"
    )
    private Date date;

    @Enumerated(EnumType.STRING)
    @Column(
        name = "transaction_type",
        nullable = false,
        columnDefinition = "TEXT"
    )
    private TransactionType type;

    @ManyToOne
    @JoinColumn(name = "merchant_id")
    @JsonBackReference
    private Merchant merchant;

    @ManyToOne
    @JoinColumn(name = "card_id")
    @JsonBackReference
    private Card card;

    @ManyToOne
    @JoinColumn(name = "user_id")
    @JsonBackReference
    private User user;

}
