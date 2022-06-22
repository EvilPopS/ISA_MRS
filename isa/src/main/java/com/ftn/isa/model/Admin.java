package com.ftn.isa.model;

import com.ftn.isa.DTO.AdminDTO;
import com.ftn.isa.DTO.BoatOwnerDTO;

import javax.persistence.*;
import java.util.Set;

@Entity
public class Admin extends User{


    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name = "response_id", referencedColumnName = "id")
    private Set<Response> responses;

    public Admin() {
    }

    public void updatePersonalInfo(AdminDTO data) {
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

    public Admin(Set<Response> responses) {
        this.responses = responses;
    }

    public Set<Response> getResponses() {
        return responses;
    }

    public void setResponses(Set<Response> responses) {
        this.responses = responses;
    }
}
