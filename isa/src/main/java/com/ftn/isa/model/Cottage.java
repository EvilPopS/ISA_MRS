package com.ftn.isa.model;


import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
public class Cottage extends RentalService{
    @Column(name = "additional_services", nullable = false)
    private String additionalServices;

    @Column(name = "no_rooms", nullable = false)
    private int noRooms;

    public Cottage(String name, String description, int capacity, String rules, boolean isDeleted, Address address, Double averageRate,
                         int noRatings, RentalType rentalType, Double price, String additionalServices, int noRooms) {
        super(name, description, new HashSet<>(), capacity, rules, isDeleted, address, averageRate, noRatings, rentalType,price);
        this.noRooms = noRooms;
        this.additionalServices = additionalServices;
    }

    public Cottage(){
    }

    public String getAdditionalServices() {
        return additionalServices;
    }

    public void setAdditionalServices(String additionalServices) {
        this.additionalServices = additionalServices;
    }

    public int getNoRooms() {
        return noRooms;
    }

    public void setNoRooms(int noRooms) {
        this.noRooms = noRooms;
    }
}
