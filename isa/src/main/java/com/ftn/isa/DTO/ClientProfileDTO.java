package com.ftn.isa.DTO;


import com.ftn.isa.model.Client;

public class ClientProfileDTO {
    private String email;
    private String password;
    private String name;
    private String surname;
    private String city;
    private String zipcode;
    private String street;
    private String phoneNumber;
    private String profilePicture;
    private String userType;
    private String loyaltyStatus;
    private int loyaltyPoints;


    public ClientProfileDTO(Client client) {
        this.email = client.getEmail();
        this.password = client.getPassword();
        this.name = client.getName();
        this.surname = client.getSurname();
        this.city = client.getAddress().getPlaceName();
        this.zipcode = client.getAddress().getZipCode();
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
    }

    public ClientProfileDTO(String email, String password, String name, String surname, String city, String zipcode, String street, String phoneNumber, String profilePicture, String userType, String loyaltyStatus, int loyaltyPoints) {
        this.email = email;
        this.password = password;
        this.name = name;
        this.surname = surname;
        this.city = city;
        this.zipcode = zipcode;
        this.street = street;
        this.phoneNumber = phoneNumber;
        this.profilePicture = profilePicture;
        this.userType = userType;
        this.loyaltyStatus = loyaltyStatus;
        this.loyaltyPoints = loyaltyPoints;
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

    public String getZipcode() {
        return zipcode;
    }

    public void setZipcode(String zipcode) {
        this.zipcode = zipcode;
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
