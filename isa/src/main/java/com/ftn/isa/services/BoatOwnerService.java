package com.ftn.isa.services;

import com.ftn.isa.DTO.BoatDTO;
import com.ftn.isa.DTO.BoatOwnerDTO;
import com.ftn.isa.DTO.CottageDTO;
import com.ftn.isa.DTO.OwnersSearchResDTO;
import com.ftn.isa.helpers.Validate;
import com.ftn.isa.model.*;
import com.ftn.isa.repository.BoatOwnerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static java.time.temporal.ChronoUnit.DAYS;

@Service
public class BoatOwnerService {
    @Autowired
    private BoatOwnerRepository boatOwnerRepository;

    public BoatOwner findByEmail(String email) {
        return boatOwnerRepository.findByEmail(email);
    }

    public BoatOwner getOwnerByBoatId(Long boatId) {
        return boatOwnerRepository.getOwnerByCBoatId(boatId);
    }

    public void save(BoatOwner boatOwner) {
        boatOwnerRepository.save(boatOwner);
    }

    public boolean save(BoatOwnerDTO boatOwnerDTO, BoatOwner bo) {
        boatOwnerDTO.hashPassword();
        bo.updatePersonalInfo(boatOwnerDTO);
        boatOwnerRepository.save(bo);
        return true;
    }

    public void deleteBoat(BoatOwner boatOwner, Long id) throws Exception {
        for (Boat b : boatOwner.getBoats()){
            if (b.getId() == id){
                if (b.isDeleted())
                    throw new Exception("Cottage with this id is already deleted");
                else if (b.hasUpcomingReservations())
                    throw new Exception("Cottage cannot be deleted due to upcoming reservations");
                b.setDeleted(true);
                break;
            }
        }
        boatOwnerRepository.save(boatOwner);
    }

    @Transactional(propagation = Propagation.REQUIRED)
    public void save(BoatOwner boatOwner, BoatDTO boatDTO, Set<Photo> photos){
        for (Boat boat : boatOwner.getBoats()){
            if (boat.getId() == boatDTO.getId()) {
                boat.setPhotos(photos);
                boat.getAddress().setPlaceName(boatDTO.getCity());
                boat.getAddress().setCountry(boatDTO.getCountry());
                boat.getAddress().setStreet(boatDTO.getStreet());
                boat.getAddress().setLon(boatDTO.getLon());
                boat.getAddress().setLat(boatDTO.getLat());
                boat.setNavigationEquipment(boatDTO.getNavigationEquipment());
                boat.setFishingEquipment(boatDTO.getFishingEquipment());
                boat.setCapacity(boatDTO.getCapacity());
                boat.setDescription(boatDTO.getDescription());
                boat.setName(boatDTO.getName());
                boat.setPrice(boatDTO.getPrice());
                boat.setRules(boatDTO.getRules());
                boat.setType(boatDTO.getType());
                boat.setBoatLength(boatDTO.getBoatLength());
                boat.setEngineNumber(boatDTO.getEngineNumber());
                boat.setEnginePower(boatDTO.getEnginePower());
                boat.setMaxSpeed(boatDTO.getMaxSpeed());
            }
        }

        boatOwnerRepository.save(boatOwner);
    }

    public void addNewBoat(BoatDTO boatDTO, BoatOwner boatOwner) {
        Set<Photo> photos = new HashSet<>();
        for (String p : boatDTO.getPhotos())
            photos.add(new Photo(p));

        Address address = new Address(boatDTO.getCountry(), boatDTO.getCity(), boatDTO.getStreet(),
                boatDTO.getLon(), boatDTO.getLat());

        Boat boat = new Boat(boatDTO.getName(), boatDTO.getDescription(),
                boatDTO.getCapacity(), boatDTO.getRules(),
                false, address, boatDTO.getAverageRating(), boatDTO.getNoRatings(),
                RentalType.BOAT, boatDTO.getPrice(), boatDTO.getType(), boatDTO.getBoatLength(), boatDTO.getEngineNumber(),
                boatDTO.getEnginePower(), boatDTO.getMaxSpeed(), boatDTO.getNavigationEquipment(), boatDTO.getFishingEquipment());

        boat.setPhotos(photos);
        boat.setAddress(address);
        boatOwner.getBoats().add(boat);
        this.save(boatOwner);
    }

