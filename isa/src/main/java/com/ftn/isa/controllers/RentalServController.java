package com.ftn.isa.controllers;

import com.ftn.isa.DTO.BasicEntityInfoDTO;
import com.ftn.isa.DTO.RentalSearchResDTO;
import com.ftn.isa.helpers.Validate;
import com.ftn.isa.model.Adventure;
import com.ftn.isa.model.RentalService;
import com.ftn.isa.services.RentalServService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
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
    @CrossOrigin(origins = "http://localhost:8081")
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
    @CrossOrigin(origins = "http://localhost:8081")
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

}
