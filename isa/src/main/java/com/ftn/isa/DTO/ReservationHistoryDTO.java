package com.ftn.isa.DTO;

import com.ftn.isa.model.RentalService;
import com.ftn.isa.model.Reservation;

import java.time.LocalDateTime;

public class ReservationHistoryDTO {
    private Long id;
    private Long rentalId;
    private String name;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private double price;

    public ReservationHistoryDTO(Reservation res, RentalService rental) {
        this.id = res.getId();
        this.rentalId = rental.getId();
        this.name = rental.getName();
        this.startTime = res.getStartTime();
        this.endTime = res.getEndTime();
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

    public LocalDateTime getStartTime() {
        return startTime;
    }

    public void setStartTime(LocalDateTime startTime) {
        this.startTime = startTime;
    }

    public LocalDateTime getEndTime() {
        return endTime;
    }

    public void setEndTime(LocalDateTime endTime) {
        this.endTime = endTime;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }
}