    public List<OwnersSearchResDTO> search(BoatOwner boatOwner, String minPrice, String maxPrice, String location, String minCapacity, String serviceName) {
        List<OwnersSearchResDTO> rentals = new ArrayList<>();
        for (Boat c : boatOwner.getBoats()){
            if (!c.isDeleted() && (c.getPrice() >= Double.parseDouble(minPrice))
                    && (c.getPrice() <= Double.parseDouble(maxPrice)) && (c.getAddress().getPlaceName().equalsIgnoreCase(location))
                    && (c.getCapacity() >= Double.parseDouble(minCapacity))) {
                if (serviceName.equals(""))
                    rentals.add(new OwnersSearchResDTO(c));
                else
                if ((c.getName().equalsIgnoreCase(serviceName)))
                    rentals.add(new OwnersSearchResDTO(c));
            }
        }
        return rentals;
    }

    public List<List<String>> getChartData(BoatOwner boatOwner, String selectedGraph, String selectedPeriod, String selectedMonth, List<LoyaltyProgram> loyaltyPrograms) {
        List<List<String>> data = null;
        data = this.makeBarChart(boatOwner, selectedGraph, selectedPeriod, selectedMonth, loyaltyPrograms);

        return data;
    }

    public List<List<String>> populateBarChart(BoatOwner boatOwner, LocalDateTime start, LocalDateTime end, String graph){
        List<List<String>> data = new ArrayList<>();
        List<String> tempInner = new ArrayList<>();
        tempInner.add("Rental");
        tempInner.add(graph);
        data.add(tempInner);

        for (Boat c : boatOwner.getBoats()) {
            double counter = 0;
            List<String> innerList = new ArrayList<>();
            innerList.add(c.getName());
            for (Reservation res : c.getReservations()){
                if ((!res.isCanceled() && !res.isUnavailable() && res.isReserved()) && (
                        res.getStartTime().isAfter(start) || res.getStartTime().isEqual(start))
                        && (res.getEndTime().isBefore(end) || res.getEndTime().isEqual(end))){
                    if (DAYS.between(res.getStartTime(), res.getEndTime()) == 0) counter += 1;
                    else counter += DAYS.between(res.getStartTime(), res.getEndTime());
                } else if ((!res.isCanceled() && !res.isUnavailable() && res.isReserved()) &&
                        ((res.getStartTime().isBefore(start) && (res.getEndTime().isAfter(start)) && (res.getEndTime().isBefore(end) || res.getEndTime().equals(end))))) {
                    if (DAYS.between(start, res.getEndTime()) == 0) counter += 1;
                    else counter += DAYS.between(start, res.getEndTime());
                } else if ((!res.isCanceled() && !res.isUnavailable() && res.isReserved()) &&
                        (((res.getStartTime().isAfter(start) || res.getStartTime().equals(start)) && (res.getStartTime().isBefore(end)) && (res.getEndTime().isAfter(end) || res.getEndTime().equals(end))))) {
                    if (DAYS.between(res.getStartTime(), end) == 0) counter += 1;
                    else counter += DAYS.between(res.getStartTime(), end);
                }
            }
            if (counter < 0) counter = 0;
            innerList.add(String.valueOf(counter));
            data.add(innerList);
        }
        return data;
    }

    public List<List<String>> populateBarChartRevenue(BoatOwner boatOwner, LocalDateTime start, LocalDateTime end, String graph, Double increaseRev){
        List<List<String>> data = new ArrayList<>();
        List<String> tempInner = new ArrayList<>();
        tempInner.add("Rental");
        tempInner.add(graph);

        data.add(tempInner);

        for (Boat c : boatOwner.getBoats()) {
            double counter = 0;
            List<String> innerList = new ArrayList<>();
            innerList.add(c.getName());
            for (Reservation res : c.getReservations()){
                if ((res.getStartTime().isAfter(start) || res.getStartTime().equals(start)) && res.getStartTime().isBefore(end))
                    counter += (DAYS.between(res.getStartTime(), res.getEndTime())) * (res.getPrice() + res.getPrice() * increaseRev/100);
            }
            if (counter < 0) counter = 0;
            innerList.add(String.valueOf(counter));
            data.add(innerList);
        }
        return data;
    }

