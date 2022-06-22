package com.ftn.isa.DTO;

import com.ftn.isa.model.Address;
import com.ftn.isa.model.User;

public class OwnerProfileViewDTO {
    private Long id;
    private String name;
    private String surname;
    private String profilePic;
    private String country;
    private String city;
    private String street;
    private String phoneNum;


    public OwnerProfileViewDTO() {}

    public OwnerProfileViewDTO(User usr) {
        this.id = usr.getId();
        this.name = usr.getName();
        this.surname = usr.getSurname();
        this.profilePic = usr.getProfilePicture().getPhotoPath();
        Address adr = usr.getAddress();
        this.country = adr.getCountry();
        this.city = adr.getPlaceName();
        this.street = adr.getStreet();
        this.phoneNum = usr.getPhoneNumber();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public String getProfilePic() {
        return profilePic;
    }

    public void setProfilePic(String profilePic) {
        this.profilePic = profilePic;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public String getPhoneNum() {
        return phoneNum;
    }

    public void setPhoneNum(String phoneNum) {
        this.phoneNum = phoneNum;
    }
}
