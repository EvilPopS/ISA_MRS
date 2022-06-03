package com.ftn.isa.services;

import com.ftn.isa.DTO.BoatDTO;
import com.ftn.isa.DTO.BoatOwnerDTO;
import com.ftn.isa.DTO.CottageDTO;
import com.ftn.isa.model.*;
import com.ftn.isa.repository.BoatOwnerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Set;

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

    public void save(BoatOwner boatOwner) {
        boatOwnerRepository.save(boatOwner);
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

    public void save(BoatOwner boatOwner, BoatDTO boatDTO, Set<Photo> photos){
        for (Boat boat : boatOwner.getBoats()){
            if (boat.getId() == boatDTO.getId()) {
                boat.setPhotos(photos);
                boat.getAddress().setPlaceName(boatDTO.getCity());
                boat.getAddress().setCountry(boatDTO.getCountry());
                boat.getAddress().setStreet(boatDTO.getStreet());
                boat.getAddress().setLon(boatDTO.getLon());
                boat.getAddress().setLat(boatDTO.getLat());
                boat.setNavigationEquipment(boatDTO.getNavigationEquipment());
                boat.setFishingEquipment(boatDTO.getFishingEquipment());
                boat.setCapacity(boatDTO.getCapacity());
                boat.setDescription(boatDTO.getDescription());
                boat.setName(boatDTO.getName());
                boat.setPrice(boatDTO.getPrice());
                boat.setRules(boatDTO.getRules());
                boat.setType(boatDTO.getType());
                boat.setBoatLength(boatDTO.getBoatLength());
                boat.setEngineNumber(boatDTO.getEngineNumber());
                boat.setEnginePower(boatDTO.getEnginePower());
                boat.setMaxSpeed(boatDTO.getMaxSpeed());
            }
        }

        boatOwnerRepository.save(boatOwner);
    }

    public void addNewBoat(BoatDTO boatDTO, BoatOwner boatOwner) {
        Set<Photo> photos = new HashSet<>();
        for (String p : boatDTO.getPhotos())
            photos.add(new Photo(p));

        Address address = new Address(boatDTO.getCountry(), boatDTO.getCity(), boatDTO.getStreet(),
                boatDTO.getLon(), boatDTO.getLat());

        Boat boat = new Boat(boatDTO.getName(), boatDTO.getDescription(),
                boatDTO.getCapacity(), boatDTO.getRules(),
                false, address, boatDTO.getAverageRating(), boatDTO.getNoRatings(),
                RentalType.BOAT, boatDTO.getPrice(), boatDTO.getType(), boatDTO.getBoatLength(), boatDTO.getEngineNumber(),
                boatDTO.getEnginePower(), boatDTO.getMaxSpeed(), boatDTO.getNavigationEquipment(), boatDTO.getFishingEquipment());

        boat.setPhotos(photos);
        boat.setAddress(address);
        boatOwner.getBoats().add(boat);
        this.save(boatOwner);
    }

}
