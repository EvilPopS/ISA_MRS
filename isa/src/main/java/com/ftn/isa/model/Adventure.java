package com.ftn.isa.model;


import javax.persistence.*;

@Entity
public class Adventure extends RentalService {


    @Column(name = "biography", nullable = false)
    private String biography;

    @Column(name = "fishing_equipment", nullable = false)
    private String fishingEquipment;

    @Column(name = "cancellation_conditions", nullable = false)
    private Double cancellationConditions;

    public Adventure() {
    }

    public Adventure(String biography, String fishingEquipment, Double cancellationConditions) {
        this.biography = biography;
        this.fishingEquipment = fishingEquipment;
        this.cancellationConditions = cancellationConditions;
    }

    public String getBiography() {
        return biography;
    }

    public void setBiography(String biography) {
        this.biography = biography;
    }

    public String getFishingEquipment() {
        return fishingEquipment;
    }

    public void setFishingEquipment(String fishingEquipment) {
        this.fishingEquipment = fishingEquipment;
    }

    public Double getCancellationConditions() {
        return cancellationConditions;
    }

    public void setCancellationConditions(Double cancellationConditions) {
        this.cancellationConditions = cancellationConditions;
    }
}
