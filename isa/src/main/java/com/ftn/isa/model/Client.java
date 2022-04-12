package com.ftn.isa.model;


import javax.persistence.*;
import java.util.Set;

@Entity
public class Client extends User {
    @Column(name = "no_penalties", nullable = false)
    private int noPenalties;

    @OneToMany
    @JoinColumn(name = "reservation_id", referencedColumnName = "id")
    private Set<Reservation> reservations;

    public Client() {
        super();
    }

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
