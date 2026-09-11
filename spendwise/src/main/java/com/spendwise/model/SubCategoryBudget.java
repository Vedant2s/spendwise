package com.spendwise.model;

import jakarta.persistence.*;
import java.math.BigDecimal;

@Entity
@Table(name="sub_category_budgets")
public class SubCategoryBudget {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long subCategoryBudgetID;
    @Column(precision = 12, scale = 2)
    private BigDecimal maxAllotedBudget;
    @ManyToOne
    @JoinColumn(name = "sub_category_id",nullable = false)
    private SubCategory subCategory;
}
