package com.ftn.isa.model;


import javax.persistence.*;

@Entity
public class Boat extends RentalService{


    @Column(name = "type", nullable = false)
    private String type;

    @Column(name = "boat_length", nullable = false)
    private String boatLength;

    @Column(name = "engine_number", nullable = false)
    private String engineNumber;

    @Column(name = "engine_power", nullable = false)
    private String enginePower;

    @Column(name = "max_speed", nullable = false)
    private String maxSpeed;

    @Column(name = "navigation_equipment", nullable = false)
    private String navigationEquipment;

    @Column(name = "fishing_equipment", nullable = false)
    private String fishingEquipment;

    @Column(name = "cancellation_conditions", nullable = false)
    private Double cancellationConditions;

    public Boat() {
    }

    public Boat(String type, String boatLength, String engineNumber, String enginePower, String maxSpeed, String navigationEquipment, String fishingEquipment, Double cancellationConditions) {
        this.type = type;
        this.boatLength = boatLength;
        this.engineNumber = engineNumber;
        this.enginePower = enginePower;
        this.maxSpeed = maxSpeed;
        this.navigationEquipment = navigationEquipment;
        this.fishingEquipment = fishingEquipment;
        this.cancellationConditions = cancellationConditions;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getBoatLength() {
        return boatLength;
    }

    public void setBoatLength(String boatLength) {
        this.boatLength = boatLength;
    }

    public String getEngineNumber() {
        return engineNumber;
    }

    public void setEngineNumber(String engineNumber) {
        this.engineNumber = engineNumber;
    }

    public String getEnginePower() {
        return enginePower;
    }

    public void setEnginePower(String enginePower) {
        this.enginePower = enginePower;
    }

    public String getMaxSpeed() {
        return maxSpeed;
    }

    public void setMaxSpeed(String maxSpeed) {
        this.maxSpeed = maxSpeed;
    }

    public String getNavigationEquipment() {
        return navigationEquipment;
    }

    public void setNavigationEquipment(String navigationEquipment) {
        this.navigationEquipment = navigationEquipment;
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
