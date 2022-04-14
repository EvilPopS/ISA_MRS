package com.ftn.isa.model;

import com.ftn.isa.DTO.ClientProfileDTO;
import com.ftn.isa.DTO.CottageOwnerDTO;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
public class CottageOwner extends User{


    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name = "cottage_id", referencedColumnName = "id")
    private Set<Cottage> cottages = new HashSet<>();

    public void updatePersonalInfo(CottageOwnerDTO data) {
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

    public Set<Cottage> getCottages() {
        return cottages;
    }

    public void setCottages(Set<Cottage> cottages) {
        this.cottages = cottages;
    }
}