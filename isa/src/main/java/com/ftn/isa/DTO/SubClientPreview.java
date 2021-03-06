package com.ftn.isa.DTO;

import com.ftn.isa.model.Subscription;
import com.ftn.isa.model.User;

public class SubClientPreview {
    private Long ownerId;
    private String name;
    private String surname;
    private String phoneNum;
    private String rentType;
    private String accPic;


    public SubClientPreview() {}

    public SubClientPreview(Subscription sub) {
        User owner = sub.getOwner();
        this.ownerId = owner.getId();
        this.name = owner.getName();
        this.surname = owner.getSurname();
        this.phoneNum = owner.getPhoneNumber();
        this.accPic = owner.getProfilePicture().getPhotoPath();
        switch (owner.getRole().getName()) {
            case "ROLE_COTTAGE_OWNER":
                this.rentType = "COTTAGE";
                break;
            case "ROLE_BOAT_OWNER":
                this.rentType = "BOAT";
                break;
            case "ROLE_INSTRUCTOR":
                this.rentType = "ADVENTURE";
        }
    }

    public Long getOwnerId() {
        return ownerId;
    }

    public void setOwnerId(Long ownerId) {
        this.ownerId = ownerId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public String getPhoneNum() {
        return phoneNum;
    }

    public void setPhoneNum(String phoneNum) {
        this.phoneNum = phoneNum;
    }

    public String getRentType() {
        return rentType;
    }

    public void setRentType(String rentType) {
        this.rentType = rentType;
    }

    public String getAccPic() {
        return accPic;
    }

    public void setAccPic(String accPic) {
        this.accPic = accPic;
    }
}
