package com.ftn.isa.controllers;

import com.ftn.isa.DTO.*;
import com.ftn.isa.configs.ServerConfig;
import com.ftn.isa.helpers.Validate;
import com.ftn.isa.model.*;
import com.ftn.isa.security.auth.TokenUtils;
import com.ftn.isa.services.*;
import org.hibernate.dialect.lock.PessimisticEntityLockException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.orm.ObjectOptimisticLockingFailureException;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static java.time.temporal.ChronoUnit.DAYS;


@RestController
@RequestMapping(value="api/fishingInstructor")
@CrossOrigin(origins = "http://localhost:8081")
public class FishingInstructorController {
    @Autowired
    private FishingInstructorService fishingInstructorService;

    @Autowired
    private AdventureService adventureService;

    @Autowired
    private RequestService requestService;

    @Autowired
    private TokenUtils tokenUtils;

    @Autowired
    private ReservationService reservationService;

    @Autowired
    private ClientService clientService;

    @Autowired
    private SubscriptionService subscriptionService;

    @Autowired
    private EmailService emailService;

    @Autowired
    private LoyaltyProgramService loyaltyProgramService;


    @GetMapping(value = "/{email}/searchAdventure")
    @PreAuthorize("hasRole('INSTRUCTOR')")
    public ResponseEntity<List<AdventureDTO>> searchAdventureByName(@RequestParam String adventureName, @PathVariable String email){
        FishingInstructor fishingInstructor = fishingInstructorService.findByEmail(email);
        List<Adventure> adventures = adventureService.searchAdventureByName(adventureName, fishingInstructor.getId());
        List<AdventureDTO> adventureDTOs = new ArrayList<>();
        for (Adventure a : adventures){adventureDTOs.add(new AdventureDTO(a));}

        return  new ResponseEntity<List<AdventureDTO>>(adventureDTOs, HttpStatus.OK);


    }


    @GetMapping
    @PreAuthorize("hasRole('INSTRUCTOR')")
    public ResponseEntity<FishingInstructorDTO> getByEmail(HttpServletRequest request) {
        String email = tokenUtils.getEmailDirectlyFromHeader(request);
        if (email == null)
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);

