package com.ftn.isa.services;

import com.ftn.isa.DTO.CottageOwnerDTO;
import com.ftn.isa.helpers.Validate;
import com.ftn.isa.model.CottageOwner;
import com.ftn.isa.repository.CottageOwnerRepository;
import com.ftn.isa.helpers.WrongInputException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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

}
