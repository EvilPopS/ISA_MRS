package com.ftn.isa.DTO;

import com.ftn.isa.model.Review;

import java.time.format.DateTimeFormatter;

public class ReviewDTO {


    public boolean isAnswered;
    public String message;
    public String sentTime;
    public String rentalServiceName;
    public Double grade;
    public Long recieverId;
    public Long rentalServiceId;
    public int rentalServiceType;
    public Long senderId;


    public ReviewDTO (){
    }

    public ReviewDTO(Review review){
        this.isAnswered = review.isAnswered();
        this.message = review.getMessage();
        this.sentTime = review.getSentTime().format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm"));
        this.grade = review.getGrade();
        this.recieverId = review.getReceiver().getId();
        this.rentalServiceId = review.getRentalService().getId();
        this.rentalServiceName = review.getRentalService().getName();
        this.rentalServiceType = review.getRentalService().getRentalType().ordinal();
        this.senderId = review.getSender().getId();

    }

    public ReviewDTO(boolean isAnswered, String message, String sentTime, String rentalServiceName, Double grade, Long recieverId, Long rentalServiceId, int rentalServiceType, Long senderId) {
        this.isAnswered = isAnswered;
        this.message = message;
        this.sentTime = sentTime;
        this.rentalServiceName = rentalServiceName;
        this.grade = grade;
        this.recieverId = recieverId;
        this.rentalServiceId = rentalServiceId;
        this.rentalServiceType = rentalServiceType;
        this.senderId = senderId;
    }

    public int getRentalServiceType() {
        return rentalServiceType;
    }

    public void setRentalServiceType(int rentalServiceType) {
        this.rentalServiceType = rentalServiceType;
    }

    public String getRentalServiceName() {
        return rentalServiceName;
    }

    public void setRentalServiceName(String rentalServiceName) {
        this.rentalServiceName = rentalServiceName;
    }

    public boolean isAnswered() {
        return isAnswered;
    }

    public void setAnswered(boolean answered) {
        isAnswered = answered;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getSentTime() {
        return sentTime;
    }

    public void setSentTime(String sentTime) {
        this.sentTime = sentTime;
    }

    public Double getGrade() {
        return grade;
    }

    public void setGrade(Double grade) {
        this.grade = grade;
    }

    public Long getRecieverId() {
        return recieverId;
    }

    public void setRecieverId(Long recieverId) {
        this.recieverId = recieverId;
    }

    public Long getRentalServiceId() {
        return rentalServiceId;
    }

    public void setRentalServiceId(Long rentalServiceId) {
        this.rentalServiceId = rentalServiceId;
    }

    public Long getSenderId() {
        return senderId;
    }

    public void setSenderId(Long senderId) {
        this.senderId = senderId;
    }
}
