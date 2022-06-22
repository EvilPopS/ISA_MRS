package com.ftn.isa.DTO;

import com.ftn.isa.helpers.Validate;

public class NewReportDTO {

    private boolean isNegative;

    private boolean hasShowedUp;

    private String message;

    private String clientEmail;

    private String ownerRole;

    public NewReportDTO() {

    }

    public NewReportDTO(boolean isNegative, boolean hasShowedUp, String message, String clientEmail, String ownerRole) {
        this.isNegative = isNegative;
        this.hasShowedUp = hasShowedUp;
        this.message = message;
        this.clientEmail = clientEmail;
        this.ownerRole = ownerRole;
    }

    public boolean arePropsValid() {
        return Validate.validateStreet(this.message) && this.message.length() >= 15;
    }

    public String getClientEmail() {
        return clientEmail;
    }

    public void setClientEmail(String clientEmail) {
        this.clientEmail = clientEmail;
    }

    public boolean isNegative() {
        return isNegative;
    }

    public void setIsNegative(boolean negativeP) {
        isNegative = negativeP;
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

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getOwnerRole() {
        return ownerRole;
    }

    public void setOwnerRole(String ownerRole) {
        this.ownerRole = ownerRole;
    }
}
