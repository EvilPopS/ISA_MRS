package com.ftn.isa.DTO;

import com.ftn.isa.helpers.Validate;
import com.ftn.isa.model.Client;

public class BasicClientDTO {

    private String email;
    private String name;
    private String surname;
    private String city;
    private String zipcode;
    private String profilePicture;
    private String loyaltyStatus;

    public BasicClientDTO(Client client) {
        this.email = client.getEmail();
        this.name = client.getName();
        this.surname = client.getSurname();
        this.city = client.getAddress().getPlaceName();
        this.zipcode = client.getAddress().getZipCode();
        this.profilePicture = client.getProfilePicture().getPhotoPath();
        switch (client.getLoyaltyType()) {
            case REGULAR:
                this.loyaltyStatus = "REGULAR";
                break;
            case SILVER:
                this.loyaltyStatus = "SILVER";
                break;
            default:
                this.loyaltyStatus = "GOLD";
        }
    }

    public BasicClientDTO(String email, String name, String surname, String city,
                            String zipcode, String profilePicture, String loyaltyStatus) {
        this.email = email;
        this.name = name;
        this.surname = surname;
        this.city = city;
        this.zipcode = zipcode;
        this.profilePicture = profilePicture;
        this.loyaltyStatus = loyaltyStatus;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
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

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getZipcode() {
        return zipcode;
    }

    public void setZipcode(String zipcode) {
        this.zipcode = zipcode;
    }

    public String getProfilePicture() {
        return profilePicture;
    }

    public void setProfilePicture(String profilePicture) {
        this.profilePicture = profilePicture;
    }

    public String getLoyaltyStatus() {
        return loyaltyStatus;
    }

    public void setLoyaltyStatus(String loyaltyStatus) {
        this.loyaltyStatus = loyaltyStatus;
    }
}
