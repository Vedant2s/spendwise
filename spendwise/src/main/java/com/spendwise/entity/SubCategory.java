package com.spendwise.entity;

import java.util.List;

import jakarta.persistence.*;

@Entity
@Table(name="sub_categories")
public class SubCategory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long subCategoryID;

    private String subCategoryName;

    @ManyToOne
    @JoinColumn(name = "category_id",nullable = false)
    private Category category;

    @OneToMany(mappedBy = "subCategory")
    private List<SubCategoryBudget> subCategoryBudgets;

    @OneToMany(mappedBy = "subCategory")
    private List<Transaction> transactions;
}