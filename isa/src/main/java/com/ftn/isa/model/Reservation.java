package com.ftn.isa.model;


import com.ftn.isa.DTO.ReservingInfoDTO;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Entity
public class Reservation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "start_time", nullable = false)
    private LocalDate startTime;

    @Column(name = "end_time", nullable = false)
    private LocalDate endTime;

    @Column(name = "is_action", nullable = false)
    private boolean isAction;

    @Column(name = "price", nullable = false)
    private Double price;

    @Column(name = "is_reserved", nullable = false)
    private boolean isReserved;

    @OneToOne
    @JoinColumn(name="client_id")
    private Client client;

    @OneToOne
    @JoinColumn(name="rental_id")
    private RentalService rental;


    public Reservation() {}

    public Reservation(ReservingInfoDTO reservingData, Double price, RentalService rental, Client client) {
        DateTimeFormatter format = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        this.startTime = LocalDate.parse(reservingData.getStartDate(), format);
        this.endTime = LocalDate.parse(reservingData.getEndDate(), format);
        this.price = price;
        this.isAction = false;
        this.isReserved = true;
        this.rental = rental;
        this.client = client;
    }

    public boolean periodsAreOverlapping(LocalDate startDate, LocalDate endDate) {
        return startDate.isAfter(this.startTime) && startDate.isBefore(this.endTime) ||
                    endDate.isAfter(this.startTime) && endDate.isBefore(this.endTime) ||
                    this.startTime.isAfter(startDate) && this.startTime.isBefore(endDate) ||
                    this.endTime.isAfter(startDate) && this.endTime.isBefore(endDate) ||
                    startDate.isEqual(this.startTime) || endDate.isEqual(this.endTime);
    }

    public LocalDate getStartTime() {
        return startTime;
    }

    public void setStartTime(LocalDate startTime) {
        this.startTime = startTime;
    }

    public LocalDate getEndTime() {
        return endTime;
    }

    public void setEndTime(LocalDate endTime) {
        this.endTime = endTime;
    }

    public boolean isAction() {
        return isAction;
    }

    public void setAction(boolean action) {
        isAction = action;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public boolean isReserved() {
        return isReserved;
    }

    public void setReserved(boolean reserved) {
        isReserved = reserved;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Client getClient() {
        return client;
    }

    public void setClient(Client client) {
        this.client = client;
    }

    public RentalService getRental() {
        return rental;
    }

    public void setRental(RentalService rental) {
        this.rental = rental;
    }
}
