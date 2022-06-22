package com.ftn.isa.DTO;

import com.ftn.isa.model.Address;
import com.ftn.isa.model.Photo;
import com.ftn.isa.model.RentalService;
import java.util.ArrayList;
import java.util.List;

public class BasicEntityInfoDTO {
    private String name;
    private String description;
    private Address address;
    private double price;
    private double rate;
    private List<String> photos;


    public BasicEntityInfoDTO(RentalService rental) {
        this.name = rental.getName();
        this.description = rental.getDescription();
        this.address = rental.getAddress();
        this.price = rental.getPrice();
        this.rate = rental.getAverageRate();
        this.photos = new ArrayList<>();
        for(Photo photo : rental.getPhotos())
            this.photos.add(photo.getPhotoPath());
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Address getAddress() {
        return address;
    }

    public void setAddress(Address address) {
        this.address = address;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public double getRate() {
        return rate;
    }

    public void setRate(double rate) {
        this.rate = rate;
    }

    public List<String> getPhotos() {
        return photos;
    }

    public void setPhotos(List<String> photos) {
        this.photos = photos;
    }
}
