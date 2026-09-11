package com.spendwise.model;

import java.math.BigDecimal;

import jakarta.persistence.*;

@Entity
@Table(name="category_budgets")
public class CategoryBudget {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long categoryBudgetID;
    private String name;
    private BigDecimal maxAllotedBudget;
    @ManyToOne
    @JoinColumn(name = "account_id",nullable = false)
    private Account account;
    @ManyToOne
    @JoinColumn(name = "category_id",nullable = false)
    private Category category;
}
