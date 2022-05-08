package com.ftn.isa.services;

import com.ftn.isa.DTO.CottageDTO;
import com.ftn.isa.DTO.CottageOwnerDTO;
import com.ftn.isa.DTO.OwnersSearchResDTO;
import com.ftn.isa.helpers.Validate;
import com.ftn.isa.model.*;
import com.ftn.isa.repository.CottageOwnerRepository;
import com.ftn.isa.helpers.WrongInputException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

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

    public void save(CottageOwner cottageOwner, CottageDTO cottageDTO, Set<Photo> photos){
        for (Cottage cottage : cottageOwner.getCottages()){
            if (cottage.getId() == cottageDTO.getId()) {
                cottage.setPhotos(photos);
                cottage.getAddress().setPlaceName(cottageDTO.getCity());
                cottage.getAddress().setCountry(cottageDTO.getCountry());
                cottage.getAddress().setStreet(cottageDTO.getStreet());
                cottage.getAddress().setLon(cottageDTO.getLon());
                cottage.getAddress().setLat(cottageDTO.getLat());
                cottage.setAdditionalServices(cottageDTO.getAdditionalServices());
                cottage.setNoRooms(cottageDTO.getNoRooms());
                cottage.setCapacity(cottageDTO.getCapacity());
                cottage.setName(cottageDTO.getName());
                cottage.setPrice(cottageDTO.getPrice());
                cottage.setRules(cottageDTO.getRules());
            }
        }

        cottageOwnerRepository.save(cottageOwner);
    }


    public void deleteCottage(CottageOwner cottageOwner, Long id) throws Exception {
        for (Cottage c : cottageOwner.getCottages()){
            if (c.getId() == id){
                if (c.isDeleted())
                    throw new Exception("Cottage with this id is already deleted");
                else if (c.hasUpcomingReservations())
                    throw new Exception("Cottage cannot be deleted due to upcoming reservations");
                c.setDeleted(true);
                break;
            }
        }
        cottageOwnerRepository.save(cottageOwner);
    }

    public List<OwnersSearchResDTO> search(CottageOwner cottageOwner, String minPrice, String maxPrice, String location, String minCapacity, String serviceName) {
        List<OwnersSearchResDTO> rentals = new ArrayList<>();
        for (Cottage c : cottageOwner.getCottages()){
            if (!c.isDeleted() && (c.getPrice() >= Double.parseDouble(minPrice))
                    && (c.getPrice() <= Double.parseDouble(maxPrice)) && (c.getAddress().getPlaceName().equalsIgnoreCase(location))
                    && (c.getCapacity() >= Double.parseDouble(minCapacity))) {
                if (serviceName.equals(""))
                    rentals.add(new OwnersSearchResDTO(c));
                else
                    if ((c.getName().equalsIgnoreCase(serviceName)))
                        rentals.add(new OwnersSearchResDTO(c));
            }
        }
        return rentals;
    }
}
