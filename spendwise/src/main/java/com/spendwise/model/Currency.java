package com.spendwise.model;

import java.math.BigDecimal;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Getter;

@Getter
@Entity
public class Currency {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long currencyId;
    @Column (nullable = false)
    String currencyCode;
    @Column (nullable = false)
    char symbol;
    @Column (precision = 12,scale=2)
    BigDecimal exchangeRate;
}