        FishingInstructor fishingInstructor = fishingInstructorService.findByEmail(email);
        if (fishingInstructor == null)
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        return new ResponseEntity<>(new FishingInstructorDTO(fishingInstructor), HttpStatus.OK);
    }

    @GetMapping(value="/adventures")
    @PreAuthorize("hasRole('INSTRUCTOR')")
    @CrossOrigin(origins = ServerConfig.FRONTEND_ORIGIN)
    public ResponseEntity<Set<AdventureDTO>> getAllAdventures(HttpServletRequest request) {
        String email = tokenUtils.getEmailDirectlyFromHeader(request);
        if (email == null)
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);

        FishingInstructor fishingInstructor = fishingInstructorService.findByEmail(email);
        if (fishingInstructor == null)
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);

        Set<Adventure> adventures = fishingInstructor.getAdventures();
        Set<AdventureDTO> adventureDTOS = new HashSet<>();
        for (Adventure a : adventures) {
            if (!a.isDeleted()) adventureDTOS.add(new AdventureDTO(a));
        }

        return new ResponseEntity<Set<AdventureDTO>>(adventureDTOS, HttpStatus.OK);
    }

    @PostMapping(value="/adventures/add-adventure")
    @PreAuthorize("hasRole('INSTRUCTOR')")
    public ResponseEntity<HttpStatus> addAdventure(HttpServletRequest request, @RequestBody AdventureDTO adventureDTO) {
        String email = tokenUtils.getEmailDirectlyFromHeader(request);
        if (email == null)
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);

        FishingInstructor fishingInstructor = fishingInstructorService.findByEmail(email);
        if (fishingInstructor == null){
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        if (!adventureDTO.propsValid()){
            return new ResponseEntity<>(HttpStatus.UNPROCESSABLE_ENTITY);
        }


        Address adventureAddress = new Address(adventureDTO.getCountry(), adventureDTO.getCity(), adventureDTO.getStreet());
        Set<Photo> photos = new HashSet<>();
        for (String p : adventureDTO.getPhotos())
            photos.add(new Photo(p));

        Adventure adventure = new Adventure(adventureDTO.getName(), adventureDTO.getDescription(), photos,
                 adventureDTO.getCapacity(), adventureDTO.getRules(), false, adventureAddress, adventureDTO.getRating(),
                adventureDTO.getNoRatings(), RentalType.ADVENTURE, adventureDTO.getPrice(), adventureDTO.getBiography(),
                adventureDTO.getFishingEquipment(), adventureDTO.getCancellationConditions());

        adventure.setPhotos(photos);

        fishingInstructor.getAdventures().add(adventure);
        fishingInstructorService.save(fishingInstructor);

        return new ResponseEntity<>(HttpStatus.OK);
    }


    @DeleteMapping(value="/{email}/delete-adventure/{id}")
    @CrossOrigin(origins = ServerConfig.FRONTEND_ORIGIN)
    public ResponseEntity<HttpStatus> deleteAdventure(HttpServletRequest request, @PathVariable String id) throws Exception {
        String email = tokenUtils.getEmailDirectlyFromHeader(request);
        if (email == null)
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);

        FishingInstructor fishingInstructor = fishingInstructorService.findByEmail(email);
        if (fishingInstructor == null)
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);

        fishingInstructorService.deleteAdventure(fishingInstructor, Long.parseLong(id));
        return new ResponseEntity<>(HttpStatus.OK);

    }

    @PutMapping(consumes="application/json", value="/data-update")
    @PreAuthorize("hasRole('INSTRUCTOR')")
    public ResponseEntity<FishingInstructorDTO> updatePersonalData(@RequestBody FishingInstructorDTO fishingInstructorData) {
        FishingInstructor fishingInstructor = fishingInstructorService.findByEmail(fishingInstructorData.getEmail());

        if (fishingInstructor == null)
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);

        if (!fishingInstructorData.arePropsValid())
            return new ResponseEntity<>(HttpStatus.UNPROCESSABLE_ENTITY);

        fishingInstructorService.updatePersonalInfo(fishingInstructorData, fishingInstructor);
        return new ResponseEntity<>(fishingInstructorData, HttpStatus.OK);
    }

    @PostMapping(consumes="application/json", value="/register")
    @CrossOrigin(origins = ServerConfig.FRONTEND_ORIGIN)
    @PreAuthorize("hasRole('INSTRUCTOR')")
    public ResponseEntity<HttpStatus> registerUser(@RequestBody OwnerRegDTO ownerData) {
        if (!ownerData.arePropsValid())
            return new ResponseEntity<>(HttpStatus.UNPROCESSABLE_ENTITY);

        if (fishingInstructorService.findByEmail(ownerData.getEmail()) != null)
            return new ResponseEntity<>(HttpStatus.CONFLICT);

        FishingInstructor fishingInstructor = null;
        try {
            fishingInstructor = new FishingInstructor(ownerData);
            fishingInstructorService.save(fishingInstructor);
        } catch (Exception ignored) {
            return new ResponseEntity<>(HttpStatus.UNPROCESSABLE_ENTITY);
        }

        Request regRequest = new Request(RequestType.ACCOUNT_REGISTRATION, fishingInstructor, ownerData.getRegReason());
        requestService.save(regRequest);

        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping(value="/confirm-mail/{email}")
    @PreAuthorize("hasRole('INSTRUCTOR')")
    public ResponseEntity<HttpStatus> activateAccount(@PathVariable String email){
        FishingInstructor fishingInstructor = fishingInstructorService.findByEmail(email);
        if (fishingInstructor == null)
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);

        fishingInstructor.setActive(true);
        fishingInstructorService.save(fishingInstructor);

        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping(value="/all-reservations")
    @CrossOrigin(origins = ServerConfig.FRONTEND_ORIGIN)
    @PreAuthorize("hasRole('INSTRUCTOR')")
    public ResponseEntity<Set<ReservationDTO>> getAllReservations(HttpServletRequest request) {
        String email = tokenUtils.getEmailDirectlyFromHeader(request);
        if (email == null)
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);

        FishingInstructor fishingInstructor = fishingInstructorService.findByEmail(email);
        if (fishingInstructor == null)
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);

        Set<ReservationDTO> reservations = reservationService.createResDTO(fishingInstructor, clientService.getAllClients());
        return new ResponseEntity<Set<ReservationDTO>>(reservations, HttpStatus.OK);
    }


    @PostMapping(value = "/add-regular-reservation")
    @PreAuthorize("hasRole('INSTRUCTOR')")
    @CrossOrigin(origins = ServerConfig.FRONTEND_ORIGIN)
    public ResponseEntity<HttpStatus> addRegularRes(HttpServletRequest request, @RequestBody RegularResDTO regularResDTO) {
        String email = tokenUtils.getEmailDirectlyFromHeader(request);
        if (email == null)
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);

        FishingInstructor fishingInstructor = fishingInstructorService.findByEmail(email);
        if (fishingInstructor == null)
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        Client client = clientService.findByEmail(regularResDTO.getClientEmail());
        if (client == null || client.getNumOfPenalties() >= 3)
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);

        if (!regularResDTO.arePropsValidAdding())
            return new ResponseEntity<>(HttpStatus.UNPROCESSABLE_ENTITY);

        if (!fishingInstructorService.checkIfAdventureExists(fishingInstructor, regularResDTO.getRentalId()))
            return new ResponseEntity<>(HttpStatus.UNPROCESSABLE_ENTITY);


        if (!fishingInstructorService.checkIfCurrentResInProgress(client, fishingInstructor, reservationService.getAllReservations()))
            return new ResponseEntity<>(HttpStatus.UNPROCESSABLE_ENTITY);
        Reservation newRes = null;
        try {
            newRes = reservationService.addNewRegularRes(regularResDTO, client, false, "INSTRUCTOR");
        } catch(ObjectOptimisticLockingFailureException e) {
            return new ResponseEntity<>(HttpStatus.CONFLICT);
        }

        if (newRes == null)
            return new ResponseEntity<>(HttpStatus.UNPROCESSABLE_ENTITY);

        for (Adventure a : fishingInstructor.getAdventures()){
            if (a.getId().equals(regularResDTO.getRentalId())){
                newRes.setRental(a);
                break;
            }
        }
        //fishingInstructorService.save(fishingInstructor);
        reservationService.save(newRes);

        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PostMapping(value = "/add-unvailable-period")
    @PreAuthorize("hasRole('INSTRUCTOR')")
    @CrossOrigin(origins = ServerConfig.FRONTEND_ORIGIN)
    public ResponseEntity<HttpStatus> addUnvailablePeriod(HttpServletRequest request, @RequestBody RegularResDTO regularResDTO) {
        String email = tokenUtils.getEmailDirectlyFromHeader(request);
        if (email == null)
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);

        FishingInstructor fishingInstructor = fishingInstructorService.findByEmail(email);
        if (fishingInstructor == null)
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);

        if (!regularResDTO.checkOnlyDate())
            return new ResponseEntity<>(HttpStatus.UNPROCESSABLE_ENTITY);

        if (!fishingInstructorService.checkIfAdventureExists(fishingInstructor, regularResDTO.getRentalId()))
            return new ResponseEntity<>(HttpStatus.UNPROCESSABLE_ENTITY);

        Reservation newRes = null;
        try {
            newRes = reservationService.addNewRegularRes(regularResDTO, null, true, "INSTRUCTOR");
        } catch(ObjectOptimisticLockingFailureException e) {
            return new ResponseEntity<>(HttpStatus.CONFLICT);
        }

        if (newRes == null)
            return new ResponseEntity<>(HttpStatus.UNPROCESSABLE_ENTITY);

        for (Adventure a : fishingInstructor.getAdventures()){
            if (a.getId().equals(regularResDTO.getRentalId())){
                newRes.setRental(a);
                break;
            }
        }
        //fishingInstructorService.save(fishingInstructor);
        reservationService.save(newRes);

        return new ResponseEntity<>(HttpStatus.OK);
    }




    @PostMapping(value = "/add-action-reservation")
    @PreAuthorize("hasRole('INSTRUCTOR')")
    @CrossOrigin(origins = ServerConfig.FRONTEND_ORIGIN)
    public ResponseEntity<HttpStatus> addNewActionRes(HttpServletRequest request, @RequestBody ActionResDTO actionResDTO) {
        String email = tokenUtils.getEmailDirectlyFromHeader(request);
        if (email == null)
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);

        FishingInstructor fishingInstructor = fishingInstructorService.findByEmail(email);
        if (fishingInstructor == null)
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);

        if (!actionResDTO.arePropsValidAdding())
            return new ResponseEntity<>(HttpStatus.UNPROCESSABLE_ENTITY);

        if (!fishingInstructorService.checkIfAdventureExists(fishingInstructor, actionResDTO.getRentalId()))
            return new ResponseEntity<>(HttpStatus.UNPROCESSABLE_ENTITY);

        Reservation newRes = null;
        try {
            newRes = reservationService.addNewActionRes(actionResDTO, "INSTRUCTOR");
        } catch(ObjectOptimisticLockingFailureException e) {
            return new ResponseEntity<>(HttpStatus.CONFLICT);
        }

        if (newRes == null)
            return new ResponseEntity<>(HttpStatus.UNPROCESSABLE_ENTITY);

        for (Adventure a : fishingInstructor.getAdventures()){
            if (a.getId().equals(actionResDTO.getRentalId())){
                newRes.setRental(a);
                break;
            }
        }
        //fishingInstructorService.save(fishingInstructor);
        reservationService.save(newRes);
        notifySubscribers(fishingInstructor, actionResDTO);

        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PreAuthorize("hasRole('INSTRUCTOR')")
    private void notifySubscribers(FishingInstructor fishingInstructor, ActionResDTO actionResDTO) {
        for (Subscription s : subscriptionService.getAllSubscriptions()){
            if (s.getOwner().getId().equals(fishingInstructor.getId()) && s.isActiveSubscription()){
                try {
                    emailService.sendMail(s.getClient().getEmail(), "New Action on the radar",
                            "New action reservation is waiting for you! Check this great deal" +
                                    " for rental of owner " + fishingInstructor.getName() + " " + fishingInstructor .getSurname() +
                                    " now for just " + String.valueOf(actionResDTO.getPrice()) + " euros!"
                    );
                } catch(Exception ignored){
                    //nikom nista
                }
            }
        }
    }

    @PutMapping(value="/buy-loyalty-program/{program}")
    @PreAuthorize("hasRole('INSTRUCTOR')")
    @CrossOrigin(origins = ServerConfig.FRONTEND_ORIGIN)
    public ResponseEntity<HttpStatus> buyLoyaltyProgram(@PathVariable String program, HttpServletRequest request) {
        String email = tokenUtils.getEmailDirectlyFromHeader(request);
        if (email == null)
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);

        if (
                !fishingInstructorService.updateLoyaltyProgram(fishingInstructorService.findByEmail(email), loyaltyProgramService.getAllLoyaltyPrograms(), program)
        )
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);

        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping(value="/ownersSearch")
    @PreAuthorize("hasRole('INSTRUCTOR')")
    @CrossOrigin(origins = ServerConfig.FRONTEND_ORIGIN)
    public ResponseEntity<List<OwnersSearchResDTO>> search(HttpServletRequest request, @RequestParam String minPrice,
                                                           @RequestParam String maxPrice, @RequestParam String location,
                                                           @RequestParam String minCapacity, @RequestParam String serviceName) {
        String email = tokenUtils.getEmailDirectlyFromHeader(request);
        if (email == null)
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);

        FishingInstructor fishingInstructor = fishingInstructorService.findByEmail(email);
        if (fishingInstructor == null)
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);

        if (!Validate.validatePrice(minPrice) || !Validate.validatePrice(maxPrice)
                || !Validate.validatePrice(minCapacity) || (!location.equals("") && !Validate.validateWords(location)))
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);

        List<OwnersSearchResDTO> rentals = new ArrayList<>();
        try {
            rentals = fishingInstructorService.search(fishingInstructor, minPrice, maxPrice, location, minCapacity, serviceName);
        } catch (Exception ignored) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        return new ResponseEntity<>(rentals, HttpStatus.OK );
    }

    @GetMapping(value = "/get-chart-data/{selectedGraph}/{selectedPeriod}/{selectedMonth}")
    @PreAuthorize("hasRole('INSTRUCTOR')")
    @CrossOrigin(origins = ServerConfig.FRONTEND_ORIGIN)
    public ResponseEntity<List<List<String>>> getChartData(@PathVariable String selectedGraph, @PathVariable String selectedPeriod, @PathVariable String selectedMonth, HttpServletRequest request) {
        String email = tokenUtils.getEmailDirectlyFromHeader(request);
        if (email == null)
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);

        FishingInstructor fishingInstructor = fishingInstructorService.findByEmail(email);
        if (fishingInstructor == null)
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);

        List<List<String>> data = fishingInstructorService.getChartData(fishingInstructor, selectedGraph, selectedPeriod, selectedMonth, loyaltyProgramService.getAllLoyaltyPrograms());

        return new ResponseEntity<>(data, HttpStatus.OK);
    }

    @GetMapping(value="/find-one-rental/{id}")
    @PreAuthorize("hasRole('INSTRUCTOR')")
    @CrossOrigin(origins = ServerConfig.FRONTEND_ORIGIN)
    public ResponseEntity<AdventureDTO> getOneWithId(@PathVariable Long id, HttpServletRequest request) {
        String email = tokenUtils.getEmailDirectlyFromHeader(request);
        if (email == null)
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);

        FishingInstructor fishingInstructor = fishingInstructorService.findByEmail(email);
        if (fishingInstructor == null)
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);

        Set<Adventure> adventures = fishingInstructor.getAdventures();
        AdventureDTO returnAdventure = null;
        for (Adventure c : adventures) {
            if (!c.isDeleted() && c.getId().equals(id)) returnAdventure = new AdventureDTO(c);
        }

        if (returnAdventure == null)
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);

        return new ResponseEntity<AdventureDTO>(returnAdventure, HttpStatus.OK);
    }

}