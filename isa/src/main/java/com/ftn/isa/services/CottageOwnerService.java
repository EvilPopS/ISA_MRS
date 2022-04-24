package com.ftn.isa.services;

import com.ftn.isa.DTO.CottageOwnerDTO;
import com.ftn.isa.helpers.Validate;
import com.ftn.isa.model.Cottage;
import com.ftn.isa.model.CottageOwner;
import com.ftn.isa.model.FishingInstructor;
import com.ftn.isa.repository.CottageOwnerRepository;
import com.ftn.isa.helpers.WrongInputException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CottageOwnerService {

    @Autowired
    public CottageOwnerRepository cottageOwnerRepository;

    public CottageOwner findByEmail(String email) {
        return cottageOwnerRepository.findByEmail(email);
    }

    public boolean save(CottageOwnerDTO cottageOwnerData, CottageOwner co) {
        co.updatePersonalInfo(cottageOwnerData);
        cottageOwnerRepository.save(co);
        return true;
    }

    public void save(CottageOwner cottageOwner) {
        cottageOwnerRepository.save(cottageOwner);
    }


    public void deleteCottage(CottageOwner cottageOwner, Long id) throws Exception {
        for (Cottage c : cottageOwner.getCottages()){
            if (c.getId() == id){
                if (c.isDeleted()) throw new Exception("Cottage with this id is already deleted");
                c.setDeleted(true);
                break;
            }
        }
        cottageOwnerRepository.save(cottageOwner);
    }

}
