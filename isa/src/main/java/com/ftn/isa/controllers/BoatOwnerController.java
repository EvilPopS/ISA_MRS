package com.ftn.isa.controllers;

import com.ftn.isa.DTO.*;
import com.ftn.isa.configs.ServerConfig;
import com.ftn.isa.helpers.Validate;
import com.ftn.isa.model.*;
import com.ftn.isa.security.auth.TokenUtils;
import com.ftn.isa.services.BoatOwnerService;
import com.ftn.isa.services.ClientService;
import com.ftn.isa.services.PhotoService;
import com.ftn.isa.services.ReservationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@RestController
@RequestMapping(value = "api/boat-owner")
public class BoatOwnerController {

    @Autowired
    private BoatOwnerService boatOwnerService;

    @Autowired
    private TokenUtils tokenUtils;

    @Autowired
    private PhotoService photoService;

    @Autowired
    private ReservationService reservationService;

    @Autowired
    private ClientService clientService;

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

    @DeleteMapping(value="/delete-boat/{id}")
    @PreAuthorize("hasRole('BOAT_OWNER')")
    @CrossOrigin(origins = ServerConfig.FRONTEND_ORIGIN)
    public ResponseEntity<HttpStatus> deleteBoat(HttpServletRequest request, @PathVariable String id) {
        String email = tokenUtils.getEmailDirectlyFromHeader(request);
        if (email == null)
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);

        BoatOwner boatOwner = boatOwnerService.findByEmail(email);
        if (boatOwner == null)
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);

        try {
            boatOwnerService.deleteBoat(boatOwner, Long.parseLong(id));
        } catch(Exception e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        return new ResponseEntity<>(HttpStatus.OK);

    }

    @PreAuthorize("hasRole('BOAT_OWNER')")
    @CrossOrigin(origins = ServerConfig.FRONTEND_ORIGIN)
    @PutMapping(consumes="application/json", value="/change-boat-data")
    public ResponseEntity<HttpStatus> updateBoatData(HttpServletRequest request, @RequestBody BoatDTO boatDTO) {
        String email = tokenUtils.getEmailDirectlyFromHeader(request);
        if (email == null)
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);

        BoatOwner boatOwner = boatOwnerService.findByEmail(email);
        if (boatOwner == null)
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);

        if (!boatDTO.arePropsValidAdding())
            return new ResponseEntity<>(HttpStatus.UNPROCESSABLE_ENTITY);

        Set<Photo> photos = new HashSet<>();
        photos = photoService.changeBoatPhotos(boatOwner, boatDTO.getId(), boatDTO.getPhotos());
        boatOwnerService.save(boatOwner, boatDTO, photos);

        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PostMapping(value = "/add-boat")
    @PreAuthorize("hasRole('BOAT_OWNER')")
    @CrossOrigin(origins = ServerConfig.FRONTEND_ORIGIN)
    public ResponseEntity<HttpStatus> addNewBoat(HttpServletRequest request, @RequestBody BoatDTO boatDTO) {
        String email = tokenUtils.getEmailDirectlyFromHeader(request);
        if (email == null)
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);

        BoatOwner boatOwner = boatOwnerService.findByEmail(email);
        if (boatOwner == null)
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);

        if (!boatDTO.arePropsValidAdding())
            return new ResponseEntity<>(HttpStatus.UNPROCESSABLE_ENTITY);

        boatOwnerService.addNewBoat(boatDTO, boatOwner);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping(value="/all-reservations")
    @PreAuthorize("hasRole('BOAT_OWNER')")
    @CrossOrigin(origins = ServerConfig.FRONTEND_ORIGIN)
    public ResponseEntity<Set<ReservationDTO>> getAllReservations(HttpServletRequest request) {
        String email = tokenUtils.getEmailDirectlyFromHeader(request);
        if (email == null)
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);

        BoatOwner boatOwner = boatOwnerService.findByEmail(email);
        if (boatOwner == null)
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);

        Set<ReservationDTO> reservations = reservationService.createResDTO(boatOwner, clientService.getAllClients());
        return new ResponseEntity<Set<ReservationDTO>>(reservations, HttpStatus.OK);
    }

    @GetMapping(value="/find-one-rental/{id}")
    @PreAuthorize("hasRole('BOAT_OWNER')")
    @CrossOrigin(origins = ServerConfig.FRONTEND_ORIGIN)
    public ResponseEntity<BoatDTO> getOneWithId(@PathVariable Long id, HttpServletRequest request) {
        String email = tokenUtils.getEmailDirectlyFromHeader(request);
        if (email == null)
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);

        BoatOwner boatOwner = boatOwnerService.findByEmail(email);
        if (boatOwner == null)
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);

        Set<Boat> boats = boatOwner.getBoats();
        BoatDTO returnBoat = null;
        for (Boat c : boats) {
            if (!c.isDeleted() && c.getId().equals(id)) returnBoat = new BoatDTO(c);
        }

        if (returnBoat == null)
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);

        return new ResponseEntity<BoatDTO>(returnBoat, HttpStatus.OK);
    }

    @GetMapping(value="/ownersSearch")
    @PreAuthorize("hasRole('BOAT_OWNER')")
    @CrossOrigin(origins = ServerConfig.FRONTEND_ORIGIN)
    public ResponseEntity<List<OwnersSearchResDTO>> search(HttpServletRequest request, @RequestParam String minPrice,
                                                           @RequestParam String maxPrice, @RequestParam String location,
                                                           @RequestParam String minCapacity, @RequestParam String serviceName) {
        String email = tokenUtils.getEmailDirectlyFromHeader(request);
        if (email == null)
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);

        BoatOwner boatOwner = boatOwnerService.findByEmail(email);
        if (boatOwner == null)
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);

        if (!Validate.validatePrice(minPrice) || !Validate.validatePrice(maxPrice)
                || !Validate.validatePrice(minCapacity) || (!location.equals("") && !Validate.validateWords(location)))
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);

        List<OwnersSearchResDTO> rentals = new ArrayList<>();
        try {
            rentals = boatOwnerService.search(boatOwner, minPrice, maxPrice, location, minCapacity, serviceName);
        } catch (Exception ignored) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        return new ResponseEntity<>(rentals, HttpStatus.OK );
    }

}
