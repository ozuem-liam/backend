package com.fundall.backend.user;

import java.util.Collection;
import java.util.List;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fundall.backend.card.Card;
import com.fundall.backend.merchant.Merchant;
import com.fundall.backend.transaction.Transaction;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity(name = "User")
@Table(
    name = "_user",
    uniqueConstraints = {
        @UniqueConstraint(name = "user_email_unique", columnNames = "email")
    }    
)
public class User implements UserDetails {

    @Id
    @SequenceGenerator(
        name = "user_sequence",
        sequenceName = "user_sequence",
        allocationSize = 1
    )
    @GeneratedValue(
        strategy = GenerationType.SEQUENCE,
        generator = "user_sequence"
    )
    @Column(
        name = "id",
        updatable = false
    )
    private Long id;

    @Column(
        name = "first_name",
        nullable = false,
        columnDefinition = "TEXT"
    )
    private String firstname;
    
    @Column(
        name = "last_name",
        nullable = false,
        columnDefinition = "TEXT"
    )
    private String lastname;
    
    @Column(
        name = "phone_number",
        nullable = false,
        columnDefinition = "TEXT"
    )
    private String phone;
    
    @Column(
        name = "email",
        nullable = false,
        columnDefinition = "TEXT"
    )
    private String email;
    
    @Column(
        name = "password",
        nullable = false,
        columnDefinition = "TEXT"
    )
    private String password;

    @Column(
        name = "card_to_top_up",
        nullable = true,
        columnDefinition = "INTEGER"
    )
    private Long cardToTopUp;

    @Enumerated(EnumType.STRING)
    @Column(
        name = "role",
        nullable = false,
        columnDefinition = "TEXT"
    )
    private Role role;

    @OneToMany(mappedBy = "user")
    @JsonManagedReference
    private List<Card> cards;

    @OneToMany(mappedBy = "user")
    @JsonManagedReference
    private List<Transaction> transactions;
    
    @OneToMany(mappedBy = "user")
    @JsonManagedReference
    private List<Merchant> merchants;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority(role.name()));
    }

    @Override
    public String getUsername() {
        return email;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    @Override
    public String getPassword() {
        return password;
    }

}
