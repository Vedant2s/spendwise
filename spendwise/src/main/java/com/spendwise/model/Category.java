package com.spendwise.model;


import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;

@Entity
@Table(name="categories")
@Builder 
@Getter
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
