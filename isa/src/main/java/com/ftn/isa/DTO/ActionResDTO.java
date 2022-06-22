package com.ftn.isa.DTO;

import com.ftn.isa.helpers.Validate;

import java.time.LocalDateTime;

public class ActionResDTO {

    private Long rentalId;

    private LocalDateTime startTime;

    private LocalDateTime endTime;

    private Double price;

    private String actionServices;

    public ActionResDTO() {

    }

    public ActionResDTO(LocalDateTime startTime, LocalDateTime endTime, Double price, String actionServices, Long cottageId) {
        this.startTime = startTime;
        this.endTime = endTime;
        this.price = price;
        this.actionServices = actionServices;
        this.rentalId = cottageId;
    }

    public boolean arePropsValidAdding() {
        return price > 0 && Validate.getTodaysDate().isBefore(startTime)
                && startTime.isBefore(endTime);
    }

    public Long getRentalId() {
        return rentalId;
    }

    public void setRentalId(Long rentalId) {
        this.rentalId = rentalId;
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

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public String getActionServices() {
        return actionServices;
    }

    public void setActionServices(String actionServices) {
        this.actionServices = actionServices;
    }

}
