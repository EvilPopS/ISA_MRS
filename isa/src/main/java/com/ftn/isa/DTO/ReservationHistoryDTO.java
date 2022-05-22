package com.ftn.isa.DTO;

import com.ftn.isa.model.RentalService;
import com.ftn.isa.model.Reservation;

import java.time.LocalDateTime;

public class ReservationHistoryDTO {
    private Long id;
    private Long rentalId;
    private String name;
    private LocalDateTime startDate;
    private LocalDateTime endDate;
    private double price;

    public ReservationHistoryDTO(Reservation res, RentalService rental) {
        this.id = res.getId();
        this.rentalId = rental.getId();
        this.name = rental.getName();
        this.startDate = res.getStartTime();
        this.endDate = res.getEndTime();
        this.price = res.getPrice();
    }

    public ReservationHistoryDTO(Reservation res) {
        this.id = res.getId();
        this.rentalId = null;
        this.name = null;
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

    public LocalDateTime getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDateTime startDate) {
        this.startDate = startDate;
    }

    public LocalDateTime getEndDate() {
        return endDate;
    }

    public void setEndDate(LocalDateTime endDate) {
        this.endDate = endDate;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }
}
