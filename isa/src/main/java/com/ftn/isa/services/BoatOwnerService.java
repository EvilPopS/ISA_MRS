package com.ftn.isa.services;

import com.ftn.isa.DTO.BoatOwnerDTO;
import com.ftn.isa.DTO.CottageOwnerDTO;
import com.ftn.isa.model.Boat;
import com.ftn.isa.model.BoatOwner;
import com.ftn.isa.model.Cottage;
import com.ftn.isa.model.CottageOwner;
import com.ftn.isa.repository.BoatOwnerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class BoatOwnerService {
    @Autowired
    private BoatOwnerRepository boatOwnerRepository;

    public BoatOwner findByEmail(String email) {
        return boatOwnerRepository.findByEmail(email);
    }

    public BoatOwner getOwnerByBoatId(Long boatId) {
        return boatOwnerRepository.getOwnerByCBoatId(boatId);
    }

    public boolean save(BoatOwnerDTO boatOwnerDTO, BoatOwner bo) {
        boatOwnerDTO.hashPassword();
        bo.updatePersonalInfo(boatOwnerDTO);
        boatOwnerRepository.save(bo);
        return true;
    }

    public void deleteBoat(BoatOwner boatOwner, Long id) throws Exception {
        for (Boat b : boatOwner.getBoats()){
            if (b.getId() == id){
                if (b.isDeleted())
                    throw new Exception("Cottage with this id is already deleted");
                else if (b.hasUpcomingReservations())
                    throw new Exception("Cottage cannot be deleted due to upcoming reservations");
                b.setDeleted(true);
                break;
            }
        }
        boatOwnerRepository.save(boatOwner);
    }

}
