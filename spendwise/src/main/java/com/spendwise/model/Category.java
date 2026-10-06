package com.spendwise.model;


import jakarta.persistence.*;

@Entity
@Table(name="categories")
public class Category {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long categoryId;
    private String categoryName;
    private String description;
    @ManyToOne
    @JoinColumn(name = "user_id",nullable=true)
    private User user;
}
