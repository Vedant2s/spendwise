package com.spendwise.repository;


import org.springframework.data.jpa.repository.JpaRepository;

import com.spendwise.model.Category;

public interface CategoryRepository extends JpaRepository<Category,Long>{

}