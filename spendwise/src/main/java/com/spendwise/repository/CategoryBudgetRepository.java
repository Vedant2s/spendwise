package com.spendwise.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.spendwise.model.CategoryBudget;

public interface CategoryBudgetRepository extends JpaRepository<CategoryBudget,Long> {

}
