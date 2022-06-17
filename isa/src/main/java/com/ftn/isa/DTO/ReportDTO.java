package com.ftn.isa.DTO;

import com.ftn.isa.model.Report;

import java.time.format.DateTimeFormatter;

public class ReportDTO {

    public Long id;
    public String message;
    public boolean isAnswered;
    public String sentTIme;
    public boolean isNegative;
    public boolean hasShowedUp;
    public String ownerName;
    public String clientName;
    public String clientProfilePhoto;
    public String clientEmail;
    public String ownerEmail;

    public ReportDTO(Report report){
        this.id = report.getId();
        this.message = report.getMessage();
        this.isAnswered = report.isAnswered();
        this.sentTIme = report.getSentTime().format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm"));
        this.isNegative = report.isNegative();
        this.hasShowedUp = report.isHasShowedUp();
        this.ownerName = report.getOwner().getName() + " " + report.getOwner().getSurname();
        this.clientName = report.getClient().getName() + " " + report.getClient().getSurname();
        this.clientProfilePhoto = report.getClient().getProfilePicture().getPhotoPath();
        this.clientEmail = report.getClient().getEmail();
        this.ownerEmail = report.getOwner().getEmail();
    }

    public ReportDTO(Long id, String message, boolean isAnswered, String sentTIme, boolean isNegative, boolean hasShowedUp, String ownerName, String clientName, String clientProfilePhoto, String clientEmail, String ownerEmail) {
        this.id = id;
        this.message = message;
        this.isAnswered = isAnswered;
        this.sentTIme = sentTIme;
        this.isNegative = isNegative;
        this.hasShowedUp = hasShowedUp;
        this.ownerName = ownerName;
        this.clientName = clientName;
        this.clientProfilePhoto = clientProfilePhoto;
        this.clientEmail = clientEmail;
        this.ownerEmail = ownerEmail;
    }

    public String getClientEmail() {
        return clientEmail;
    }

    public void setClientEmail(String clientEmail) {
        this.clientEmail = clientEmail;
    }

    public String getOwnerEmail() {
        return ownerEmail;
    }

    public void setOwnerEmail(String ownerEmail) {
        this.ownerEmail = ownerEmail;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public boolean isAnswered() {
        return isAnswered;
    }

    public void setAnswered(boolean answered) {
        isAnswered = answered;
    }

    public String getSentTIme() {
        return sentTIme;
    }

    public void setSentTIme(String sentTIme) {
        this.sentTIme = sentTIme;
    }

    public boolean isNegative() {
        return isNegative;
    }

    public void setNegative(boolean negative) {
        isNegative = negative;
    }

    public boolean isHasShowedUp() {
        return hasShowedUp;
    }

    public void setHasShowedUp(boolean hasShowedUp) {
        this.hasShowedUp = hasShowedUp;
    }

    public String getOwnerName() {
        return ownerName;
    }

    public void setOwnerName(String ownerName) {
        this.ownerName = ownerName;
    }

    public String getClientName() {
        return clientName;
    }

    public void setClientName(String clientName) {
        this.clientName = clientName;
    }

    public String getClientProfilePhoto() {
        return clientProfilePhoto;
    }

    public void setClientProfilePhoto(String clientProfilePhoto) {
        this.clientProfilePhoto = clientProfilePhoto;
    }
}
