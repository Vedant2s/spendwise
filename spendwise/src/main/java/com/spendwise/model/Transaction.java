package com.spendwise.model;

import jakarta.persistence.*;
import lombok.Getter;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name="transactions")
@Getter
public class Transaction {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long transactionID;
    @Enumerated(EnumType.STRING)
    private TransactionType transactionType;
    private LocalDate timeStamp;
    @Column(precision = 12, scale = 2)
    private BigDecimal amount;
    @ManyToOne
    @JoinColumn(name = "user_id",nullable = false)
    private User user;
    @ManyToOne
    @JoinColumn(name = "account_id",nullable = true)
    private Account account;
    @ManyToOne
    @JoinColumn(name = "category_id",nullable = true)
    private Category category;
    @ManyToOne
    @JoinColumn(name = "sub_category_id",nullable = true)
    private SubCategory subCategory;
}
