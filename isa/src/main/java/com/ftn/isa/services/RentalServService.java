package com.ftn.isa.services;

import com.ftn.isa.model.RentalService;
import com.ftn.isa.repository.AdventureRepository;
import com.ftn.isa.repository.BoatRepository;
import com.ftn.isa.repository.CottageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.stream.Stream;

@Service
public class RentalServService {
    @Autowired
    private AdventureRepository adventureRepository;

    @Autowired
    private BoatRepository boatRepository;

    @Autowired
    private CottageRepository cottageRepository;

    public List<RentalService> searchForRentals(Date startDate, Date endDate, double minPrice, double maxPrice,
                                                String location, double minRate, double maxRate, String entities) {
        List<RentalService> rentals = new ArrayList<>();
        List<RentalService> adventures = null;
        List<RentalService> cottages = null;
        List<RentalService> boats = null;

        if (entities.contains("adventures"))
            adventures = new ArrayList<>(adventureRepository.searchAdventures(startDate, endDate, minPrice, maxPrice, location, minRate, maxRate));

        if (entities.contains("cottages"))
            cottages = new ArrayList<>(cottageRepository.searchCottages(startDate, endDate, minPrice, maxPrice, location, minRate, maxRate));

        if (entities.contains("boats"))
            boats = new ArrayList<>(boatRepository.searchBoats(startDate, endDate, minPrice, maxPrice, location, minRate, maxRate));

        Stream.of(adventures, cottages, boats).filter(Objects::nonNull).forEach(rentals::addAll);
        return rentals;
    }

    public RentalService getEntityByTypeAndId(String type, Long id) throws Exception {
        switch (type) {
            case "cottage":
                return cottageRepository.getCottageById(id);
            case "boat":
                return boatRepository.getBoatById(id);
            case "adventure":
                return adventureRepository.getAdventureById(id);
        }
        throw new Exception("Type is invalid!");
    }
}
