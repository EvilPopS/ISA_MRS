package com.ftn.isa.model;


import com.ftn.isa.DTO.FishingInstructorDTO;

import javax.persistence.*;
import java.util.Set;

@Entity
public class FishingInstructor extends User{


    @OneToMany
    @JoinColumn(name = "adventure_id", referencedColumnName = "id")
    private Set<Adventure> adventures;

    public Set<Adventure> getAdventures() {
        return adventures;
    }

    public void setAdventures(Set<Adventure> adventures) {
        this.adventures = adventures;
    }

    public void updatePersonalInfo(FishingInstructorDTO data) {
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

}
