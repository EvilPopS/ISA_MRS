package com.ftn.isa.DTO;

import java.time.LocalDateTime;

public class ReservationDTO {

    private Long reservationId;
    private Long rentalId;
    private String clientEmail;
    private String clientFullName;
    private String clientProfilePhoto;
    private String rentalName;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private double price;
    private String actionServices = "";
    private boolean isAction;
    private boolean isReserved;

    public ReservationDTO() {

    }


    public ReservationDTO(Long reservationId, Long rentalId, String clientEmail,
                          String rentalName, LocalDateTime startTime,
                          LocalDateTime endTime, double price, boolean isAction, boolean isReserved, String clientProfilePhoto, String clientFullName, String actionServices) {
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
        this.actionServices = actionServices;
    }

    public ReservationDTO(Long reservationId, Long rentalId,
                          String rentalName, LocalDateTime startTime,
                          LocalDateTime endTime, double price,
                          boolean isAction, boolean isReserved, String actionServices) {
        this.reservationId = reservationId;
        this.rentalId = rentalId;
        this.rentalName = rentalName;
        this.startTime = startTime;
        this.endTime = endTime;
        this.price = price;
        this.isAction = isAction;
        this.isReserved = isReserved;
        this.actionServices = actionServices;
    }

    public String getActionServices() {
        return actionServices;
    }

    public void setActionServices(String actionServices) {
        this.actionServices = actionServices;
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
