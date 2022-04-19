package com.ftn.isa.controllers;

import com.ftn.isa.DTO.RentalSearchResDTO;
import com.ftn.isa.helpers.Validate;
import com.ftn.isa.model.Adventure;
import com.ftn.isa.model.RentalService;
import com.ftn.isa.services.RentalServService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping(value="api/rental")
public class RentalServController {
    @Autowired
    private RentalServService rentalServService;

    @GetMapping(value="/search")
    @CrossOrigin(origins = "http://localhost:8081")
    public ResponseEntity<List<RentalSearchResDTO>> search(@RequestParam String startDate, @RequestParam String endDate,
                                                           @RequestParam String minPrice, @RequestParam String maxPrice,
                                                           @RequestParam String location, @RequestParam String  rate,
                                                           @RequestParam String entities) {

/*        if (Validate.validateDatePeriod(startDate, endDate) || Validate.validatePrice(minPrice)
                || Validate.validatePrice(maxPrice) || Validate.validateRating(rate)
                || Validate.validateWords(location))
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);*/

        List<RentalSearchResDTO> l = new ArrayList<>();
        for (RentalService a : rentalServService.searchForRentals(startDate, endDate, minPrice,
                                                                maxPrice, location, rate, entities))
            l.add(new RentalSearchResDTO(a));

        return new ResponseEntity<>(l, HttpStatus.OK );
    }

}
