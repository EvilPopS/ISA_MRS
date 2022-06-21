package com.ftn.isa.DTO;

import com.ftn.isa.helpers.Validate;
import com.ftn.isa.model.FishingInstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class FishingInstructorDTO {

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


    public FishingInstructorDTO(FishingInstructor instructor) {
        this.email = instructor.getEmail();
        this.password = instructor.getPassword();
        this.name = instructor.getName();
        this.surname = instructor.getSurname();
        this.city = instructor.getAddress().getPlaceName();
        this.country = instructor.getAddress().getCountry();
        this.street = instructor.getAddress().getStreet();
        this.phoneNumber = instructor.getPhoneNumber();
        this.profilePicture = instructor.getProfilePicture().getPhotoPath();
        this.userType = "INSTRUCTOR";
        switch (instructor.getLoyaltyType()) {
            case REGULAR:
                this.loyaltyStatus = "REGULAR";
                break;
            case BRONZE:
                this.loyaltyStatus = "BRONZE";
                break;
            case SILVER:
                this.loyaltyStatus = "SILVER";
                break;
            default:
                this.loyaltyStatus = "GOLD";
        }
        this.loyaltyPoints = instructor.getLoyaltyPoints();
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

    public FishingInstructorDTO(String email, String password, String name, String surname, String city, String country,
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
