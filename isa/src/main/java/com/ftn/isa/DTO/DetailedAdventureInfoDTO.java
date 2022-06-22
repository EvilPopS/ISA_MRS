package com.ftn.isa.DTO;

import com.ftn.isa.model.Adventure;
import com.ftn.isa.model.User;


public class DetailedAdventureInfoDTO extends DetailedEntityInfoDTO {
    private final String biography;
    private final String fishingEquipment;

    public DetailedAdventureInfoDTO(Adventure adventure, User user) {
        super(adventure, user);
        this.biography = adventure.getBiography();
        this.fishingEquipment = adventure.getFishingEquipment();
    }

    public String getBiography() {
        return biography;
    }

    public String getFishingEquipment() {
        return fishingEquipment;
    }
}