    private List<List<String>> makeBarChart(BoatOwner boatOwner, String selectedGraph, String selectedPeriod, String selectedMonth, List<LoyaltyProgram> loyaltyPrograms) {
        List<List<String>> data = new ArrayList<>();
        LocalDateTime start = null;
        LocalDateTime end = null;
        double increaseRevenue = 0;

        LoyaltyType loyaltyType = boatOwner.getLoyaltyType();
        for (LoyaltyProgram lp : loyaltyPrograms){
            if (lp.getLoyaltyType().equals(loyaltyType)) {increaseRevenue = lp.getIncrease();}
        }

        if (selectedGraph.equalsIgnoreCase("Occupancy")){
            if (selectedPeriod.equalsIgnoreCase("Weekly")){
                start = Validate.getCurrentWeekMonday();
                end = Validate.getCurrentWeekSunday();
                data = this.populateBarChart(boatOwner, start, end, selectedGraph);
            } else if (selectedPeriod.equalsIgnoreCase("Monthly")) {
                //monthly
                start = Validate.getSelectedMonthStart(selectedMonth);
                end = Validate.getSelectedMonthEnd(selectedMonth);
                data = this.populateBarChart(boatOwner, start, end, selectedGraph);
            } else {
                //yearly
                start = Validate.getStartOfYear();
                end = Validate.getEndOfYear();
                data = this.populateBarChart(boatOwner, start, end, selectedGraph);
            }
        }
        else
        {
            //revenue
            if (selectedPeriod.equalsIgnoreCase("Weekly")){
                start = Validate.getCurrentWeekMonday();
                end = Validate.getCurrentWeekSunday();
                data = this.populateBarChartRevenue(boatOwner, start, end, selectedGraph, increaseRevenue);
            } else if (selectedPeriod.equalsIgnoreCase("Monthly")) {
                //monthly
                start = Validate.getSelectedMonthStart(selectedMonth);
                end = Validate.getSelectedMonthEnd(selectedMonth);
                data = this.populateBarChartRevenue(boatOwner, start, end, selectedGraph, increaseRevenue);
            } else {
                //yearly
                start = Validate.getStartOfYear();
                end = Validate.getEndOfYear();
                data = this.populateBarChartRevenue(boatOwner, start, end, selectedGraph, increaseRevenue);
            }
        }

        return data;
    }

    public boolean checkIfCurrentResInProgress(Client client, BoatOwner boatOwner, List<Reservation> reservations) {
        for (Reservation res : reservations){
            if (Validate.getTodaysDate().isAfter(res.getStartTime()) && Validate.getTodaysDate().isBefore(res.getEndTime())
                    && !res.isCanceled() && res.isReserved() && res.getClient().getId().equals(client.getId()))
            {
                for (Boat a : boatOwner.getBoats())
                    if (a.getId().equals(res.getRental().getId())) return true;
            }
        }
        return false;
    }

    public boolean checkIfBoatExists(BoatOwner boatOwner, Long boatId) {
        for (Boat c : boatOwner.getBoats()){
            if (c.getId().equals(boatId))
                return true;
        }
        return false;
    }

    public boolean updateLoyaltyProgram(BoatOwner boatOwner, List<LoyaltyProgram> programs, String newProgram) {
        LoyaltyType type = LoyaltyType.REGULAR;
        switch (newProgram) {
            case "BRONZE":
                type = LoyaltyType.BRONZE;
                break;
            case "SILVER":
                type = LoyaltyType.SILVER;
                break;
            case "GOLD":
                type = LoyaltyType.GOLD;
                break;
        }

        for (LoyaltyProgram lp : programs) {
            if (lp.getLoyaltyType() == type) {
                int leftPoints = boatOwner.getLoyaltyPoints() - lp.getPrice();
                if (leftPoints >= 0)
                    boatOwner.setLoyaltyPoints(leftPoints);
                else
                    return false;

                boatOwner.setLoyaltyType(type);
                boatOwnerRepository.save(boatOwner);
                return true;
            }
        }
        return false;
    }

}
