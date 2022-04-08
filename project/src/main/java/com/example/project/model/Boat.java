package com.example.project.model;


import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Inheritance;

@Entity
public class Boat extends RentalService{


    @Column(name = "type", nullable = false)
    private String type;

    @Column(name = "length", nullable = false)
    private String length;

    @Column(name = "engineNumber", nullable = false)
    private String engineNumber;

    @Column(name = "enginePower", nullable = false)
    private String enginePower;

    @Column(name = "maxSpeed", nullable = false)
    private String maxSpeed;

    @Column(name = "navigationEquipment", nullable = false)
    private String navigationEquipment;

    @Column(name = "fishingEquipment", nullable = false)
    private String fishingEquipment;

    @Column(name = "cancellationConditions", nullable = false)
    private Double cancellationConditions;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getLength() {
        return length;
    }

    public void setLength(String length) {
        this.length = length;
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
