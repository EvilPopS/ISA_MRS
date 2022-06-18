package com.ftn.isa.services;


import com.ftn.isa.DTO.AdventureDTO;
import com.ftn.isa.DTO.FishingInstructorDTO;
import com.ftn.isa.helpers.Validate;
import com.ftn.isa.model.*;
import com.ftn.isa.repository.FishingInstructorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class FishingInstructorService {

    @Autowired
    private FishingInstructorRepository fishingInstructorRepo;

    public FishingInstructor findByEmail(String email){return fishingInstructorRepo.findByEmail(email);}

    public void updatePersonalInfo(FishingInstructorDTO fishingInstructorData, FishingInstructor fishingInstructor) {
        fishingInstructorData.hashPassword();
        fishingInstructor.updatePersonalInfo(fishingInstructorData);
        fishingInstructorRepo.save(fishingInstructor);
    }

    public boolean checkIfCurrentResInProgress(Client client, FishingInstructor fishingInstructor, List<Reservation> reservations) {
        for (Reservation res : reservations){
            if (Validate.getTodaysDate().isAfter(res.getStartTime()) && Validate.getTodaysDate().isBefore(res.getEndTime())
                    && !res.isCanceled() && res.isReserved() && res.getClient().getId().equals(client.getId()))
            {
                for (Adventure a : fishingInstructor.getAdventures())
                    if (a.getId().equals(res.getRental().getId())) return true;
            }
        }
        return false;
    }

    public FishingInstructor getOwnerByAdventureId(Long advId) {
        return fishingInstructorRepo.getOwnerByAdventureId(advId);
    }
    public void save(FishingInstructor fishingInstructor){ fishingInstructorRepo.save(fishingInstructor);};

    public void updateAdventure(FishingInstructor fishingInstructor, AdventureDTO adventureData) {
        fishingInstructor.updateAdventure(adventureData);
        fishingInstructorRepo.save(fishingInstructor);
    }

    public void deleteAdventure(FishingInstructor fishingInstructor, Long id) {
        fishingInstructor.findAdventureById(id).setDeleted(true);
        fishingInstructorRepo.save(fishingInstructor);

    }

    public boolean checkIfAdventureExists(FishingInstructor fishingInstructor, Long adventureId) {
        for (Adventure a : fishingInstructor.getAdventures()){
            if (a.getId().equals(adventureId))
                return true;
        }
        return false;
    }

    public boolean updateLoyaltyProgram(FishingInstructor fishingInstructor, List<LoyaltyProgram> programs, String newProgram) {
        LoyaltyType type = LoyaltyType.REGULAR;
        switch (newProgram) {
            case "BRONZE":
                type = LoyaltyType.BRONZE;
                break;
            case "SILVER":
                type = LoyaltyType.SILVER;
                break;
            case "GOLD":
                type = LoyaltyType.GOLD;
                break;
        }

        for (LoyaltyProgram lp : programs) {
            if (lp.getLoyaltyType() == type) {
                int leftPoints = fishingInstructor.getLoyaltyPoints() - lp.getPrice();
                if (leftPoints >= 0)
                    fishingInstructor.setLoyaltyPoints(leftPoints);
                else
                    return false;

                fishingInstructor.setLoyaltyType(type);
                fishingInstructorRepo.save(fishingInstructor);
                return true;
            }
        }
        return false;
    }

}
