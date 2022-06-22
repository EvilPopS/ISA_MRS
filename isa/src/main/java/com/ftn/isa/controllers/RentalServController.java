package com.ftn.isa.controllers;

import com.ftn.isa.DTO.*;
import com.ftn.isa.configs.ServerConfig;
import com.ftn.isa.helpers.Validate;
import com.ftn.isa.model.*;
import com.ftn.isa.services.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;

import javax.transaction.Transactional;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.logging.SimpleFormatter;

@RestController
@RequestMapping(value="api/rental")
public class RentalServController {
    @Autowired
    private RentalServService rentalServService;
    @Autowired
    private CottageOwnerService cottageOwnerService;
    @Autowired
    private FishingInstructorService instructorService;
    @Autowired
    private BoatOwnerService boatOwnerService;
    @Autowired
    private ReviewService reviewService;

    @Autowired
    private ReservationService reservationService;

    @GetMapping(value="/search")
    @CrossOrigin(origins = ServerConfig.FRONTEND_ORIGIN)
    public ResponseEntity<List<RentalSearchResDTO>> search(@RequestParam String startDate, @RequestParam String endDate,
                                                           @RequestParam String minPrice, @RequestParam String maxPrice,
                                                           @RequestParam String location, @RequestParam String minRate,
                                                           @RequestParam String maxRate, @RequestParam String entities) {
        if (!Validate.validateDatePeriod(startDate, endDate) || !Validate.validatePrice(minPrice)
                || !Validate.validatePrice(maxPrice) || !Validate.validateRating(minRate) || !Validate.validateRating(maxRate)
                || (!location.equals("") && !Validate.validateWords(location)) || !Validate.validateSearchEntities(entities))
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);

        SimpleDateFormat sf = new SimpleDateFormat("dd/MM/yyyy");
        List<RentalSearchResDTO> rentals = new ArrayList<>();
        try {
            for (RentalService adv : rentalServService.searchForRentals(sf.parse(startDate), sf.parse(endDate),
                                                                    minPrice.equals("") ? -1 : Double.parseDouble(minPrice),
                                                                    maxPrice.equals("") ? -1 : Double.parseDouble(maxPrice),
                                                                    location,
                                                                    minRate.equals("") ? -1 : Double.parseDouble(minRate),
                                                                    maxRate.equals("") ? -1 : Double.parseDouble(maxRate),
                                                                    entities))
                rentals.add(new RentalSearchResDTO(adv));
        } catch (Exception ignored) {}

        return new ResponseEntity<>(rentals, HttpStatus.OK );
    }

    @GetMapping(value="/basic/{id}")
    @CrossOrigin(origins = ServerConfig.FRONTEND_ORIGIN)
    public ResponseEntity<BasicEntityInfoDTO> getEntityBasicInfo(@RequestParam String type, @PathVariable Long id) {
        try {
            return new ResponseEntity<>(
                                        new BasicEntityInfoDTO(rentalServService.getEntityByTypeAndId(type, id)),
                                        HttpStatus.OK
                                    );
        } catch (Exception ignored) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping(value="/cottage/details/{id}")
    @PreAuthorize("hasRole('CLIENT')")
    @CrossOrigin(origins = ServerConfig.FRONTEND_ORIGIN)
    @Transactional
    public ResponseEntity<DetailedCottageInfoDTO> getCottageDetailedInfo(@PathVariable Long id) {
        Cottage cottage = rentalServService.getCottageById(id);
        if (cottage == null)
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        return new ResponseEntity<>(
                    new DetailedCottageInfoDTO(cottage, cottageOwnerService.getOwnerByCottageId(id)),
                    HttpStatus.OK
        );
    }

    @GetMapping(value="/get-reservations-by-rental/{id}")
    @PreAuthorize("hasRole('COTTAGE_OWNER') || hasRole('INSTRUCTOR') || hasRole('BOAT_OWNER')") // za pocetak samo ovak oposle dodati sve lagnao ce raditi
    @CrossOrigin(origins = ServerConfig.FRONTEND_ORIGIN)
    public ResponseEntity<List<CalendarReservationDTO>> getReservationsByRental(@PathVariable Long id){
        List<Reservation> reservations = reservationService.getAllReservations();
        List<CalendarReservationDTO> calendarReservationDTOS = new ArrayList<>();
        reservations = reservationService.findUpcomingReservationsByRentalId(id, reservations);
        for (Reservation res : reservations){calendarReservationDTOS.add(new CalendarReservationDTO(res));}


        return new ResponseEntity<>(calendarReservationDTOS, HttpStatus.OK);
    }

    @GetMapping(value="/boat/details/{id}")
    @PreAuthorize("hasRole('CLIENT')")
    @CrossOrigin(origins = ServerConfig.FRONTEND_ORIGIN)
    public ResponseEntity<DetailedBoatInfoDTO> getBoatDetailedInfo(@PathVariable Long id) {
        Boat boat = rentalServService.getBoatById(id);
        if (boat == null)
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        return new ResponseEntity<>(
                new DetailedBoatInfoDTO(boat, boatOwnerService.getOwnerByBoatId(id)),
                HttpStatus.OK
        );
    }

    @GetMapping(value="/adventure/details/{id}")
    @PreAuthorize("hasRole('CLIENT')")
    @CrossOrigin(origins = ServerConfig.FRONTEND_ORIGIN)
    public ResponseEntity<DetailedAdventureInfoDTO> getAdventureDetailedInfo(@PathVariable Long id) {
        Adventure adventure = rentalServService.getAdventureById(id);
        if (adventure == null)
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        return new ResponseEntity<>(
                new DetailedAdventureInfoDTO(adventure, instructorService.getOwnerByAdventureId(id)),
                HttpStatus.OK
        );
    }

    @PostMapping(value="/gradeUpdate")
    @PreAuthorize("hasRole('ADMIN')")
    @CrossOrigin(origins = ServerConfig.FRONTEND_ORIGIN)
    public ResponseEntity<HttpStatus> updateRentalServiceRate(@RequestBody ReviewDTO gradedReview) throws Exception {

        String rentalTypeString = RentalType.values()[gradedReview.getRentalServiceType()].name().toLowerCase();
        rentalTypeString = rentalTypeString.substring(0, 1).toUpperCase() + rentalTypeString.substring(1).toLowerCase();


        rentalServService.updateRentalGrade(gradedReview.getRentalServiceId(), rentalTypeString, gradedReview.getGrade());
        Review foundReview = reviewService.getReviewById(gradedReview.getReviewId());
        foundReview.setAnswered(true);
        reviewService.save(foundReview);

        return new ResponseEntity<>(HttpStatus.OK);
    }


}
