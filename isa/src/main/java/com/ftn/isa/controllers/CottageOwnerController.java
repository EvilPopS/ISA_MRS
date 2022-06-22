package com.ftn.isa.controllers;

import com.ftn.isa.DTO.*;
import com.ftn.isa.configs.ServerConfig;
import com.ftn.isa.helpers.Validate;
import com.ftn.isa.model.*;
import com.ftn.isa.security.auth.TokenUtils;
import com.ftn.isa.services.*;
import org.hibernate.dialect.lock.OptimisticEntityLockException;
import org.hibernate.dialect.lock.PessimisticEntityLockException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.orm.ObjectOptimisticLockingFailureException;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@RestController
@RequestMapping(value = "api/cottage-owner")
public class CottageOwnerController  {

    @Autowired
    private CottageOwnerService cottageOwnerService;

    @Autowired
    private PhotoService photoService;

    @Autowired
    private ClientService clientService;

    @Autowired
    private RequestService requestService;

    @Autowired
    private TokenUtils tokenUtils;

    @Autowired
    private ReservationService reservationService;

    @Autowired
    private SubscriptionService subscriptionService;

    @Autowired
    private EmailService emailService;

    @Autowired
    private LoyaltyProgramService loyaltyProgramService;

    @GetMapping
    @PreAuthorize("hasRole('COTTAGE_OWNER')")
    @CrossOrigin(origins = ServerConfig.FRONTEND_ORIGIN)
    public ResponseEntity<CottageOwnerDTO> getByEmail(HttpServletRequest request) {
        String email = tokenUtils.getEmailDirectlyFromHeader(request);
        if (email == null)
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);

