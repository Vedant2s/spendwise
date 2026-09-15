package com.spendwise.repository;


import org.springframework.data.jpa.repository.JpaRepository;

import com.spendwise.model.Subscription;

public interface SubscriptionRepository extends JpaRepository<Subscription,Long>{

}
