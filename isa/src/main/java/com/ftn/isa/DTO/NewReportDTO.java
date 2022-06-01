package com.ftn.isa.DTO;

import com.ftn.isa.helpers.Validate;

public class NewReportDTO {

    private boolean isNegative;

    private boolean hasShowedUp;

    private String message;

    private String clientEmail;

    public NewReportDTO() {

    }

    public NewReportDTO(boolean isNegative, boolean hasShowedUp, String message, String clientEmail) {
        this.isNegative = isNegative;
        this.hasShowedUp = hasShowedUp;
        this.message = message;
        this.clientEmail = clientEmail;
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

    public void setNegative(boolean negativeP) {
        isNegative = negativeP;
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


}
