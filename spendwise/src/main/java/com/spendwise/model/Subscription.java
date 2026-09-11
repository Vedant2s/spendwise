package com.spendwise.model;

import java.math.BigDecimal;
import java.time.LocalDate;

import jakarta.persistence.*;

enum BillingCycle {
    MONTHLY,
    YEARLY
}

@Entity
@Table(name="subscriptions")
public class Subscription {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long subscriptionID;
    private String subscriptionName;
    @Column(precision = 12, scale = 2)
    private BigDecimal price;
    @OneToOne
    @JoinColumn(name = "user_id",nullable = false,unique = true)
    private User user;
    @Enumerated(EnumType.STRING)
    private BillingCycle billingCycle;
    private LocalDate nextBillingDate;
}
