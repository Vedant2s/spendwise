package com.spendwise.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.spendwise.model.User;

public interface UserRepository extends JpaRepository<User,Long>{

    Optional<User> findByEmail(String email);
}
