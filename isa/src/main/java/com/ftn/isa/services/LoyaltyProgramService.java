package com.ftn.isa.services;

import com.ftn.isa.model.Client;
import com.ftn.isa.model.LoyaltyProgram;
import com.ftn.isa.repository.LoyaltyProgramRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class LoyaltyProgramService {

    @Autowired
    private LoyaltyProgramRepository loyaltyProgramRepository;

    public List<LoyaltyProgram>  getAllLoyaltyPrograms() {return loyaltyProgramRepository.getAllLoyaltyPrograms();}

    public Double getClientDiscount(Client client) {
        for (LoyaltyProgram lp : getAllLoyaltyPrograms())
            if (lp.getLoyaltyType() == client.getLoyaltyType())
                return lp.getDiscount();
        return 0.0;
    }
}
