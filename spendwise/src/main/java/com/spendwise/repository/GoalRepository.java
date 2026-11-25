package com.spendwise.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.spendwise.model.Goal;
import com.spendwise.model.User;

public interface GoalRepository extends JpaRepository<Goal,Long> {
    List<Goal> findByUser(User user);
}
