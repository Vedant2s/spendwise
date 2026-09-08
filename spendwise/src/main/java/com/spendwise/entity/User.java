package com.spendwise.entity;
import jakarta.persistence.*;  // ✅ Required for JPA annotations
import java.math.BigDecimal;
import java.util.List;

@Entity  // ✅ Marks this class as a JPA entity → mapped to a database table
@Table(name="users")
public class User {

    @Id 
    // ✅ Declares this field as the Primary Key of the table
    // → uniquely identifies each User record

    @GeneratedValue(strategy = GenerationType.IDENTITY)
    // ✅ Auto-generates the ID using database auto-increment
    // → Database (MySQL/Postgres) assigns IDs like 1, 2, 3...
    // → Best for simple applications like SpendWise

    private Long userID;
    @Column(nullable = false)
    private String username;
    // ✅ Normal column (automatically mapped)
    // → No annotation needed for simple fields
    @Column(nullable = false, unique = true)
    private String email;
    // ✅ Should ideally be UNIQUE (add @Column(unique = true) later)
    @Column(nullable = false)
    private String passwordHash;
    // ✅ Stores hashed password (NOT plain password)
    // → Used for secure authentication

    @Column(precision = 12, scale = 2)
    // ✅ Maps BigDecimal to DECIMAL(12,2) in DB
    // → precision = total digits (12)
    // → scale = digits after decimal (2)
    // Example: 1234567890.12
    // ✅ Ensures accurate storage for financial data

    private BigDecimal monthlyIncome;

    // ✅ 1. User → Categories
    @OneToMany(mappedBy = "user")
    private List<Category> categories;

    // ✅ 2. User → Bank Accounts
    @OneToMany(mappedBy = "user")
    private List<BankAccount> bankAccounts;

    // ✅ 3. User → Category Budgets
    @OneToMany(mappedBy = "user")
    private List<CategoryBudget> categoryBudgets;

    // ✅ 4. User → Transactions
    @OneToMany(mappedBy = "user")
    private List<Transaction> transactions;

    // ✅ 5. User → Premium Subscription
    @OneToOne(mappedBy = "user")
    private Subscription subscription;

    @OneToMany(mappedBy = "user")
    private List<SubCategoryBudget> subCategoryBudgets;

}