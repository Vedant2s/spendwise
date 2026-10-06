package com.spendwise.model;

import java.math.BigDecimal;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Entity
@Table(name="category_budgets")
@Builder
@Getter 
@AllArgsConstructor
@NoArgsConstructor
public class CategoryBudget {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long categoryBudgetId;
    private String name;
    private BigDecimal maxAllotedBudget;
    @ManyToOne
    @JoinColumn(name = "account_id",nullable = false)
    private Account account;
    @ManyToOne
    @JoinColumn(name = "category_id",nullable = false)
    private Category category;
}
