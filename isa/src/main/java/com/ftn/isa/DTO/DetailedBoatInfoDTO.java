package com.ftn.isa.DTO;

import com.ftn.isa.model.Boat;

public class DetailedBoatInfoDTO extends DetailedEntityInfoDTO{
    private final String boatType;
    private final String boatLength;
    private final String engineNumber;
    private final String enginePower;
    private final String maxSpeed;
    private final String navigationEquipment;
    private final String fishingEquipment;

    public DetailedBoatInfoDTO(Boat boat) {
        super(boat);
        this.boatType = boat.getType();
        this.boatLength = boat.getBoatLength();
        this.engineNumber = boat.getEngineNumber();
        this.enginePower = boat.getEnginePower();
        this.maxSpeed = boat.getMaxSpeed();
        this.navigationEquipment = boat.getNavigationEquipment();
        this.fishingEquipment = boat.getFishingEquipment();
    }

    public String getBoatType() {
        return boatType;
    }

    public String getBoatLength() {
        return boatLength;
    }

    public String getEngineNumber() {
        return engineNumber;
    }

    public String getEnginePower() {
        return enginePower;
    }

    public String getMaxSpeed() {
        return maxSpeed;
    }

    public String getNavigationEquipment() {
        return navigationEquipment;
    }

    public String getFishingEquipment() {
        return fishingEquipment;
    }
}
