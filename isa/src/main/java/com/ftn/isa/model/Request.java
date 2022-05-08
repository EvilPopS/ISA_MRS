package com.ftn.isa.model;


import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
public class Request extends Notification {


    @Column(name = "request_type", nullable = false)
    private RequestType requestType;

    @OneToOne(cascade = CascadeType.MERGE, fetch = FetchType.EAGER)
    @JoinColumn(name = "sender_id", referencedColumnName = "id")
    private User sender;

    public Request(){

    }

    public Request(RequestType requestType, User sender, String message){
        this.requestType = requestType;
        this.sender = sender;
        this.setMessage(message);
        this.setAnswered(false);
        this.setSentTime(LocalDateTime.now());
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
