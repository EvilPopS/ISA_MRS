package com.ftn.isa.DTO;

import com.ftn.isa.model.Address;
import com.ftn.isa.model.Photo;
import com.ftn.isa.model.RentalService;
import java.util.HashSet;
import java.util.Set;

public abstract class DetailedEntityInfoDTO {
    private final String name;
    private final String description;
    private final Set<String> photos;
    private final int capacity;
    private final String rules;
    private final Address address;
    private final Double rate;
    private final Double price;

    public DetailedEntityInfoDTO(RentalService rental) {
        this.name = rental.getName();
        this.description = rental.getDescription();
        this.photos = new HashSet<>();
        for (Photo photo : rental.getPhotos())
            this.photos.add(photo.getPhotoPath());
        this.capacity = rental.getCapacity();
        this.rules = rental.getRules();
        this.address = rental.getAddress();
        this.rate = rental.getAverageRate();
        this.price = rental.getPrice();
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public Set<String> getPhotos() {
        return photos;
    }

    public int getCapacity() {
        return capacity;
    }

    public String getRules() {
        return rules;
    }

    public Address getAddress() {
        return address;
    }

    public Double getRate() {
        return rate;
    }

    public Double getPrice() {
        return price;
    }
}
