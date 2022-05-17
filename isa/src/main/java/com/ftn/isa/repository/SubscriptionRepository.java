package com.ftn.isa.repository;

import com.ftn.isa.model.Subscription;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface SubscriptionRepository extends JpaRepository<Subscription, Long> {

    @Query(nativeQuery = true, value = "select * from subscription")
    public List<Subscription> getAllSubscriptions();
}
