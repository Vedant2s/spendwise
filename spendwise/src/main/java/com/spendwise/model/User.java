package com.spendwise.model;

import jakarta.persistence.*;
import java.math.BigDecimal;

@Entity
@Table(name = "users")
public class User {

    public User(String username, String email, String passwordHash, BigDecimal monthlyIncome) {
		this.username=username;
        this.email=email;
        this.passwordHash=passwordHash;
        this.monthlyIncome=monthlyIncome;
	}
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long userID;
    @Column(nullable = false)
    private String username;
    @Column(nullable = false, unique = true)
    private String email;
    @Column(nullable = false)
    private String passwordHash;
    @Column(precision = 12, scale = 2)
    private BigDecimal monthlyIncome;
}
