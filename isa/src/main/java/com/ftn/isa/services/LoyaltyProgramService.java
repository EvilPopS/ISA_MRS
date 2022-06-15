package com.ftn.isa.services;

import com.ftn.isa.model.LoyaltyProgram;
import com.ftn.isa.repository.LoyaltyProgramRepository;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class LoyaltyProgramService {

    @Autowired
    LoyaltyProgramRepository loyaltyProgramRepository;

    public List<LoyaltyProgram> getCompleteLoyaltyProgram(){
        return loyaltyProgramRepository.getCompleteLoyaltyProgram();
    }

    public void updateLoyaltyProgram(List<LoyaltyProgram> loyaltyPrograms) {
        for (LoyaltyProgram sentLP : loyaltyPrograms){
            for (LoyaltyProgram dbLP : loyaltyProgramRepository.getCompleteLoyaltyProgram()){
                if (dbLP.getLoyaltyType().equals(sentLP.getLoyaltyType())){
                    dbLP.setDiscount(sentLP.getDiscount());
                    dbLP.setIncrease(sentLP.getIncrease());
                    dbLP.setPrice(sentLP.getPrice());
                    loyaltyProgramRepository.save(dbLP);
                }
            }
        }
    }
}
