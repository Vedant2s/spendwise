package com.spendwise.repository;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.spendwise.model.Account;
import com.spendwise.model.Transaction;
import com.spendwise.model.TransactionType;

public interface TransactionRepository extends JpaRepository<Transaction, Long> {
    // List<Transaction> findAllByAccounts(List<Account> acc);
    List<Transaction> findAllByAccountInAndTimeStampBetween(
            List<Account> account,
            LocalDate startDate,
            LocalDate endDate);

    List<Transaction> findAllByAccountInAndTimeStampBetweenOrderByTimeStampDesc(List<Account> account,
            LocalDate startDate,
            LocalDate endDate);

    @Query("""
                SELECT t
                FROM Transaction t
                WHERE t.account = :account
                  AND t.transactionType = :type
                  AND t.timeStamp BETWEEN :startDate AND :endDate
                GROUP BY t.category.categoryName
                ORDER BY SUM(t.amount) DESC
            """)
    List<Transaction> findExpensesByCategory(
            @Param("account") Account account,
            @Param("type") TransactionType type,
            @Param("startDate") LocalDate startDate,
            @Param("endDate") LocalDate endDate);
}
