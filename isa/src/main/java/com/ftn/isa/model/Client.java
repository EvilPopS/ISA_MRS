package com.ftn.isa.model;


import com.ftn.isa.DTO.ClientProfileDTO;
import com.ftn.isa.DTO.UserRegDTO;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
public class Client extends User {
    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinColumn(name = "client_id", referencedColumnName = "id")
    private Set<Reservation> reservations;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinColumn(name = "client_id", referencedColumnName = "id")
    private List<Subscription> subscriptions;

    @Column(name = "num_of_penalties")
    private int numOfPenalties;

    public Client() {
    }

    public Client(UserRegDTO data) {
            this.setName(data.getName());
            this.setSurname(data.getSurname());
            this.setEmail(data.getEmail());
            this.setAddress(new Address(data.getCountry(), data.getCity(), data.getAddress()));
            this.setPhoneNumber(data.getPhoneNumber());
            this.setProfilePicture(new Photo(data.getProfilePicture()));
            this.setPassword(data.getPassword());
            this.setDeleted(false);
            this.setActive(false);
            this.setRole(new Role("ROLE_CLIENT"));
            this.setLoyaltyPoints(0);
            this.setLoyaltyType(LoyaltyType.REGULAR);
            this.numOfPenalties = 0;
            this.reservations = new HashSet<>();
            this.subscriptions = new ArrayList<>();
    }

    public void updatePersonalInfo(ClientProfileDTO data) {
        this.setName(data.getName());
        this.setSurname(data.getSurname());
        if (!data.getPassword().equals(""))
            this.setPassword(data.getPassword());
        Address address = this.getAddress();
        address.setPlaceName(data.getCity());
        address.setCountry(data.getCountry());
        address.setStreet(data.getStreet());
        this.getProfilePicture().setPhotoPath(data.getProfilePicture());
        this.setPhoneNumber(data.getPhoneNumber());
    }

    public void incLoyaltyPoints(int points) {
        this.setLoyaltyPoints(this.getLoyaltyPoints() + points);
    }

    public Set<Reservation> getReservations() {
        return reservations;
    }

    public void setReservations(Set<Reservation> reservations) {
        this.reservations = reservations;
    }

    public List<Subscription> getSubscriptions() {
        return subscriptions;
    }

    public void setSubscriptions(List<Subscription> subscriptions) {
        this.subscriptions = subscriptions;
    }

    public int getNumOfPenalties() {
        return numOfPenalties;
    }

    public void setNumOfPenalties(int numOfPenalties) {
        this.numOfPenalties = numOfPenalties;
    }
}
