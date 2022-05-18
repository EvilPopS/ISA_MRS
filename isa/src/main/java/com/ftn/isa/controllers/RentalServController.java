package com.ftn.isa.controllers;

import com.ftn.isa.DTO.*;
import com.ftn.isa.configs.ServerConfig;
import com.ftn.isa.helpers.Validate;
import com.ftn.isa.model.Adventure;
import com.ftn.isa.model.Boat;
import com.ftn.isa.model.Cottage;
import com.ftn.isa.model.RentalService;
import com.ftn.isa.services.RentalServService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.SimpleFormatter;

@RestController
@RequestMapping(value="api/rental")
public class RentalServController {
    @Autowired
    private RentalServService rentalServService;

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
    public ResponseEntity<DetailedCottageInfoDTO> getCottageDetailedInfo(@PathVariable Long id) {
        Cottage cottage = rentalServService.getCottageById(id);
        if (cottage == null)
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        return new ResponseEntity<>(new DetailedCottageInfoDTO(cottage), HttpStatus.OK);
    }

    @GetMapping(value="/boat/details/{id}")
    @PreAuthorize("hasRole('CLIENT')")
    @CrossOrigin(origins = ServerConfig.FRONTEND_ORIGIN)
    public ResponseEntity<DetailedBoatInfoDTO> getBoatDetailedInfo(@PathVariable Long id) {
        Boat boat = rentalServService.getBoatById(id);
        if (boat == null)
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        return new ResponseEntity<>(new DetailedBoatInfoDTO(boat), HttpStatus.OK);
    }

    @GetMapping(value="/adventure/details/{id}")
    @PreAuthorize("hasRole('CLIENT')")
    @CrossOrigin(origins = ServerConfig.FRONTEND_ORIGIN)
    public ResponseEntity<DetailedAdventureInfoDTO> getAdventureDetailedInfo(@PathVariable Long id) {
        Adventure adventure = rentalServService.getAdventureById(id);
        if (adventure == null)
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        return new ResponseEntity<>(new DetailedAdventureInfoDTO(adventure), HttpStatus.OK);
    }

}
