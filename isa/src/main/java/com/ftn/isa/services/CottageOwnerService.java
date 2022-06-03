package com.ftn.isa.services;

import com.ftn.isa.DTO.CottageDTO;
import com.ftn.isa.DTO.CottageOwnerDTO;
import com.ftn.isa.DTO.OwnersSearchResDTO;
import com.ftn.isa.helpers.Validate;
import com.ftn.isa.model.*;
import com.ftn.isa.repository.CottageOwnerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static java.time.temporal.ChronoUnit.DAYS;

@Service
public class CottageOwnerService {

    @Autowired
    public CottageOwnerRepository cottageOwnerRepository;

    public CottageOwner findByEmail(String email) {
        return cottageOwnerRepository.findByEmail(email);
    }

    public CottageOwner getOwnerByCottageId(Long cottageId) {
        return cottageOwnerRepository.getOwnerByCottageId(cottageId);
    }

    public boolean save(CottageOwnerDTO cottageOwnerData, CottageOwner co) {
        cottageOwnerData.hashPassword();
        co.updatePersonalInfo(cottageOwnerData);
        cottageOwnerRepository.save(co);
        return true;
    }

    public void save(CottageOwner cottageOwner) {
        cottageOwnerRepository.save(cottageOwner);
    }

    public void save(CottageOwner cottageOwner, CottageDTO cottageDTO, Set<Photo> photos){
        for (Cottage cottage : cottageOwner.getCottages()){
            if (cottage.getId() == cottageDTO.getId()) {
                cottage.setPhotos(photos);
                cottage.getAddress().setPlaceName(cottageDTO.getCity());
                cottage.getAddress().setCountry(cottageDTO.getCountry());
                cottage.getAddress().setStreet(cottageDTO.getStreet());
                cottage.getAddress().setLon(cottageDTO.getLon());
                cottage.getAddress().setLat(cottageDTO.getLat());
                cottage.setAdditionalServices(cottageDTO.getAdditionalServices());
                cottage.setNoRooms(cottageDTO.getNoRooms());
                cottage.setDescription(cottageDTO.getDescription());
                cottage.setCapacity(cottageDTO.getCapacity());
                cottage.setName(cottageDTO.getName());
                cottage.setPrice(cottageDTO.getPrice());
                cottage.setRules(cottageDTO.getRules());
            }
        }

        cottageOwnerRepository.save(cottageOwner);
    }


    public void deleteCottage(CottageOwner cottageOwner, Long id) throws Exception {
        for (Cottage c : cottageOwner.getCottages()){
            if (c.getId() == id){
                if (c.isDeleted())
                    throw new Exception("Cottage with this id is already deleted");
                else if (c.hasUpcomingReservations())
                    throw new Exception("Cottage cannot be deleted due to upcoming reservations");
                c.setDeleted(true);
                break;
            }
        }
        cottageOwnerRepository.save(cottageOwner);
    }

    public List<OwnersSearchResDTO> search(CottageOwner cottageOwner, String minPrice, String maxPrice, String location, String minCapacity, String serviceName) {
        List<OwnersSearchResDTO> rentals = new ArrayList<>();
        for (Cottage c : cottageOwner.getCottages()){
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

    public void addNewCottage(CottageDTO cottageDTO, CottageOwner cottageOwner) {
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
        this.save(cottageOwner);
    }

    public boolean checkIfCottageExists(CottageOwner cottageOwner, Long cottageId) {
        for (Cottage c : cottageOwner.getCottages()){
            if (c.getId().equals(cottageId))
                return true;
        }
        return false;
    }

    public List<List<String>> getChartData(CottageOwner cottageOwner, String selectedGraph, String selectedPeriod, String selectedMonth, List<LoyaltyProgram> loyaltyPrograms) {
        List<List<String>> data = null;
        data = this.makeBarChart(cottageOwner, selectedGraph, selectedPeriod, selectedMonth, loyaltyPrograms);

        return data;
    }

    public List<List<String>> populateBarChart(CottageOwner cottageOwner, LocalDateTime start, LocalDateTime end, String graph){
        List<List<String>> data = new ArrayList<>();
        List<String> tempInner = new ArrayList<>();
        tempInner.add("Rental");
        tempInner.add(graph);
        data.add(tempInner);

        for (Cottage c : cottageOwner.getCottages()) {
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
                    if (DAYS.between(res.getStartTime(), end) == 0 && counter <= 0) counter += 1;
                    else if (counter <= 0) counter += DAYS.between(res.getStartTime(), end);
                }
            }
            if (counter < 0) counter = 0;
            innerList.add(String.valueOf(counter));
            data.add(innerList);
        }
        return data;
    }

