package com.ftn.isa.controllers;

import com.ftn.isa.DTO.AdventureDTO;
import com.ftn.isa.DTO.FishingInstructorDTO;
import com.ftn.isa.DTO.OwnerRegDTO;
import com.ftn.isa.DTO.ReservationDTO;
import com.ftn.isa.configs.ServerConfig;
import com.ftn.isa.model.*;
import com.ftn.isa.security.auth.TokenUtils;
import com.ftn.isa.services.*;
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


    @GetMapping(value = "/{email}/searchAdventure")
    @PreAuthorize("hasRole('INSTRUCTOR')")
    public ResponseEntity<List<AdventureDTO>> searchAdventureByName(@RequestParam String adventureName, @PathVariable String email){
        FishingInstructor fishingInstructor = fishingInstructorService.findByEmail(email);
        List<Adventure> adventures = adventureService.searchAdventureByName(adventureName, fishingInstructor.getId());
        List<AdventureDTO> adventureDTOs = new ArrayList<>();
        for (Adventure a : adventures){adventureDTOs.add(new AdventureDTO(a));}

        return  new ResponseEntity<List<AdventureDTO>>(adventureDTOs, HttpStatus.OK);


    }


    @GetMapping(value="/{email}")
    @PreAuthorize("hasRole('INSTRUCTOR')")
    public ResponseEntity<FishingInstructorDTO> getByEmail(@PathVariable String email) {
        FishingInstructor fishingInstructor = fishingInstructorService.findByEmail(email);
        if (fishingInstructor == null)
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        return new ResponseEntity<>(new FishingInstructorDTO(fishingInstructor), HttpStatus.OK);
    }

    @GetMapping(value="/{email}/adventures")
    @PreAuthorize("hasRole('INSTRUCTOR')")
    public ResponseEntity<Set<AdventureDTO>> getAllAdventures(@PathVariable String email){
        FishingInstructor fishingInstructor = fishingInstructorService.findByEmail(email);
        if (fishingInstructor == null){
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        Set<Adventure> adventures = fishingInstructor.getAdventures();
        Set<AdventureDTO> adventureDTOS = new HashSet<>();
        for (Adventure a : adventures){
            if (!a.isDeleted()) {
                adventureDTOS.add(new AdventureDTO(a));}
            };
        return new ResponseEntity<Set<AdventureDTO>>(adventureDTOS, HttpStatus.OK);
    }

    @PostMapping(value="/{email}/adventures/addAdventure")
    @PreAuthorize("hasRole('INSTRUCTOR')")
    public ResponseEntity<HttpStatus> addAdventure(@PathVariable String email, @RequestBody AdventureDTO adventureDTO){
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

    @PutMapping(value="/{email}/adventureUpdate/{id}")
    @PreAuthorize("hasRole('INSTRUCTOR')")
    public ResponseEntity<HttpStatus> updateAdventure(@RequestBody AdventureDTO adventureData, @PathVariable String email, @PathVariable String id){
        FishingInstructor fishingInstructor = fishingInstructorService.findByEmail(email);

        if (fishingInstructor == null){
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

//        if(!adventureData.propsValid())
//            return new ResponseEntity<>(HttpStatus.UNPROCESSABLE_ENTITY);
        adventureData.setId(Long.parseLong(id));
        fishingInstructorService.updateAdventure(fishingInstructor, adventureData);

        return new ResponseEntity<>(HttpStatus.OK);

    }

    @DeleteMapping(value="/{email}/deleteAdventure/{id}")
    @PreAuthorize("hasRole('INSTRUCTOR')")
    public ResponseEntity<HttpStatus> deleteAdventure(@PathVariable String email, @PathVariable String id){
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


}