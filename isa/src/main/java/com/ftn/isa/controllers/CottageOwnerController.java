package com.ftn.isa.controllers;

import com.ftn.isa.DTO.CottageDTO;
import com.ftn.isa.DTO.CottageOwnerDTO;
import com.ftn.isa.model.*;
import com.ftn.isa.services.AddressService;
import com.ftn.isa.services.CottageOwnerService;
import com.ftn.isa.helpers.WrongInputException;
import com.ftn.isa.services.CottageService;
import com.ftn.isa.services.PhotoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@RestController
@RequestMapping(value = "api/cottage-owner")
public class CottageOwnerController  {

    @Autowired
    private CottageOwnerService cottageOwnerService;

    @Autowired
    private CottageService cottageService;

    @Autowired
    private PhotoService photoService;

    @Autowired
    private AddressService addressService;

    @CrossOrigin(origins = "http://localhost:8081")
    @GetMapping(value="/{email}")
    public ResponseEntity<CottageOwnerDTO> getByEmail(@PathVariable String email) {
        CottageOwner cottageOwner = cottageOwnerService.findByEmail(email);
        if (cottageOwner == null)
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        return new ResponseEntity<>(new CottageOwnerDTO(cottageOwner), HttpStatus.OK);
    }

    @CrossOrigin(origins = "http://localhost:8081")
    @PutMapping(consumes="application/json", value="/data-update")
    public ResponseEntity<CottageOwnerDTO> updatePersonalData(@RequestBody CottageOwnerDTO cottageOwnerData) {
        CottageOwner cottageOwner = cottageOwnerService.findByEmail(cottageOwnerData.getEmail());
        if (cottageOwner == null)
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);

        if (cottageOwnerData.arePropsValid())
            return new ResponseEntity<>(HttpStatus.UNPROCESSABLE_ENTITY);

        cottageOwnerService.save(cottageOwnerData, cottageOwner);
        return new ResponseEntity<>(cottageOwnerData, HttpStatus.OK);

    }

    @CrossOrigin(origins = "http://localhost:8081")
    @GetMapping(value="/all-cottages/{email}")
    public ResponseEntity<Set<CottageDTO>> getAllCottages(@PathVariable String email) {
        CottageOwner cottageOwner = cottageOwnerService.findByEmail(email);
        if (cottageOwner == null)
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);

        Set<Cottage> cottages = cottageOwner.getCottages();
        Set<CottageDTO> cottagesSet = new HashSet<>();
        for (Cottage c : cottages){ cottagesSet.add(new CottageDTO(c));}

        return new ResponseEntity<Set<CottageDTO>>(cottagesSet, HttpStatus.OK);
    }

    @CrossOrigin(origins = "http://localhost:8081")
    @PostMapping(value = "/add-cottage/{email}")
    public ResponseEntity<HttpStatus> addNewCottage(@PathVariable String email, @RequestBody CottageDTO cottageDTO) {
        CottageOwner cottageOwner = cottageOwnerService.findByEmail(email);
        if (cottageOwner == null)
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);

        if (cottageDTO.arePropsValidAdding())
            return new ResponseEntity<>(HttpStatus.UNPROCESSABLE_ENTITY);

        Address address = addressService.save(cottageDTO.getCity(), cottageDTO.getZipCode(), cottageDTO.getStreet());
        Cottage cottage = new Cottage(cottageDTO.getName(), cottageDTO.getDescription(),
                cottageDTO.getCapacity(), cottageDTO.getRules(),
                false, address, cottageDTO.getAverageRating(), cottageDTO.getNoRatings(),
                RentalType.COTTAGE, cottageDTO.getPrice(), cottageDTO.getAdditionalServices(), cottageDTO.getNoRooms());

        Set<Photo> newPhotos = photoService.save(cottageDTO.getPhotos());
        cottage.setPhotos(newPhotos);
        cottage.setId(10l);
        Cottage newCottage = cottageService.save(cottage);
        cottageOwner.getCottages().add(newCottage);
        cottageOwnerService.save(cottageOwner);

        return new ResponseEntity<HttpStatus>(HttpStatus.OK);
    }

}