    public List<List<String>> populateBarChartRevenue(CottageOwner cottageOwner, LocalDateTime start, LocalDateTime end, String graph, Double increaseRev){
        List<List<String>> data = new ArrayList<>();
        List<String> tempInner = new ArrayList<>();
        tempInner.add("Rental");
        tempInner.add(graph);

        data.add(tempInner);

        for (Cottage c : cottageOwner.getCottages()) {
            double counter = 0;
            List<String> innerList = new ArrayList<>();
            innerList.add(c.getName());
            for (Reservation res : c.getReservations()){
                if ((!res.isCanceled() && !res.isUnavailable() && res.isReserved()) && (res.getStartTime().isAfter(start) && res.getStartTime().isBefore(end))){
                    counter += res.getPrice() + res.getPrice() * increaseRev/100;
                }   //za revenue je uzeto da mesec/nedelja u kome pocinje za nju se doda revenue
            }
            if (counter < 0) counter = 0;
            innerList.add(String.valueOf(counter));
            data.add(innerList);
        }
        return data;
    }

    private List<List<String>> makeBarChart(CottageOwner cottageOwner, String selectedGraph, String selectedPeriod, String selectedMonth, List<LoyaltyProgram> loyaltyPrograms) {
        List<List<String>> data = new ArrayList<>();
        LocalDateTime start = null;
        LocalDateTime end = null;
        double increaseRevenue = 0;

        LoyaltyType loyaltyType = cottageOwner.getLoyaltyType();
        for (LoyaltyProgram lp : loyaltyPrograms){
            if (lp.getLoyaltyType().equals(loyaltyType)) {increaseRevenue = lp.getIncrease();}
        }

        if (selectedGraph.equalsIgnoreCase("Occupancy")){
            if (selectedPeriod.equalsIgnoreCase("Weekly")){
                start = Validate.getCurrentWeekMonday();
                end = Validate.getCurrentWeekSunday();
                data = this.populateBarChart(cottageOwner, start, end, selectedGraph);
            } else if (selectedPeriod.equalsIgnoreCase("Monthly")) {
                //monthly
                start = Validate.getSelectedMonthStart(selectedMonth);
                end = Validate.getSelectedMonthEnd(selectedMonth);
                data = this.populateBarChart(cottageOwner, start, end, selectedGraph);
            } else {
                //yearly
                start = Validate.getStartOfYear();
                end = Validate.getEndOfYear();
                data = this.populateBarChart(cottageOwner, start, end, selectedGraph);
            }
        }
        else
        {
            //revenue
            if (selectedPeriod.equalsIgnoreCase("Weekly")){
                start = Validate.getCurrentWeekMonday();
                end = Validate.getCurrentWeekSunday();
                data = this.populateBarChartRevenue(cottageOwner, start, end, selectedGraph, increaseRevenue);
            } else if (selectedPeriod.equalsIgnoreCase("Monthly")) {
                //monthly
                start = Validate.getSelectedMonthStart(selectedMonth);
                end = Validate.getSelectedMonthEnd(selectedMonth);
                data = this.populateBarChartRevenue(cottageOwner, start, end, selectedGraph, increaseRevenue);
            } else {
                //yearly
                start = Validate.getStartOfYear();
                end = Validate.getEndOfYear();
                data = this.populateBarChartRevenue(cottageOwner, start, end, selectedGraph, increaseRevenue);
            }
        }

        return data;
    }
    
}
