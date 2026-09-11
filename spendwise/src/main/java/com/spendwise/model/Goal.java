package com.spendwise.model;

import java.math.BigDecimal;
import java.time.LocalDate;

import jakarta.persistence.*;

@Entity
@Table(name="goals")
public class Goal {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long goalID;
    private String name;
    @Column(precision = 12, scale = 2)
    private BigDecimal targetAmount;
    @Column(precision = 12, scale = 2)
    private BigDecimal currentAmount;
    private LocalDate desired_date;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id",nullable = false)
    private User user;
}
