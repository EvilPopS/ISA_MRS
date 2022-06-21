package com.ftn.isa.services;


import com.ftn.isa.DTO.AdventureDTO;
import com.ftn.isa.DTO.FishingInstructorDTO;
import com.ftn.isa.DTO.OwnersSearchResDTO;
import com.ftn.isa.helpers.Validate;
import com.ftn.isa.model.*;
import com.ftn.isa.repository.FishingInstructorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static java.time.temporal.ChronoUnit.DAYS;

@Service
public class FishingInstructorService {

    @Autowired
    private FishingInstructorRepository fishingInstructorRepo;

    public FishingInstructor findByEmail(String email){return fishingInstructorRepo.findByEmail(email);}

    public void updatePersonalInfo(FishingInstructorDTO fishingInstructorData, FishingInstructor fishingInstructor) {
        fishingInstructorData.hashPassword();
        fishingInstructor.updatePersonalInfo(fishingInstructorData);
        fishingInstructorRepo.save(fishingInstructor);
    }

    public boolean checkIfCurrentResInProgress(Client client, FishingInstructor fishingInstructor, List<Reservation> reservations) {
        for (Reservation res : reservations){
            if (Validate.getTodaysDate().isAfter(res.getStartTime()) && Validate.getTodaysDate().isBefore(res.getEndTime())
                    && !res.isCanceled() && res.isReserved() && res.getClient().getId().equals(client.getId()))
            {
                for (Adventure a : fishingInstructor.getAdventures())
                    if (a.getId().equals(res.getRental().getId())) return true;
            }
        }
        return false;
    }

    public FishingInstructor getOwnerByAdventureId(Long advId) {
        return fishingInstructorRepo.getOwnerByAdventureId(advId);
    }
    public void save(FishingInstructor fishingInstructor){ fishingInstructorRepo.save(fishingInstructor);};

    public void updateAdventure(FishingInstructor fishingInstructor, AdventureDTO adventureData) {
        fishingInstructor.updateAdventure(adventureData);
        fishingInstructorRepo.save(fishingInstructor);
    }

    public void deleteAdventure(FishingInstructor fishingInstructor, Long id) {
        fishingInstructor.findAdventureById(id).setDeleted(true);
        fishingInstructorRepo.save(fishingInstructor);

    }

    public boolean checkIfAdventureExists(FishingInstructor fishingInstructor, Long adventureId) {
        for (Adventure a : fishingInstructor.getAdventures()){
            if (a.getId().equals(adventureId))
                return true;
        }
        return false;
    }

    public boolean updateLoyaltyProgram(FishingInstructor fishingInstructor, List<LoyaltyProgram> programs, String newProgram) {
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
                int leftPoints = fishingInstructor.getLoyaltyPoints() - lp.getPrice();
                if (leftPoints >= 0)
                    fishingInstructor.setLoyaltyPoints(leftPoints);
                else
                    return false;

                fishingInstructor.setLoyaltyType(type);
                fishingInstructorRepo.save(fishingInstructor);
                return true;
            }
        }
        return false;
    }

    public List<OwnersSearchResDTO> search(FishingInstructor fishingInstructor, String minPrice, String maxPrice, String location, String minCapacity, String serviceName) {
        List<OwnersSearchResDTO> rentals = new ArrayList<>();
        for (Adventure c : fishingInstructor.getAdventures()){
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

    public List<List<String>> getChartData(FishingInstructor fishingInstructor, String selectedGraph, String selectedPeriod, String selectedMonth, List<LoyaltyProgram> loyaltyPrograms) {
        List<List<String>> data = null;
        data = this.makeBarChart(fishingInstructor, selectedGraph, selectedPeriod, selectedMonth, loyaltyPrograms);

        return data;
    }

    public List<List<String>> populateBarChart(FishingInstructor fishingInstructor, LocalDateTime start, LocalDateTime end, String graph){
        List<List<String>> data = new ArrayList<>();
        List<String> tempInner = new ArrayList<>();
        tempInner.add("Rental");
        tempInner.add(graph);
        data.add(tempInner);

        for (Adventure c : fishingInstructor.getAdventures()) {
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

    public List<List<String>> populateBarChartRevenue(FishingInstructor fishingInstructor, LocalDateTime start, LocalDateTime end, String graph, Double increaseRev){
        List<List<String>> data = new ArrayList<>();
        List<String> tempInner = new ArrayList<>();
        tempInner.add("Rental");
        tempInner.add(graph);

        data.add(tempInner);

        for (Adventure c : fishingInstructor.getAdventures()) {
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

    private List<List<String>> makeBarChart(FishingInstructor fishingInstructor, String selectedGraph, String selectedPeriod, String selectedMonth, List<LoyaltyProgram> loyaltyPrograms) {
        List<List<String>> data = new ArrayList<>();
        LocalDateTime start = null;
        LocalDateTime end = null;
        double increaseRevenue = 0;

        LoyaltyType loyaltyType = fishingInstructor.getLoyaltyType();
        for (LoyaltyProgram lp : loyaltyPrograms){
            if (lp.getLoyaltyType().equals(loyaltyType)) {increaseRevenue = lp.getIncrease();}
        }

        if (selectedGraph.equalsIgnoreCase("Occupancy")){
            if (selectedPeriod.equalsIgnoreCase("Weekly")){
                start = Validate.getCurrentWeekMonday();
                end = Validate.getCurrentWeekSunday();
                data = this.populateBarChart(fishingInstructor, start, end, selectedGraph);
            } else if (selectedPeriod.equalsIgnoreCase("Monthly")) {
                //monthly
                start = Validate.getSelectedMonthStart(selectedMonth);
                end = Validate.getSelectedMonthEnd(selectedMonth);
                data = this.populateBarChart(fishingInstructor, start, end, selectedGraph);
            } else {
                //yearly
                start = Validate.getStartOfYear();
                end = Validate.getEndOfYear();
                data = this.populateBarChart(fishingInstructor, start, end, selectedGraph);
            }
        }
        else
        {
            //revenue
            if (selectedPeriod.equalsIgnoreCase("Weekly")){
                start = Validate.getCurrentWeekMonday();
                end = Validate.getCurrentWeekSunday();
                data = this.populateBarChartRevenue(fishingInstructor, start, end, selectedGraph, increaseRevenue);
            } else if (selectedPeriod.equalsIgnoreCase("Monthly")) {
                //monthly
                start = Validate.getSelectedMonthStart(selectedMonth);
                end = Validate.getSelectedMonthEnd(selectedMonth);
                data = this.populateBarChartRevenue(fishingInstructor, start, end, selectedGraph, increaseRevenue);
            } else {
                //yearly
                start = Validate.getStartOfYear();
                end = Validate.getEndOfYear();
                data = this.populateBarChartRevenue(fishingInstructor, start, end, selectedGraph, increaseRevenue);
            }
        }

        return data;
    }


}
