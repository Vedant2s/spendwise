package com.spendwise.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.spendwise.model.Transaction;

public interface TransactionRepository extends JpaRepository<Transaction,Long>{

}
