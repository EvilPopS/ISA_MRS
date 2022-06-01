package com.ftn.isa.services;


import com.ftn.isa.DTO.AdventureDTO;
import com.ftn.isa.DTO.FishingInstructorDTO;
import com.ftn.isa.model.FishingInstructor;
import com.ftn.isa.repository.FishingInstructorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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

}
