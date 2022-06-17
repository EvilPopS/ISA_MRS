package com.ftn.isa.services;

import com.ftn.isa.model.LoyaltyProgram;
import org.springframework.beans.factory.annotation.Autowired;
import com.ftn.isa.model.Client;
import com.ftn.isa.repository.LoyaltyProgramRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class LoyaltyProgramService {

    @Autowired
    private LoyaltyProgramRepository loyaltyProgramRepository;

    public List<LoyaltyProgram> getCompleteLoyaltyProgram(){
        return loyaltyProgramRepository.getCompleteLoyaltyProgram();
    }
    public List<LoyaltyProgram>  getAllLoyaltyPrograms() {
        return loyaltyProgramRepository.getAllLoyaltyPrograms();
    }

    public void updateLoyaltyProgram(List<LoyaltyProgram> loyaltyPrograms) {
        for (LoyaltyProgram sentLP : loyaltyPrograms) {
            for (LoyaltyProgram dbLP : loyaltyProgramRepository.getCompleteLoyaltyProgram()) {
                if (dbLP.getLoyaltyType().equals(sentLP.getLoyaltyType())) {
                    dbLP.setDiscount(sentLP.getDiscount());
                    dbLP.setIncrease(sentLP.getIncrease());
                    dbLP.setPrice(sentLP.getPrice());
                    loyaltyProgramRepository.save(dbLP);
                }
            }
        }
    }

    public Double getClientDiscount(Client client) {
        for (LoyaltyProgram lp : getAllLoyaltyPrograms())
            if (lp.getLoyaltyType() == client.getLoyaltyType())
                return lp.getDiscount();
        return 0.0;
    }
}
