package com.ftn.isa.model;


import com.ftn.isa.DTO.AdventureDTO;
import com.ftn.isa.DTO.FishingInstructorDTO;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
public class FishingInstructor extends User{


    @OneToMany(cascade = CascadeType.ALL, fetch= FetchType.EAGER)
    @JoinColumn(name="instructor_id", referencedColumnName = "id")
    private Set<Adventure> adventures = new HashSet<>();

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

    public Adventure findAdventureById(Long id) {
        for (Adventure a : this.adventures) {
            if (a.getId().equals(id)) {
                return a;

            }
        }
        return null;
    }

    public void updateAdventure(AdventureDTO adventureData) {
        Adventure adventureToUpdate = findAdventureById(adventureData.getId());
        if (adventureToUpdate != null) {
            adventureToUpdate.setName(adventureData.getName());
            Set<Photo> photos = new HashSet<>();
            for (String photo : adventureData.getPhotos()) {
                photos.add(new Photo(photo));
            }
            adventureToUpdate.setPhotos(photos);
            adventureToUpdate.setDescription(adventureData.getDescription());
            adventureToUpdate.setBiography(adventureData.getBiography());
            adventureToUpdate.setCapacity(adventureData.getCapacity());
            adventureToUpdate.setCancellationConditions(adventureData.getCancellationConditions());
            adventureToUpdate.setPrice(adventureData.getPrice());
            adventureToUpdate.setFishingEquipment(adventureData.getFishingEquipment());
            adventureToUpdate.setAddress(new Address(adventureData.getCity(), adventureData.getZipcode(), adventureData.getStreet()));
            adventureToUpdate.setRules(adventureData.getRules());
        }
    }
}
