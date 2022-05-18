package com.ftn.isa.DTO;

import java.time.LocalDate;
import java.time.LocalDateTime;

public class ReservationDTO {

    private Long reservationId;
    private Long rentalId;
    private String clientEmail;
    private String clientFullName;
    private String clientProfilePhoto;
    private String rentalName;
    private LocalDate startTime;
    private LocalDate endTime;
    private double price;
    private boolean isAction;
    private boolean isReserved;

    public ReservationDTO() {

    }

    public ReservationDTO(Long reservationId, Long rentalId, String clientEmail,
                          String rentalName, LocalDate startTime,
                          LocalDate endTime, double price, boolean isAction, boolean isReserved, String clientProfilePhoto, String clientFullName) {
        this.reservationId = reservationId;
        this.rentalId = rentalId;
        this.clientEmail = clientEmail;
        this.rentalName = rentalName;
        this.startTime = startTime;
        this.endTime = endTime;
        this.price = price;
        this.isAction = isAction;
        this.isReserved = isReserved;
        this.clientProfilePhoto = clientProfilePhoto;
        this.clientFullName = clientFullName;
    }

    public ReservationDTO(Long reservationId, Long rentalId,
                          String rentalName, LocalDate startTime,
                          LocalDate endTime, double price,
                          boolean isAction, boolean isReserved) {
        this.reservationId = reservationId;
        this.rentalId = rentalId;
        this.rentalName = rentalName;
        this.startTime = startTime;
        this.endTime = endTime;
        this.price = price;
        this.isAction = isAction;
        this.isReserved = isReserved;
    }

    public String getClientFullName() {
        return clientFullName;
    }

    public void setClientFullName(String clientFullName) {
        this.clientFullName = clientFullName;
    }

    public Long getReservationId() {
        return reservationId;
    }

    public void setReservationId(Long reservationId) {
        this.reservationId = reservationId;
    }

    public Long getRentalId() {
        return rentalId;
    }

    public void setRetnalId(Long cottageId) {
        this.rentalId = cottageId;
    }

    public String getClientEmail() {
        return clientEmail;
    }

    public void setClientEmail(String clientEmail) {
        this.clientEmail = clientEmail;
    }

    public String getRentalName() {
        return rentalName;
    }

    public void setRentalName(String cottageName) {
        this.rentalName = cottageName;
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

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public boolean isAction() {
        return isAction;
    }

    public void setAction(boolean action) {
        isAction = action;
    }

    public boolean isReserved() {
        return isReserved;
    }

    public void setReserved(boolean reserved) {
        isReserved = reserved;
    }

    public void setRentalId(Long rentalId) {
        this.rentalId = rentalId;
    }

    public String getClientProfilePhoto() {
        return clientProfilePhoto;
    }

    public void setClientProfilePhoto(String clientProfilePhoto) {
        this.clientProfilePhoto = clientProfilePhoto;
    }
}
