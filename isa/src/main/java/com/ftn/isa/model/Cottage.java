package com.ftn.isa.model;


import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
public class Cottage extends RentalService{
    @Column(name = "additional_services", nullable = false)
    private String additionalServices;

    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name = "room_id", referencedColumnName = "id")
    private Set<Room> rooms = new HashSet<>();

    public Cottage(String name, String description, Set<Photo> photos, int capacity, String rules, boolean isDeleted, Address address, Double averageRate, int noRatings, RentalType rentalType, Double price) {
        super(name, description, photos, capacity, rules, isDeleted, address, averageRate, noRatings, rentalType, price);
    }

    public Cottage() {
        super();
    }

    public String getAdditionalServices() {
        return additionalServices;
    }

    public void setAdditionalServices(String additionalServices) {
        this.additionalServices = additionalServices;
    }

    public Set<Room> getRooms() {
        return rooms;
    }

    public void setRooms(Set<Room> rooms) {
        this.rooms = rooms;
    }
}
