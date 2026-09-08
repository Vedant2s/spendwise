package com.spendwise.entity;

import java.util.List;

import jakarta.persistence.*;

@Entity  
// ✅ Marks this class as a JPA entity → it will be mapped to a database table
@Table(name="bank_accounts")
public class BankAccount {

    @Id
    // ✅ Declares this field as the Primary Key
    // → uniquely identifies each BankAccount record in the table

    @GeneratedValue(strategy = GenerationType.IDENTITY)
    // ✅ Auto-generates the primary key using database auto-increment
    // → DB assigns values like 1, 2, 3...
    // → commonly used with MySQL/PostgreSQL

    private Long accountID;

    private Long accountNumber;
    // ✅ Regular column → automatically mapped

    private String accountType;
    // ✅ Represents type of account (UPI / Credit / Debit, etc.)

    @ManyToOne
    // ✅ Defines relationship: Many BankAccounts → ONE User
    // → Multiple accounts can belong to a single user

    @JoinColumn(name = "user_id",nullable = false)
    // ✅ Specifies the Foreign Key column in this table
    // → "user_id" will be created in DB as a FK referencing User table
    // → links BankAccount to its owning User

    private User user;
    // ✅ Entity reference (not a raw ID)
    // → JPA uses this to manage relationship mapping internally

    @OneToMany(mappedBy = "account")
    private List<Transaction> transactions;

}
