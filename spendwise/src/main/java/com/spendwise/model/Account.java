package com.spendwise.model;

import java.math.BigDecimal;

import jakarta.persistence.*;

@Entity
@Table(name="accounts")
public class Account {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long accountID;
    private String accountNumber;
    private String accountType;
    @Column(precision = 12, scale = 2)
    private BigDecimal curentBalance;
    @ManyToOne
    @JoinColumn(name = "user_id",nullable = false)
    private User user;
}
