package com.example.project.model;


import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import java.util.Set;

@Entity
public class BoatOwner extends User {

    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name = "boat_id", referencedColumnName = "id")
    private Set<Boat> boats;

    public Set<Boat> getBoats() {
        return boats;
    }

    public void setBoats(Set<Boat> boats) {
        this.boats = boats;
    }
}
