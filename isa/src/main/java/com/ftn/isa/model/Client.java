package com.ftn.isa.model;


import com.ftn.isa.DTO.ClientProfileDTO;

import javax.persistence.*;
import java.util.Set;

@Entity
public class Client extends User {
    @Column(name = "no_penalties", nullable = false)
    private int noPenalties;

    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name = "reservation_id", referencedColumnName = "id")
    private Set<Reservation> reservations;

    public Client() {
    }

    public void updatePersonalInfo(ClientProfileDTO data) {
        this.setName(data.getName());
        this.setSurname(data.getSurname());
        this.setPassword(data.getPassword());
        Address address = this.getAddress();
        address.setPlaceName(data.getCity());
        address.setZipCode(data.getZipcode());
        address.setStreet(data.getStreet());
        this.getProfilePicture().setPhotoPath(data.getProfilePicture());
        this.setPhoneNumber(data.getPhoneNumber());
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
