package com.spendwise.repository;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.spendwise.model.Account;
import com.spendwise.model.Transaction;

public interface TransactionRepository extends JpaRepository<Transaction, Long> {
    // List<Transaction> findAllByAccounts(List<Account> acc);
    List<Transaction> findAllByAccountsInAndCreatedAtBetween(
            List<Account> accounts,
            LocalDate startDate,
            LocalDate endDate);
}
