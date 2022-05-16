package com.ftn.isa.model;

import com.ftn.isa.DTO.CottageOwnerDTO;
import com.ftn.isa.DTO.OwnerRegDTO;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
public class CottageOwner extends User{


    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinColumn(name = "cottage_owner_id", referencedColumnName = "id")
    private Set<Cottage> cottages = new HashSet<>();

    public CottageOwner() {

    }

    public CottageOwner(OwnerRegDTO data) {
        this.setName(data.getName());
        this.setSurname(data.getSurname());
        this.setEmail(data.getEmail());
        this.setAddress(new Address(data.getCountry(), data.getCity(), data.getAddress()));
        this.setPhoneNumber(data.getPhoneNumber());
        this.setProfilePicture(new Photo(data.getProfilePicture()));
        this.setPassword(data.getPassword());
        this.setDeleted(false);
        this.setActive(false);
        this.setRole(new Role("ROLE_COTTAGE_OWNER"));
        this.setLoyaltyPoints(0);
        this.setLoyaltyType(LoyaltyType.REGULAR);
        this.cottages = new HashSet<>();
    }

    public void updatePersonalInfo(CottageOwnerDTO data) {
        this.setName(data.getName());
        this.setSurname(data.getSurname());
        this.setPassword(data.getPassword());
        Address address = this.getAddress();
        address.setPlaceName(data.getCity());
        address.setCountry(data.getCountry());
        address.setStreet(data.getStreet());
        this.getProfilePicture().setPhotoPath(data.getProfilePicture());
        this.setPhoneNumber(data.getPhoneNumber());
    }

    public Set<Cottage> getCottages() {
        return cottages;
    }

    public void setCottages(Set<Cottage> cottages) {
        this.cottages = cottages;
    }
}