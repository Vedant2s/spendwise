package com.spendwise.entity;

import jakarta.persistence.*;
import java.math.BigDecimal;

@Entity
@Table(name="sub_category_budgets")
public class SubCategoryBudget {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long subCategoryBudgetID;

    private BigDecimal maxAllotedBudget; // Use BigDecimal for accurate financial calculations

    @ManyToOne
    @JoinColumn(name = "sub_category_id",nullable = false)
    private SubCategory subCategory;

    @ManyToOne
    @JoinColumn(name = "user_id",nullable = false)
    private User user;
}