package com.ftn.isa.controllers;

import com.ftn.isa.DTO.*;
import com.ftn.isa.model.*;
import com.ftn.isa.services.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashSet;
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

        if (!cottageOwnerData.arePropsValid())
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
        for (Cottage c : cottages) {
            if (!c.isDeleted()) cottagesSet.add(new CottageDTO(c));
        }

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

        Set<Photo> photos = new HashSet<>();
        for (String p : cottageDTO.getPhotos())
            photos.add(new Photo(p));

        Address address = new Address(cottageDTO.getCountry(), cottageDTO.getCity(), cottageDTO.getStreet(),
                                        cottageDTO.getLon(), cottageDTO.getLat());

        Cottage cottage = new Cottage(cottageDTO.getName(), cottageDTO.getDescription(),
                cottageDTO.getCapacity(), cottageDTO.getRules(),
                false, address, cottageDTO.getAverageRating(), cottageDTO.getNoRatings(),
                RentalType.COTTAGE, cottageDTO.getPrice(), cottageDTO.getAdditionalServices(), cottageDTO.getNoRooms());

        cottage.setPhotos(photos);
        cottage.setAddress(address);
        cottageOwner.getCottages().add(cottage);
        cottageOwnerService.save(cottageOwner);

        return new ResponseEntity<>(HttpStatus.OK);
    }

    @DeleteMapping(value="/{email}/delete-cottage/{id}")
    public ResponseEntity<HttpStatus> deleteCottage(@PathVariable String email, @PathVariable String id) throws Exception {
        CottageOwner cottageOwner = cottageOwnerService.findByEmail(email);
        if (cottageOwner == null){
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        cottageOwnerService.deleteCottage(cottageOwner, Long.parseLong(id));
        return new ResponseEntity<>(HttpStatus.OK);

    }

    @CrossOrigin(origins = "http://localhost:8081")
    @PutMapping(consumes="application/json", value="/change-cottage-data/{email}")
    public ResponseEntity<HttpStatus> updateCottageData(@PathVariable String email, @RequestBody CottageDTO cottageDTO) {
        CottageOwner cottageOwner = cottageOwnerService.findByEmail(email);
        if (cottageOwner == null)
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);

        if (cottageDTO.arePropsValidAdding())
            return new ResponseEntity<>(HttpStatus.UNPROCESSABLE_ENTITY);

        Set<Photo> photos = new HashSet<>();
        for (Cottage c : cottageOwner.getCottages()){
            if (c.getId() == cottageDTO.getId()){
                photos = photoService.addOrDeletePhoto(c, cottageDTO);
                break;
            }
        }
        cottageOwnerService.save(cottageOwner, cottageDTO, photos);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @CrossOrigin(origins = "http://localhost:8081")
    @GetMapping(value="/all-reservations/{email}")
    public ResponseEntity<Set<ReservationDTO>> getAllReservations(@PathVariable String email) {
        CottageOwner cottageOwner = cottageOwnerService.findByEmail(email);
        if (cottageOwner == null)
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);

        Set<ReservationDTO> reservations = new HashSet<ReservationDTO>();
        for (Cottage c : cottageOwner.getCottages()){
            for (Reservation reservation : c.getReservations()){
                ReservationDTO reservationDTO = new ReservationDTO(reservation.getId(), c.getId(),
                        c.getName(), reservation.getStartTime(), reservation.getEndTime(),
                        reservation.getPrice(), reservation.isAction(), reservation.isReserved());
                reservations.add(reservationDTO);
            }
        }

        for (ReservationDTO resDTO : reservations) {
            for (Client client : clientService.getAllClients()) {
                for (Reservation r : client.getReservations()) {
                    if (resDTO.getReservationId() == r.getId()) {
                        resDTO.setClientEmail(client.getEmail());
                        resDTO.setClientProfilePhoto(client.getProfilePicture().getPhotoPath());
                        resDTO.setClientFullName(client.getName() + " " + client.getSurname());
                    }
                }
            }
        }

        return new ResponseEntity<Set<ReservationDTO>>(reservations, HttpStatus.OK);
    }

    @PostMapping(consumes="application/json", value="/register")
    @CrossOrigin(origins = "http://localhost:8081")
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
    public ResponseEntity<HttpStatus> activateAccount(@PathVariable String email){
        CottageOwner cottageOwner = cottageOwnerService.findByEmail(email);
        if (cottageOwner == null)
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);

        cottageOwner.setActive(true);
        cottageOwnerService.save(cottageOwner);

        return new ResponseEntity<>(HttpStatus.OK);
    }

}
