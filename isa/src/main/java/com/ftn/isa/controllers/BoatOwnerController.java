package com.ftn.isa.controllers;

import com.ftn.isa.DTO.*;
import com.ftn.isa.configs.ServerConfig;
import com.ftn.isa.helpers.Validate;
import com.ftn.isa.model.*;
import com.ftn.isa.security.auth.TokenUtils;
import com.ftn.isa.services.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.PessimisticLockingFailureException;
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

    @Autowired
    private LoyaltyProgramService loyaltyProgramService;

    @Autowired
    private SubscriptionService subscriptionService;

    @Autowired
    private EmailService emailService;

    @Autowired
    private RequestService requestService;

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

    @GetMapping(value = "/get-chart-data/{selectedGraph}/{selectedPeriod}/{selectedMonth}")
    @PreAuthorize("hasRole('BOAT_OWNER')")
    @CrossOrigin(origins = ServerConfig.FRONTEND_ORIGIN)
    public ResponseEntity<List<List<String>>> getChartData(@PathVariable String selectedGraph, @PathVariable String selectedPeriod, @PathVariable String selectedMonth, HttpServletRequest request) {
        String email = tokenUtils.getEmailDirectlyFromHeader(request);
        if (email == null)
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);

        BoatOwner boatOwner = boatOwnerService.findByEmail(email);
        if (boatOwner == null)
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);

        List<List<String>> data = boatOwnerService.getChartData(boatOwner, selectedGraph, selectedPeriod, selectedMonth, loyaltyProgramService.getAllLoyaltyPrograms());

        return new ResponseEntity<>(data, HttpStatus.OK);
    }

    @PostMapping(value = "/add-action-reservation")
    @PreAuthorize("hasRole('BOAT_OWNER')")
    @CrossOrigin(origins = ServerConfig.FRONTEND_ORIGIN)
    public ResponseEntity<HttpStatus> addNewActionRes(HttpServletRequest request, @RequestBody ActionResDTO actionResDTO) {
        String email = tokenUtils.getEmailDirectlyFromHeader(request);
        if (email == null)
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);

        BoatOwner boatOwner = boatOwnerService.findByEmail(email);
        if (boatOwner == null)
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);

        if (!actionResDTO.arePropsValidAdding())
            return new ResponseEntity<>(HttpStatus.UNPROCESSABLE_ENTITY);

        if (!boatOwnerService.checkIfBoatExists(boatOwner, actionResDTO.getRentalId()))
            return new ResponseEntity<>(HttpStatus.UNPROCESSABLE_ENTITY);

        Reservation newRes = null;
        try {
            newRes = reservationService.addNewActionRes(actionResDTO);
        } catch (PessimisticLockingFailureException e) {
            return new ResponseEntity<>(HttpStatus.CONFLICT);
        }

        if (newRes == null)
            return new ResponseEntity<>(HttpStatus.UNPROCESSABLE_ENTITY);

        for (Boat c : boatOwner.getBoats()){
            if (c.getId().equals(actionResDTO.getRentalId())){
                c.getReservations().add(newRes);
                break;
            }
        }
        boatOwnerService.save(boatOwner);
        notifySubscribers(boatOwner, actionResDTO);

        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PreAuthorize("hasRole('BOAT_OWNER')")
    private void notifySubscribers(BoatOwner boatOwner, ActionResDTO actionResDTO) {
        for (Subscription s : subscriptionService.getAllSubscriptions()){
            if (s.getOwner().getId().equals(boatOwner.getId()) && s.isActiveSubscription()){
                try {
                    emailService.sendMail(s.getClient().getEmail(), "New Action on the radar",
                            "New action reservation is waiting for you! Check this great deal" +
                                    " for rental of owner " + boatOwner.getName() + " " + boatOwner.getSurname() +
                                    " now for just " + String.valueOf(actionResDTO.getPrice()) + " euros!"
                    );
                } catch(Exception ignored){
                    //nikom nista
                }
            }
        }
    }

    @PostMapping(value = "/add-regular-reservation")
    @PreAuthorize("hasRole('BOAT_OWNER')")
    @CrossOrigin(origins = ServerConfig.FRONTEND_ORIGIN)
    public ResponseEntity<HttpStatus> addRegularRes(HttpServletRequest request, @RequestBody RegularResDTO regularResDTO) {
        String email = tokenUtils.getEmailDirectlyFromHeader(request);
        if (email == null)
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);

        BoatOwner boatOwner = boatOwnerService.findByEmail(email);
        if (boatOwner == null)
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        Client client = clientService.findByEmail(regularResDTO.getClientEmail());
        if (client == null || client.getNumOfPenalties() >= 3)
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);

        if (!regularResDTO.arePropsValidAdding())
            return new ResponseEntity<>(HttpStatus.UNPROCESSABLE_ENTITY);

        if (!boatOwnerService.checkIfBoatExists(boatOwner, regularResDTO.getRentalId()))
            return new ResponseEntity<>(HttpStatus.UNPROCESSABLE_ENTITY);

        if (!boatOwnerService.checkIfCurrentResInProgress(client, boatOwner, reservationService.getAllReservations()))
            return new ResponseEntity<>(HttpStatus.UNPROCESSABLE_ENTITY);

        Reservation newRes = null;
        try {
            newRes = reservationService.addNewRegularRes(regularResDTO, client, false);
        } catch (PessimisticLockingFailureException e){
            return new ResponseEntity<>(HttpStatus.CONFLICT);
        }

        if (newRes == null)
            return new ResponseEntity<>(HttpStatus.UNPROCESSABLE_ENTITY);

        for (Boat c : boatOwner.getBoats()){
            if (c.getId().equals(regularResDTO.getRentalId())){
                c.getReservations().add(newRes);
                break;
            }
        }
        boatOwnerService.save(boatOwner);

        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PostMapping(value = "/add-unvailable-period")
    @PreAuthorize("hasRole('BOAT_OWNER')")
    @CrossOrigin(origins = ServerConfig.FRONTEND_ORIGIN)
    public ResponseEntity<HttpStatus> addUnvailablePeriod(HttpServletRequest request, @RequestBody RegularResDTO regularResDTO) {
        String email = tokenUtils.getEmailDirectlyFromHeader(request);
        if (email == null)
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);

        BoatOwner boatOwner = boatOwnerService.findByEmail(email);
        if (boatOwner == null)
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);

        if (!regularResDTO.checkOnlyDate())
            return new ResponseEntity<>(HttpStatus.UNPROCESSABLE_ENTITY);

        if (!boatOwnerService.checkIfBoatExists(boatOwner, regularResDTO.getRentalId()))
            return new ResponseEntity<>(HttpStatus.UNPROCESSABLE_ENTITY);

        Reservation newRes = null;
        try {
            newRes = reservationService.addNewRegularRes(regularResDTO, null, true);
        } catch (PessimisticLockingFailureException e) {
            return new ResponseEntity<>(HttpStatus.CONFLICT);
        }
        if (newRes == null)
            return new ResponseEntity<>(HttpStatus.UNPROCESSABLE_ENTITY);

        for (Boat c : boatOwner.getBoats()){
            if (c.getId().equals(regularResDTO.getRentalId())){
                c.getReservations().add(newRes);
                break;
            }
        }
        boatOwnerService.save(boatOwner);

        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PostMapping(consumes="application/json", value="/register")
    @CrossOrigin(origins = ServerConfig.FRONTEND_ORIGIN)
    public ResponseEntity<HttpStatus> registerUser(@RequestBody OwnerRegDTO ownerData) {
        if (!ownerData.arePropsValid())
            return new ResponseEntity<>(HttpStatus.UNPROCESSABLE_ENTITY);

        if (boatOwnerService.findByEmail(ownerData.getEmail()) != null)
            return new ResponseEntity<>(HttpStatus.CONFLICT);

        BoatOwner boatOwner = null;
        try {
            boatOwner = new BoatOwner(ownerData);
            boatOwnerService.save(boatOwner);
        } catch (Exception ignored) {
            return new ResponseEntity<>(HttpStatus.UNPROCESSABLE_ENTITY);
        }

        Request regRequest = new Request(RequestType.ACCOUNT_REGISTRATION, boatOwner, ownerData.getRegReason());
        requestService.save(regRequest);

        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping(value="/confirm-mail/{email}")
    @CrossOrigin(origins = ServerConfig.FRONTEND_ORIGIN)
    public ResponseEntity<HttpStatus> activateAccount(@PathVariable String email){
        BoatOwner boatOwner = boatOwnerService.findByEmail(email);
        if (boatOwner == null)
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);

        boatOwner.setActive(true);
        boatOwnerService.save(boatOwner);

        return new ResponseEntity<>(HttpStatus.OK);
    }


}
