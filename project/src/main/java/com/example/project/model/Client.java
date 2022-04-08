package com.example.project.model;


import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import java.util.Set;

@Entity
public class Client extends User{


    @Column(name = "noPenalties", nullable = false)
    private int noPenalties;

    @OneToMany
    @JoinColumn(name = "reservation_id", referencedColumnName = "id")
    private Set<Reservation> reservations;

    public int getNoPenalties() {
        return noPenalties;
    }

    public void setNoPenalties(int noPenalties) {
        this.noPenalties = noPenalties;
    }

    public Set<Reservation> getReservations() {
        return reservations;
    }

    public void setReservations(Set<Reservation> reservations) {
        this.reservations = reservations;
    }
}
