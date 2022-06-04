package com.ftn.isa.DTO;


import com.ftn.isa.helpers.Validate;
import com.ftn.isa.model.Client;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class ClientProfileDTO {
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
    private int penalties;


    public ClientProfileDTO() {}

    public ClientProfileDTO(Client client) {
        this.email = client.getEmail();
        this.password = client.getPassword();
        this.name = client.getName();
        this.surname = client.getSurname();
        this.city = client.getAddress().getPlaceName();
        this.country = client.getAddress().getCountry();
        this.street = client.getAddress().getStreet();
        this.phoneNumber = client.getPhoneNumber();
        this.profilePicture = client.getProfilePicture().getPhotoPath();
        this.userType = "CLIENT";
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
        this.loyaltyPoints = client.getLoyaltyPoints();
        this.penalties = client.getNumOfPenalties();
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

    public int getPenalties() {
        return penalties;
    }

    public void setPenalties(int penalties) {
        this.penalties = penalties;
    }
}
