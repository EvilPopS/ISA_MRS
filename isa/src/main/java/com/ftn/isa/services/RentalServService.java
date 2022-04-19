package com.ftn.isa.services;

import com.ftn.isa.DTO.RentalSearchResDTO;
import com.ftn.isa.model.Adventure;
import com.ftn.isa.model.RentalService;
import com.ftn.isa.repository.AdventureRepository;
import com.ftn.isa.repository.BoatRepository;
import com.ftn.isa.repository.CottageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class RentalServService {
    @Autowired
    private AdventureRepository adventureRepository;

    @Autowired
    private BoatRepository boatRepository;

    @Autowired
    private CottageRepository cottageRepository;

    public List<RentalService> searchForRentals(String startDate, String endDate, String minPrice, String maxPrice,
                                                String location, String  rate, String entities) {
        Date date1 = null;
        Date date2 = null;
        double minP = -1;
        double maxP = -1;
        double rating = -1;
        try {
            date1 = new SimpleDateFormat("dd/MM/yyyy").parse(startDate);
            date2 = new SimpleDateFormat("dd/MM/yyyy").parse(endDate);
            minP = Double.parseDouble(minPrice);
            maxP = Double.parseDouble(maxPrice);
            rating = Double.parseDouble(rate);
        } catch(Exception ignored) {}

        return new ArrayList<>(adventureRepository.searchAdventures(date1, date2, minP, maxP, location, rating));
    }
}
