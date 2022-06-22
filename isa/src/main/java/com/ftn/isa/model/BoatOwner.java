package com.ftn.isa.model;


import com.ftn.isa.DTO.BoatOwnerDTO;
import com.ftn.isa.DTO.OwnerRegDTO;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
public class BoatOwner extends User {

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinColumn(name = "boat_owner_id", referencedColumnName = "id")
    private Set<Boat> boats = new HashSet<>();

    public void updatePersonalInfo(BoatOwnerDTO data) {
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

    public BoatOwner() {

    }

    public BoatOwner(OwnerRegDTO data) {
        this.setName(data.getName());
        this.setSurname(data.getSurname());
        this.setEmail(data.getEmail());
        this.setAddress(new Address(data.getCountry(), data.getCity(), data.getAddress()));
        this.setPhoneNumber(data.getPhoneNumber());
        this.setProfilePicture(new Photo(data.getProfilePicture()));
        this.setPassword(new BCryptPasswordEncoder().encode(data.getPassword()));
        this.setDeleted(false);
        this.setActive(false);
        this.setRole(new Role("ROLE_BOAT_OWNER"));
        this.setLoyaltyPoints(0);
        this.setLoyaltyType(LoyaltyType.REGULAR);
        this.boats = new HashSet<>();
    }

    public Set<Boat> getBoats() {
        return boats;
    }

    public void setBoats(Set<Boat> boats) {
        this.boats = boats;
    }
}
