package com.ftn.isa.controllers;

import com.ftn.isa.DTO.CottageOwnerDTO;
import com.ftn.isa.model.CottageOwner;
import com.ftn.isa.services.CottageOwnerService;
import com.ftn.isa.helpers.WrongInputException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "api/cottage-owner")
public class CottageOwnerController  {

    @Autowired
    private CottageOwnerService cottageOwnerService;

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

}
