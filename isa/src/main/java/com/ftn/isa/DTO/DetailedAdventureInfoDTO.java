package com.ftn.isa.DTO;

import com.ftn.isa.model.Adventure;


public class DetailedAdventureInfoDTO extends DetailedEntityInfoDTO {
    private final String biography;
    private final String fishingEquipment;

    public DetailedAdventureInfoDTO(Adventure adventure) {
        super(adventure);
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
