package com.ftn.isa.DTO;

import com.ftn.isa.helpers.Validate;

import java.time.LocalDateTime;

public class RegularResDTO {

    private Long rentalId;

    private LocalDateTime startTime;

    private LocalDateTime endTime;

    private String clientEmail;

    private Double price;

    private RegularResDTO(){

    }

    public RegularResDTO(Long cottageId, LocalDateTime startTime, LocalDateTime endTime, String clientEmail, Double price) {
        this.rentalId = cottageId;
        this.startTime = startTime;
        this.endTime = endTime;
        this.clientEmail = clientEmail;
        this.price = price;
    }

    public boolean arePropsValidAdding() {
        return !clientEmail.equals("") && Validate.getTodaysDate().isBefore(startTime)
                && startTime.isBefore(endTime) && price > 0;
    }

    public boolean checkOnlyDate() {
        return Validate.getTodaysDate().isBefore(startTime)
                && startTime.isBefore(endTime);
    }

    public Long getRentalId() {
        return rentalId;
    }

    public void setRentalId(Long rentalId) {
        this.rentalId = rentalId;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
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

    public String getClientEmail() {
        return clientEmail;
    }

    public void setClientEmail(String clientEmail) {
        this.clientEmail = clientEmail;
    }
}
