package com.ftn.isa.controllers;

import com.ftn.isa.DTO.BoatDTO;
import com.ftn.isa.DTO.BoatOwnerDTO;
import com.ftn.isa.DTO.CottageDTO;
import com.ftn.isa.DTO.CottageOwnerDTO;
import com.ftn.isa.configs.ServerConfig;
import com.ftn.isa.model.Boat;
import com.ftn.isa.model.BoatOwner;
import com.ftn.isa.model.Cottage;
import com.ftn.isa.model.CottageOwner;
import com.ftn.isa.security.auth.TokenUtils;
import com.ftn.isa.services.BoatOwnerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.HashSet;
import java.util.Set;

@RestController
@RequestMapping(value = "api/boat-owner")
public class BoatOwnerController {

    @Autowired
    private BoatOwnerService boatOwnerService;

    @Autowired
    private TokenUtils tokenUtils;

    @GetMapping
    @PreAuthorize("hasRole('BOAT_OWNER')")
    @CrossOrigin(origins = ServerConfig.FRONTEND_ORIGIN)
    public ResponseEntity<BoatOwnerDTO> getByEmail(HttpServletRequest request) {
        String email = tokenUtils.getEmailDirectlyFromHeader(request);
        if (email == null)
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);

        BoatOwner boatOwner = boatOwnerService.findByEmail(email);
        if (boatOwner == null)
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        return new ResponseEntity<>(new BoatOwnerDTO(boatOwner), HttpStatus.OK);
    }

    @PutMapping(consumes="application/json", value="/data-update")
    @PreAuthorize("hasRole('BOAT_OWNER')")
    @CrossOrigin(origins = ServerConfig.FRONTEND_ORIGIN)
    public ResponseEntity<BoatOwnerDTO> updatePersonalData(@RequestBody BoatOwnerDTO boatOwnerDTO, HttpServletRequest request) {
        String email = tokenUtils.getEmailDirectlyFromHeader(request);
        if (email == null)
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);

        BoatOwner boatOwner = boatOwnerService.findByEmail(email);
        if (boatOwner == null)
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);

        if (!boatOwnerDTO.arePropsValid())
            return new ResponseEntity<>(HttpStatus.UNPROCESSABLE_ENTITY);

        boatOwnerService.save(boatOwnerDTO, boatOwner);
        return new ResponseEntity<>(boatOwnerDTO, HttpStatus.OK);

    }

    @GetMapping(value="/all-boats")
    @PreAuthorize("hasRole('BOAT_OWNER')")
    @CrossOrigin(origins = ServerConfig.FRONTEND_ORIGIN)
    public ResponseEntity<Set<BoatDTO>> getAllBoats(HttpServletRequest request) {
        String email = tokenUtils.getEmailDirectlyFromHeader(request);
        if (email == null)
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);

        BoatOwner boatOwner = boatOwnerService.findByEmail(email);
        if (boatOwner == null)
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);

        Set<Boat> boats = boatOwner.getBoats();
        Set<BoatDTO> boatDTOSSet = new HashSet<>();
        for (Boat b : boats) {
            if (!b.isDeleted()) boatDTOSSet.add(new BoatDTO(b));
        }

        return new ResponseEntity<Set<BoatDTO>>(boatDTOSSet, HttpStatus.OK);
    }

}
