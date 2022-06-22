package com.ftn.isa.services;

import com.ftn.isa.model.Subscription;
import com.ftn.isa.repository.SubscriptionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SubscriptionService {

    @Autowired
    private SubscriptionRepository subscriptionRepository;

    public List<Subscription> getAllSubscriptions() {return subscriptionRepository.getAllSubscriptions();}

}
