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
