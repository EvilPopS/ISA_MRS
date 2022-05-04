package com.ftn.isa.DTO;

import com.ftn.isa.model.Request;
import com.ftn.isa.model.RequestType;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.format.DateTimeFormatter;

public class RequestDTO {


    public Long requestId;
    public Long senderId;
    public boolean isAnswered;
    public String message;
    public String sentTime;
    public String requestType;

    public RequestDTO(Long requestId, Long senderId, boolean isAnswered, String message, String sentTime, String requestType) {
        this.requestId = requestId;
        this.senderId = senderId;
        this.isAnswered = isAnswered;
        this.message = message;
        this.sentTime = sentTime;
        this.requestType = requestType;
    }

    public RequestDTO(Request request){
        this.requestId = request.getId();
        this.senderId = request.getSender().getId();
        this.isAnswered = request.isAnswered();
        this.message = request.getMessage();
        this.sentTime = request.getSentTime().format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm"));
        this.requestType = (request.getRequestType() == RequestType.ACCOUNT_DELETION ) ? "ACCOUNT DELETION" : "ACCOUNT REGISTRATION";

    }

    public Long getRequestId() {
        return requestId;
    }

    public void setRequestId(Long requestId) {
        this.requestId = requestId;
    }

    public Long getSenderId() {
        return senderId;
    }

    public void setSenderId(Long senderId) {
        this.senderId = senderId;
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

    public String getRequestType() {
        return requestType;
    }

    public void setRequestType(String requestType) {
        this.requestType = requestType;
    }
}
