package com.ftn.isa.DTO;

import com.ftn.isa.helpers.Validate;


public class UserRegDTO {
    private String name;
    private String surname;
    private String email;
    private String country;
    private String city;
    private String address;
    private String phoneNumber;
    private String password;
    private String profilePicture;


    public boolean arePropsValid() {
        return Validate.validatePassword(this.password) &&
                Validate.validateSurName(this.name) &&
                Validate.validateSurName(this.surname) &&
                Validate.validateWords(this.country) &&
                Validate.validateWords(this.city) &&
                Validate.validateStreet(this.address) &&
                Validate.validateNumber(this.phoneNumber) &&
                Validate.validateEmail(this.email);
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

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
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

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getProfilePicture() {
        return profilePicture;
    }

    public void setProfilePicture(String profilePicture) {
        this.profilePicture = profilePicture;
    }
}
