package com.spendwise.entity;

import jakarta.persistence.*;
import java.math.BigDecimal;
@Entity
@Table(name="transactions")
public class Transaction {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long transactionID;

    private String transactionType; // e.g., "Expense", "Income"

    private String timeStamp; // Store as String for simplicity (e.g., "2024-07-01")

    private BigDecimal amount; // Use BigDecimal for accurate financial calculations

    @ManyToOne
    @JoinColumn(name = "user_id",nullable = false)
    private User user;

    @ManyToOne
    @JoinColumn(name = "account_id",nullable = true)
    private BankAccount account;

    @ManyToOne
    @JoinColumn(name = "category_id",nullable = true)
    private Category category;

    @ManyToOne
    @JoinColumn(name = "sub_category_id",nullable = true)
    private SubCategory subCategory;

}