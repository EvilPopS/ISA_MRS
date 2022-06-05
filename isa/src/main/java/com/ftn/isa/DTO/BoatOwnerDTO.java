package com.ftn.isa.DTO;

import com.ftn.isa.helpers.Validate;
import com.ftn.isa.model.BoatOwner;
import com.ftn.isa.model.CottageOwner;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class BoatOwnerDTO {
    private String email;
    private String password;
    private String name;
    private String surname;
    private String city;
    private String country;
    private String street;
    private String phoneNumber;
    private String profilePicture;
    private String userType;
    private String loyaltyStatus;
    private int loyaltyPoints;

    public BoatOwnerDTO() {}

    public BoatOwnerDTO(BoatOwner boatOwner) {
        this.email = boatOwner.getEmail();
        this.password = boatOwner.getPassword();
        this.name = boatOwner.getName();
        this.surname = boatOwner.getSurname();
        this.city = boatOwner.getAddress().getPlaceName();
        this.country = boatOwner.getAddress().getCountry();
        this.street = boatOwner.getAddress().getStreet();
        this.phoneNumber = boatOwner.getPhoneNumber();
        this.profilePicture = boatOwner.getProfilePicture().getPhotoPath();
        this.userType = "BOAT OWNER";
        switch (boatOwner.getLoyaltyType()) {
            case REGULAR:
                this.loyaltyStatus = "REGULAR";
                break;
            case SILVER:
                this.loyaltyStatus = "SILVER";
                break;
            default:
                this.loyaltyStatus = "GOLD";
        }
        this.loyaltyPoints = boatOwner.getLoyaltyPoints();
    }

    public BoatOwnerDTO(String email, String password, String name, String surname, String city, String country,
                           String street, String phoneNumber, String profilePicture, String userType, String loyaltyStatus,
                           int loyaltyPoints) {
        this.email = email;
        this.password = password;
        this.name = name;
        this.surname = surname;
        this.city = city;
        this.country = country;
        this.street = street;
        this.phoneNumber = phoneNumber;
        this.profilePicture = profilePicture;
        this.userType = userType;
        this.loyaltyStatus = loyaltyStatus;
        this.loyaltyPoints = loyaltyPoints;
    }

    public void hashPassword() {
        if (!this.password.equals(""))
            this.password = new BCryptPasswordEncoder().encode(this.password);
    }

    public boolean arePropsValid() {
        return (this.password.equals("") || Validate.validatePassword(this.password)) &&
                Validate.validateSurName(this.name) &&
                Validate.validateSurName(this.surname) &&
                Validate.validateWords(this.city) &&
                Validate.validateWords(this.country) &&
                Validate.validateStreet(this.street) &&
                Validate.validateNumber(this.phoneNumber);
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
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

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getProfilePicture() {
        return profilePicture;
    }

    public void setProfilePicture(String profilePicture) {
        this.profilePicture = profilePicture;
    }

    public String getUserType() {
        return userType;
    }

    public void setUserType(String userType) {
        this.userType = userType;
    }

    public String getLoyaltyStatus() {
        return loyaltyStatus;
    }

    public void setLoyaltyStatus(String loyaltyStatus) {
        this.loyaltyStatus = loyaltyStatus;
    }

    public int getLoyaltyPoints() {
        return loyaltyPoints;
    }

    public void setLoyaltyPoints(int loyaltyPoints) {
        this.loyaltyPoints = loyaltyPoints;
    }

}