package com.ftn.isa.DTO;

import com.ftn.isa.model.RentalService;
import com.ftn.isa.model.Reservation;

import java.time.LocalDate;
import java.time.LocalDateTime;

public class ReservationHistoryDTO {
    private Long id;
    private Long rentalId;
    private String name;
    private LocalDate startDate;
    private LocalDate endDate;
    private double price;

    public ReservationHistoryDTO(Reservation res, RentalService rental) {
        this.id = res.getId();
        this.rentalId = rental.getId();
        this.name = rental.getName();
        this.startDate = res.getStartTime();
        this.endDate = res.getEndTime();
        this.price = res.getPrice();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getRentalId() {
        return rentalId;
    }

    public void setRentalId(Long rentalId) {
        this.rentalId = rentalId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

    public LocalDate getEndDate() {
        return endDate;
    }

    public void setEndDate(LocalDate endDate) {
        this.endDate = endDate;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }
}
