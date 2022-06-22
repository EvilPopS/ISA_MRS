package com.ftn.isa.DTO;

import com.ftn.isa.model.RentalService;
import com.ftn.isa.model.RentalType;
import com.ftn.isa.model.Reservation;

import java.time.LocalDateTime;

public class ReservationDisplayDTO {
    private Long id;
    private Long rentalId;
    private String rentalType;
    private String rentalPic;
    private String name;
    private LocalDateTime startDate;
    private LocalDateTime endDate;
    private double price;
    private String additionalServices;
    private boolean isReserved;

    public ReservationDisplayDTO(Reservation res, RentalService rental) {
        this.id = res.getId();
        this.rentalId = rental.getId();
        this.rentalPic = rental.getPhotos().iterator().next().getPhotoPath();
        this.name = rental.getName();
        this.startDate = res.getStartTime();
        this.endDate = res.getEndTime();
        this.price = res.getPrice();
        this.additionalServices = res.getActionServices();
        switch (rental.getRentalType()) {
            case ADVENTURE:
                this.rentalType = "Adventure";
                break;
            case BOAT:
                this.rentalType = "Boat";
                break;
            default:
                this.rentalType = "Cottage";
        }
    }

    public ReservationDisplayDTO(Reservation res) {
        this.id = res.getId();
        this.rentalId = null;
        this.name = null;
        this.rentalPic = null;
        this.startDate = res.getStartTime();
        this.endDate = res.getEndTime();
        this.price = res.getPrice();
        this.additionalServices = res.getActionServices();
        this.rentalType = null;
        this.isReserved = res.isReserved();
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

    public Long getRentalId() {
        return rentalId;
    }

    public void setRentalId(Long rentalId) {
        this.rentalId = rentalId;
    }

    public String getRentalType() {
        return rentalType;
    }

    public void setRentalType(String rentalType) {
        this.rentalType = rentalType;
    }

    public String getRentalPic() {
        return rentalPic;
    }

    public void setRentalPic(String rentalPic) {
        this.rentalPic = rentalPic;
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

    public String getAdditionalServices() {
        return additionalServices;
    }

    public void setAdditionalServices(String additionalServices) {
        this.additionalServices = additionalServices;
    }
}
