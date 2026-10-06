package com.spendwise.model;

import java.math.BigDecimal;
import java.time.LocalDate;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name="goals")
@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor 
public class Goal {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long goalId;
    private String name;
    @Column(precision = 12, scale = 2)
    private BigDecimal targetAmount;
    @Column(precision = 12, scale = 2)
    private BigDecimal currentAmount;
    private LocalDate desiredDate;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id",nullable = false)
    private User user;
}
