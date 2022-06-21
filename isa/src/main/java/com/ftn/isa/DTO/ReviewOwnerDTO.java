package com.ftn.isa.DTO;

import com.ftn.isa.model.Review;

import java.time.LocalDateTime;

public class ReviewOwnerDTO {

    private Double grade;

    private String clientFullName;

    private String message;

    private LocalDateTime sentTime;

    public ReviewOwnerDTO() {

    }

    public ReviewOwnerDTO(Review r) {
        this.grade = r.getGrade();
        this.clientFullName = r.getSender().getName() + r.getSender().getSurname();
        this.message = r.getMessage();
        this.sentTime = r.getSentTime();
    }

    public Double getGrade() {
        return grade;
    }

    public void setGrade(Double grade) {
        this.grade = grade;
    }

    public String getClientFullName() {
        return clientFullName;
    }

    public void setClientFullName(String clientFullName) {
        this.clientFullName = clientFullName;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public LocalDateTime getSentTime() {
        return sentTime;
    }

    public void setSentTime(LocalDateTime sentTime) {
        this.sentTime = sentTime;
    }
}
