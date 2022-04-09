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
