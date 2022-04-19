package com.ftn.isa.DTO;

import com.ftn.isa.model.RentalService;

public class RentalSearchResDTO {
    private String name;
    private String type;
    private double rate;
    private double price;
    private String city;
    private String street;

    public RentalSearchResDTO() {}

    public RentalSearchResDTO(RentalService rental) {
        this.name = rental.getName();
        switch (rental.getRentalType()) {
            case COTTAGE:
                this.type = "Cottage";
                break;
            case BOAT:
                this.type = "Boat";
                break;
            default:
                this.type = "Adventure";
        }
        this.rate = rental.getAverageRate();
        this.price = rental.getPrice();
        this.city = rental.getAddress().getPlaceName();
        this.street = rental.getAddress().getStreet();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public double getRate() {
        return rate;
    }

    public void setRate(double rate) {
        this.rate = rate;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
    }
}
