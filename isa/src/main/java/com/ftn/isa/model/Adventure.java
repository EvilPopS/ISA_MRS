package com.ftn.isa.model;


import javax.persistence.*;
import java.util.Set;

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

    public Adventure(String name, String description, Set<Photo> photos, int capacity, String rules, boolean isDeleted, Address address, Double averageRate, int noRatings, RentalType rentalType, Double price, String biography, String fishingEquipment, Double cancellationConditions) {
        super(name, description, photos, capacity, rules, isDeleted, address, averageRate, noRatings, rentalType, price);
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
