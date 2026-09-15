package com.spendwise.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.spendwise.model.Goal;

public interface GoalRepository extends JpaRepository<Goal,Long> {

}
