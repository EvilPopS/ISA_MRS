package com.ftn.isa.DTO;

import com.ftn.isa.model.Cottage;
import com.ftn.isa.model.User;

public class DetailedCottageInfoDTO extends DetailedEntityInfoDTO{
    private final String additionalServices;
    private final int noRooms;


    public DetailedCottageInfoDTO(Cottage cottage, User user) {
        super(cottage, user);
        this.additionalServices = cottage.getAdditionalServices();
        this.noRooms = cottage.getNoRooms();
    }

    public String getAdditionalServices() {
        return additionalServices;
    }

    public int getNoRooms() {
        return noRooms;
    }
}
