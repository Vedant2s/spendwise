package com.spendwise.repository;


import org.springframework.data.jpa.repository.JpaRepository;

import com.spendwise.model.Currency;

public interface CurrencyRepository extends JpaRepository<Currency,Long>{

}
