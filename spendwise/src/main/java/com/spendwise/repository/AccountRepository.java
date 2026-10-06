package com.spendwise.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import com.spendwise.model.Account;
import java.util.List;
import com.spendwise.model.User;

public interface AccountRepository extends JpaRepository<Account, Long> {
    Optional<Account> findByName(String name);
    Optional<Account> findByAccountIdAndUser(Long accountId, User user);


    List<Account> findByUser(User user);
}
