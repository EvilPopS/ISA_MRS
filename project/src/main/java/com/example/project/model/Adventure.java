package com.example.project.model;


import javax.persistence.Column;
import javax.persistence.Entity;

@Entity
public class Adventure extends RentalService {


    @Column(name = "biography", nullable = false)
    private String biography;

    @Column(name = "fishingEquipment", nullable = false)
    private String fishingEquipment;

    @Column(name = "cancellationConditions", nullable = false)
    private Double cancellationConditions;

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
