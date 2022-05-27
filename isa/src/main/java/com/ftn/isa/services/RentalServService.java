package com.ftn.isa.services;

import com.ftn.isa.model.*;
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
            case "Cottage":
                return cottageRepository.getCottageById(id);
            case "Boat":
                return boatRepository.getBoatById(id);
            case "Adventure":
                return adventureRepository.getAdventureById(id);
        }
        throw new Exception("Type is invalid!");
    }

    public Cottage getCottageById(Long id) {
        return cottageRepository.getCottageById(id);
    }

    public Boat getBoatById(Long id) {
        return boatRepository.getBoatById(id);
    }

    public Adventure getAdventureById(Long id) {
        return adventureRepository.getAdventureById(id);
    }

    public void addReservationToRental(Long rentalId, String rentalType, Reservation reservation) {
        switch (rentalType) {
            case "Cottage":
                Cottage cottage = cottageRepository.getCottageById(rentalId);
                cottage.getReservations().add(reservation);
                cottageRepository.save(cottage);
                break;
            case "Boat":
                Boat boat = boatRepository.getBoatById(rentalId);
                boat.getReservations().add(reservation);
                boatRepository.save(boat);
                break;
            case "Adventure":
                Adventure adventure = adventureRepository.getAdventureById(rentalId);
                adventure.getReservations().add(reservation);
                adventureRepository.save(adventure);
                break;
        }
    }

    public void updateRentalGrade(Long rentalId, String rentalType, Double grade) {
        Double currentGradeSum = 0.0;
        switch (rentalType) {
            case "Cottage":
                Cottage cottage = cottageRepository.getCottageById(rentalId);
                if (cottage.getNoRatings() == 0){
                    currentGradeSum = cottage.getAverageRate();
                } else {
                    currentGradeSum = cottage.getAverageRate() * cottage.getNoRatings();
                }

                currentGradeSum += grade;
                cottage.setNoRatings(cottage.getNoRatings() + 1);

                cottage.setAverageRate(currentGradeSum / cottage.getNoRatings());
                cottageRepository.save(cottage);
                break;
            case "Boat":
                Boat boat = boatRepository.getBoatById(rentalId);
                if (boat.getNoRatings() == 0){
                    currentGradeSum = boat.getAverageRate();
                } else {
                    currentGradeSum = boat.getAverageRate() * boat.getNoRatings();
                }
                currentGradeSum += grade;
                boat.setNoRatings(boat.getNoRatings() + 1);

                boat.setAverageRate(currentGradeSum / boat.getNoRatings());
                boatRepository.save(boat);
                break;
            case "Adventure":
                Adventure adventure = adventureRepository.getAdventureById(rentalId);
                if (adventure.getNoRatings() == 0){
                    currentGradeSum = adventure.getAverageRate();
                } else {
                    currentGradeSum = adventure.getAverageRate() * adventure.getNoRatings();
                }
                currentGradeSum += grade;
                adventure.setNoRatings(adventure.getNoRatings() + 1);

                adventure.setAverageRate(currentGradeSum / adventure.getNoRatings());
                adventureRepository.save(adventure);
                break;
        }
    }
}
