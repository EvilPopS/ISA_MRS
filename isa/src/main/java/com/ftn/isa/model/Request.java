package com.ftn.isa.model;


import com.ftn.isa.DTO.DelReqDTO;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
public class Request extends Notification {
    @Column(name = "request_type", nullable = false)
    private RequestType requestType;

    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "sender_id", referencedColumnName = "id")
    private User sender;

    public Request(){
    }
  
    public Request(String message, boolean isAnswered, String localDateTime, String requestType, User sender) {
        super(message, isAnswered, localDateTime);
        this.requestType = (requestType.equals("ACCOUNT DELETION")) ? RequestType.ACCOUNT_DELETION : RequestType.ACCOUNT_REGISTRATION;
        this.sender = sender;
    }
  
    public Request(RequestType requestType, User sender, String message){
        this.requestType = requestType;
        this.sender = sender;
        this.setMessage(message);
        this.setAnswered(false);
        this.setSentTime(LocalDateTime.now());
    }

    public Request(DelReqDTO delReq) {
        super(delReq.getMessage(), delReq.getSentTime());
        this.requestType = RequestType.ACCOUNT_DELETION;
        this.sender = delReq.getSender();
    }

    public RequestType getRequestType(){
        return requestType;
    }

    public void setRequestType(RequestType req){
        requestType = req;
    }

    public User getSender() {
        return sender;
    }

    public void setSender(User sender) {
        this.sender = sender;
    }
}