        CottageOwner cottageOwner = cottageOwnerService.findByEmail(email);
        if (cottageOwner == null)
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        return new ResponseEntity<>(new CottageOwnerDTO(cottageOwner), HttpStatus.OK);
    }

    @PutMapping(consumes="application/json", value="/data-update")
    @PreAuthorize("hasRole('COTTAGE_OWNER')")
    @CrossOrigin(origins = ServerConfig.FRONTEND_ORIGIN)
    public ResponseEntity<CottageOwnerDTO> updatePersonalData(@RequestBody CottageOwnerDTO cottageOwnerData, HttpServletRequest request) {
        String email = tokenUtils.getEmailDirectlyFromHeader(request);
        if (email == null)
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);

        CottageOwner cottageOwner = cottageOwnerService.findByEmail(email);
        if (cottageOwner == null)
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);

        if (!cottageOwnerData.arePropsValid())
            return new ResponseEntity<>(HttpStatus.UNPROCESSABLE_ENTITY);

        cottageOwnerService.save(cottageOwnerData, cottageOwner);
        return new ResponseEntity<>(cottageOwnerData, HttpStatus.OK);

    }

    @GetMapping(value="/all-cottages")
    @PreAuthorize("hasRole('COTTAGE_OWNER')")
    @CrossOrigin(origins = ServerConfig.FRONTEND_ORIGIN)
    public ResponseEntity<Set<CottageDTO>> getAllCottages(HttpServletRequest request) {
        String email = tokenUtils.getEmailDirectlyFromHeader(request);
        if (email == null)
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);

        CottageOwner cottageOwner = cottageOwnerService.findByEmail(email);
        if (cottageOwner == null)
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);

        Set<Cottage> cottages = cottageOwner.getCottages();
        Set<CottageDTO> cottagesSet = new HashSet<>();
        for (Cottage c : cottages) {
            if (!c.isDeleted()) cottagesSet.add(new CottageDTO(c));
        }

        return new ResponseEntity<Set<CottageDTO>>(cottagesSet, HttpStatus.OK);
    }


    @GetMapping(value="/find-one-rental/{id}")
    @PreAuthorize("hasRole('COTTAGE_OWNER')")
    @CrossOrigin(origins = ServerConfig.FRONTEND_ORIGIN)
    public ResponseEntity<CottageDTO> getOneWithId(@PathVariable Long id, HttpServletRequest request) {
        String email = tokenUtils.getEmailDirectlyFromHeader(request);
        if (email == null)
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);

        CottageOwner cottageOwner = cottageOwnerService.findByEmail(email);
        if (cottageOwner == null)
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);

        Set<Cottage> cottages = cottageOwner.getCottages();
        CottageDTO returnCottage = null;
        for (Cottage c : cottages) {
            if (!c.isDeleted() && c.getId().equals(id)) returnCottage = new CottageDTO(c);
        }

        if (returnCottage == null)
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);

        return new ResponseEntity<CottageDTO>(returnCottage, HttpStatus.OK);
    }

    @PostMapping(value = "/add-cottage")
    @PreAuthorize("hasRole('COTTAGE_OWNER')")
    @CrossOrigin(origins = ServerConfig.FRONTEND_ORIGIN)
    public ResponseEntity<HttpStatus> addNewCottage(HttpServletRequest request, @RequestBody CottageDTO cottageDTO) {
        String email = tokenUtils.getEmailDirectlyFromHeader(request);
        if (email == null)
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);

        CottageOwner cottageOwner = cottageOwnerService.findByEmail(email);
        if (cottageOwner == null)
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);

        if (!cottageDTO.arePropsValidAdding())
            return new ResponseEntity<>(HttpStatus.UNPROCESSABLE_ENTITY);

        cottageOwnerService.addNewCottage(cottageDTO, cottageOwner);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @DeleteMapping(value="/delete-cottage/{id}")
    @PreAuthorize("hasRole('COTTAGE_OWNER')")
    @CrossOrigin(origins = ServerConfig.FRONTEND_ORIGIN)
    public ResponseEntity<HttpStatus> deleteCottage(HttpServletRequest request, @PathVariable String id) throws Exception {
        String email = tokenUtils.getEmailDirectlyFromHeader(request);
        if (email == null)
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);

        CottageOwner cottageOwner = cottageOwnerService.findByEmail(email);
        if (cottageOwner == null)
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);

        try {
            cottageOwnerService.deleteCottage(cottageOwner, Long.parseLong(id));
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        return new ResponseEntity<>(HttpStatus.OK);

    }

    @PreAuthorize("hasRole('COTTAGE_OWNER')")
    @CrossOrigin(origins = ServerConfig.FRONTEND_ORIGIN)
    @PutMapping(consumes="application/json", value="/change-cottage-data")
    public ResponseEntity<HttpStatus> updateCottageData(HttpServletRequest request, @RequestBody CottageDTO cottageDTO) {
        String email = tokenUtils.getEmailDirectlyFromHeader(request);
        if (email == null)
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);

        CottageOwner cottageOwner = cottageOwnerService.findByEmail(email);
        if (cottageOwner == null)
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);

        if (!cottageDTO.arePropsValidAdding())
            return new ResponseEntity<>(HttpStatus.UNPROCESSABLE_ENTITY);
        try {
            Set<Photo> photos = new HashSet<>();
            photos = photoService.changeCottagePhotos(cottageOwner, cottageDTO.getId(), cottageDTO.getPhotos());
            cottageOwnerService.save(cottageOwner, cottageDTO, photos);
        } catch (OptimisticEntityLockException e){
            return new ResponseEntity<>(HttpStatus.CONFLICT);
        }


        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping(value="/all-reservations")
    @PreAuthorize("hasRole('COTTAGE_OWNER')")
    @CrossOrigin(origins = ServerConfig.FRONTEND_ORIGIN)
    public ResponseEntity<Set<ReservationDTO>> getAllReservations(HttpServletRequest request) {
        String email = tokenUtils.getEmailDirectlyFromHeader(request);
        if (email == null)
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);

        CottageOwner cottageOwner = cottageOwnerService.findByEmail(email);
        if (cottageOwner == null)
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);

        Set<ReservationDTO> reservations = reservationService.createResDTO(cottageOwner, clientService.getAllClients());
        return new ResponseEntity<Set<ReservationDTO>>(reservations, HttpStatus.OK);
    }

    @PostMapping(consumes="application/json", value="/register")
    @CrossOrigin(origins = ServerConfig.FRONTEND_ORIGIN)
    public ResponseEntity<HttpStatus> registerUser(@RequestBody OwnerRegDTO ownerData) {
        if (!ownerData.arePropsValid())
            return new ResponseEntity<>(HttpStatus.UNPROCESSABLE_ENTITY);

        if (cottageOwnerService.findByEmail(ownerData.getEmail()) != null)
            return new ResponseEntity<>(HttpStatus.CONFLICT);

        CottageOwner cottageOwner = null;
        try {
            cottageOwner = new CottageOwner(ownerData);
            cottageOwnerService.save(cottageOwner);
        } catch (Exception ignored) {
            return new ResponseEntity<>(HttpStatus.UNPROCESSABLE_ENTITY);
        }

        Request regRequest = new Request(RequestType.ACCOUNT_REGISTRATION, cottageOwner, ownerData.getRegReason());
        requestService.save(regRequest);

        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping(value="/confirm-mail/{email}")
    @CrossOrigin(origins = ServerConfig.FRONTEND_ORIGIN)
    public ResponseEntity<HttpStatus> activateAccount(@PathVariable String email){
        CottageOwner cottageOwner = cottageOwnerService.findByEmail(email);
        if (cottageOwner == null)
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);

        cottageOwner.setActive(true);
        cottageOwnerService.save(cottageOwner);

        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping(value="/ownersSearch")
    @PreAuthorize("hasRole('COTTAGE_OWNER')")
    @CrossOrigin(origins = ServerConfig.FRONTEND_ORIGIN)
    public ResponseEntity<List<OwnersSearchResDTO>> search(HttpServletRequest request, @RequestParam String minPrice,
                                                           @RequestParam String maxPrice, @RequestParam String location,
                                                           @RequestParam String minCapacity, @RequestParam String serviceName) {
        String email = tokenUtils.getEmailDirectlyFromHeader(request);
        if (email == null)
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);

        CottageOwner cottageOwner = cottageOwnerService.findByEmail(email);
        if (cottageOwner == null)
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);

        if (!Validate.validatePrice(minPrice) || !Validate.validatePrice(maxPrice)
                || !Validate.validatePrice(minCapacity) || (!location.equals("") && !Validate.validateWords(location)))
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);

        List<OwnersSearchResDTO> rentals = new ArrayList<>();
        try {
            rentals = cottageOwnerService.search(cottageOwner, minPrice, maxPrice, location, minCapacity, serviceName);
        } catch (Exception ignored) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        return new ResponseEntity<>(rentals, HttpStatus.OK );
    }

    @PostMapping(value = "/add-action-reservation")
    @PreAuthorize("hasRole('COTTAGE_OWNER')")
    @CrossOrigin(origins = ServerConfig.FRONTEND_ORIGIN)
    public ResponseEntity<HttpStatus> addNewActionRes(HttpServletRequest request, @RequestBody ActionResDTO actionResDTO) {
        String email = tokenUtils.getEmailDirectlyFromHeader(request);
        if (email == null)
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);

        CottageOwner cottageOwner = cottageOwnerService.findByEmail(email);
        if (cottageOwner == null)
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);

        if (!actionResDTO.arePropsValidAdding())
            return new ResponseEntity<>(HttpStatus.UNPROCESSABLE_ENTITY);

        if (!cottageOwnerService.checkIfCottageExists(cottageOwner, actionResDTO.getRentalId()))
            return new ResponseEntity<>(HttpStatus.UNPROCESSABLE_ENTITY);

        Reservation newRes = null;
        try {
            newRes = reservationService.addNewActionRes(actionResDTO, "COTTAGE_OWNER");
        } catch(ObjectOptimisticLockingFailureException e) {
            return new ResponseEntity<>(HttpStatus.CONFLICT);
        }

        if (newRes == null)
            return new ResponseEntity<>(HttpStatus.UNPROCESSABLE_ENTITY);

        for (Cottage c : cottageOwner.getCottages()){
            if (c.getId().equals(actionResDTO.getRentalId())){
                newRes.setRental(c);
                break;
            }
        }
        //cottageOwnerService.save(cottageOwner);
        reservationService.save(newRes);
        notifySubscribers(cottageOwner, actionResDTO);

        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PreAuthorize("hasRole('COTTAGE_OWNER')")
    private void notifySubscribers(CottageOwner cottageOwner, ActionResDTO actionResDTO) {
        for (Subscription s : subscriptionService.getAllSubscriptions()){
            if (s.getOwner().getId().equals(cottageOwner.getId()) && s.isActiveSubscription()){
                try {
                    emailService.sendMail(s.getClient().getEmail(), "New Action on the radar",
                            "New action reservation is waiting for you! Check this great deal" +
                                    " for rental of owner " + cottageOwner.getName() + " " + cottageOwner.getSurname() +
                                    " now for just " + String.valueOf(actionResDTO.getPrice()) + " euros!"
                    );
                } catch(Exception ignored){
                    //nikom nista
                }
            }
        }
    }

    @PostMapping(value = "/add-regular-reservation")
    @PreAuthorize("hasRole('COTTAGE_OWNER')")
    @CrossOrigin(origins = ServerConfig.FRONTEND_ORIGIN)
    public ResponseEntity<HttpStatus> addRegularRes(HttpServletRequest request, @RequestBody RegularResDTO regularResDTO) {
        String email = tokenUtils.getEmailDirectlyFromHeader(request);
        if (email == null)
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);

        CottageOwner cottageOwner = cottageOwnerService.findByEmail(email);
        if (cottageOwner == null)
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        Client client = clientService.findByEmail(regularResDTO.getClientEmail());
        if (client == null || client.getNumOfPenalties() >= 3)
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);

        if (!regularResDTO.arePropsValidAdding())
            return new ResponseEntity<>(HttpStatus.UNPROCESSABLE_ENTITY);

        if (!cottageOwnerService.checkIfCottageExists(cottageOwner, regularResDTO.getRentalId()))
            return new ResponseEntity<>(HttpStatus.UNPROCESSABLE_ENTITY);


        if (!cottageOwnerService.checkIfCurrentResInProgress(client, cottageOwner, reservationService.getAllReservations()))
            return new ResponseEntity<>(HttpStatus.UNPROCESSABLE_ENTITY);

        Reservation newRes = null;
        try {
            newRes = reservationService.addNewRegularRes(regularResDTO, client, false, "COTTAGE_OWNER");
        } catch(ObjectOptimisticLockingFailureException e) {
            return new ResponseEntity<>(HttpStatus.CONFLICT);
        }

        if (newRes == null)
            return new ResponseEntity<>(HttpStatus.UNPROCESSABLE_ENTITY);

        for (Cottage c : cottageOwner.getCottages()){
            if (c.getId().equals(regularResDTO.getRentalId())){
                newRes.setRental(c);
                break;
            }
        }
        //cottageOwnerService.save(cottageOwner);
        reservationService.save(newRes);

        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PostMapping(value = "/add-unvailable-period")
    @PreAuthorize("hasRole('COTTAGE_OWNER')")
    @CrossOrigin(origins = ServerConfig.FRONTEND_ORIGIN)
    public ResponseEntity<HttpStatus> addUnvailablePeriod(HttpServletRequest request, @RequestBody RegularResDTO regularResDTO) {
        String email = tokenUtils.getEmailDirectlyFromHeader(request);
        if (email == null)
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);

        CottageOwner cottageOwner = cottageOwnerService.findByEmail(email);
        if (cottageOwner == null)
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);

        if (!regularResDTO.checkOnlyDate())
            return new ResponseEntity<>(HttpStatus.UNPROCESSABLE_ENTITY);

        if (!cottageOwnerService.checkIfCottageExists(cottageOwner, regularResDTO.getRentalId()))
            return new ResponseEntity<>(HttpStatus.UNPROCESSABLE_ENTITY);

        Reservation newRes = null;
        try {
            newRes = reservationService.addNewRegularRes(regularResDTO, null, true, "COTTAGE_OWNER");
        } catch(ObjectOptimisticLockingFailureException e) {
            return new ResponseEntity<>(HttpStatus.CONFLICT);
        }

        if (newRes == null)
            return new ResponseEntity<>(HttpStatus.UNPROCESSABLE_ENTITY);

        for (Cottage c : cottageOwner.getCottages()){
            if (c.getId().equals(regularResDTO.getRentalId())){
                newRes.setRental(c);
                break;
            }
        }
        //cottageOwnerService.save(cottageOwner);
        reservationService.save(newRes);

        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping(value = "/get-chart-data/{selectedGraph}/{selectedPeriod}/{selectedMonth}")
    @PreAuthorize("hasRole('COTTAGE_OWNER')")
    @CrossOrigin(origins = ServerConfig.FRONTEND_ORIGIN)
    public ResponseEntity<List<List<String>>> getChartData(@PathVariable String selectedGraph, @PathVariable String selectedPeriod, @PathVariable String selectedMonth, HttpServletRequest request) {
        String email = tokenUtils.getEmailDirectlyFromHeader(request);
        if (email == null)
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);

        CottageOwner cottageOwner = cottageOwnerService.findByEmail(email);
        if (cottageOwner == null)
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);

        List<List<String>> data = cottageOwnerService.getChartData(cottageOwner, selectedGraph, selectedPeriod, selectedMonth, loyaltyProgramService.getAllLoyaltyPrograms());

        return new ResponseEntity<>(data, HttpStatus.OK);
    }

    @PutMapping(value="/buy-loyalty-program/{program}")
    @PreAuthorize("hasRole('COTTAGE_OWNER')")
    @CrossOrigin(origins = ServerConfig.FRONTEND_ORIGIN)
    public ResponseEntity<HttpStatus> buyLoyaltyProgram(@PathVariable String program, HttpServletRequest request) {
        String email = tokenUtils.getEmailDirectlyFromHeader(request);
        if (email == null)
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);

        if (
                !cottageOwnerService.updateLoyaltyProgram(cottageOwnerService.findByEmail(email), loyaltyProgramService.getAllLoyaltyPrograms(), program)
        )
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);

        return new ResponseEntity<>(HttpStatus.OK);
    }

}
