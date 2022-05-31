package com.ftn.isa.DTO;

import com.ftn.isa.model.RentalService;

public class OwnersSearchResDTO {
    private Long id;
    private String name;
    private String type;
    private double capacity;
    private double price;
    private String city;
    private String street;

    public OwnersSearchResDTO() {}

    public OwnersSearchResDTO(RentalService rental) {
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
        this.id = rental.getId();
        this.capacity = rental.getCapacity();
        this.price = rental.getPrice();
        this.city = rental.getAddress().getPlaceName();
        this.street = rental.getAddress().getStreet();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public double getCapacity() {
        return capacity;
    }

    public void setCapacity(double capacity) {
        this.capacity = capacity;
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
