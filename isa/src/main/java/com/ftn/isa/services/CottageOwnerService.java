package com.ftn.isa.services;

import com.ftn.isa.DTO.CottageOwnerDTO;
import com.ftn.isa.model.CottageOwner;
import com.ftn.isa.repository.CottageOwnerRepository;
import com.ftn.isa.utils.InputValidator;
import com.ftn.isa.utils.WrongInputException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CottageOwnerService {

    @Autowired
    public CottageOwnerRepository cottageOwnerRepository;

    public CottageOwner findByEmail(String email) {
        return cottageOwnerRepository.findByEmail(email);
    }

    public boolean save(CottageOwnerDTO cottageOwnerData, CottageOwner co) throws WrongInputException {
        //if (InputValidator.checkIfEmpty(cottageOwnerData.getPassword()) || InputValidator.isStrongPassword(cottageOwnerData.getPassword())) {
        //    throw new WrongInputException("Password is not correct. Password need to have at least one digit, one special symbol!");
        //}
        if (InputValidator.checkIfEmpty(cottageOwnerData.getName()) || InputValidator.containDigit(cottageOwnerData.getName())) {
            throw new WrongInputException("Name cannot contain digit or be empty!");
        } else if (InputValidator.checkIfEmpty(cottageOwnerData.getSurname()) || InputValidator.containDigit(cottageOwnerData.getSurname())) {
            throw new WrongInputException("Surname cannot contain digit or be empty!");
        }else if (InputValidator.checkIfEmpty(cottageOwnerData.getCity()) || InputValidator.containDigit(cottageOwnerData.getCity())) {
            throw new WrongInputException("City cannot contain digit or be empty!");
        }else if (InputValidator.checkIfEmpty(cottageOwnerData.getStreet())) {
            throw new WrongInputException("Street cannot be empty!");
        } else if (InputValidator.checkIfEmpty(cottageOwnerData.getZipcode()) || !InputValidator.containOnlyDigits(cottageOwnerData.getZipcode())) {
            throw new WrongInputException("Zip code contain only digits!");
        } else if (InputValidator.checkIfEmpty(cottageOwnerData.getPhoneNumber()) || !InputValidator.containDigit(cottageOwnerData.getPhoneNumber())) {
            throw new WrongInputException("Phone number contain only digits!");
        }

        co.setPassword(cottageOwnerData.getPassword());
        co.setName(cottageOwnerData.getName());
        co.setSurname(cottageOwnerData.getSurname());
        co.getAddress().setPlaceName(cottageOwnerData.getCity());
        co.getAddress().setZipCode(cottageOwnerData.getZipcode());
        co.getAddress().setStreet(cottageOwnerData.getStreet());
        co.setPhoneNumber(cottageOwnerData.getPhoneNumber());
        co.getProfilePicture().setPhotoPath(cottageOwnerData.getProfilePicture());

        cottageOwnerRepository.save(co);
        return true;
    }

}
