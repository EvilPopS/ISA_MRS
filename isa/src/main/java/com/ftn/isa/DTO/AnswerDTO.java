package com.ftn.isa.DTO;

public class AnswerDTO {

    public Long reportId;
    public String message;
    public String clientEmail;
    public String ownerEmail;

    public AnswerDTO(){}

    public AnswerDTO(Long reportId, String message, String clientEmail, String ownerEmail) {
        this.reportId = reportId;
        this.message = message;
        this.clientEmail = clientEmail;
        this.ownerEmail = ownerEmail;
    }

    public Long getReportId() {
        return reportId;
    }

    public void setReportId(Long reportId) {
        this.reportId = reportId;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
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
}
