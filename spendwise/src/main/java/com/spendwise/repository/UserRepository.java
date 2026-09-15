package com.spendwise.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.spendwise.model.User;

public interface UserRepository extends JpaRepository<User,Long>{

}
