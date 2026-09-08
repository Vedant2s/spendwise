package com.spendwise.entity;

import java.math.BigDecimal;

import jakarta.persistence.*;

@Entity
@Table(name="category_budgets")
public class CategoryBudget {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long categoryBudgetID;

    private BigDecimal maxAllotedBudget; // Use BigDecimal for accurate financial calculations

    @ManyToOne
    @JoinColumn(name = "category_id",nullable = false)
    private Category category;

    @ManyToOne
    @JoinColumn(name = "user_id",nullable = false)
    private User user;

}