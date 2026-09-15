package com.spendwise.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.spendwise.model.Account;

public interface AccountRepository extends JpaRepository<Account,Long>{

}
