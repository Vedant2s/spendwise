package com.spendwise.model;

import java.math.BigDecimal;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import com.spendwise.dto.Type;

@Entity
@Table(name = "accounts")
@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class Account {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long accountId;
    @Column(nullable = true)
    private String accountNumber;
    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private Type accountType;
    @Column(nullable = false)
    private String name;
    @Column(precision = 12, scale = 2)
    private BigDecimal currentBalance;
    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;
    @ManyToOne
    @JoinColumn(name = "currency_id", nullable = false)
    private Currency currency;
}
