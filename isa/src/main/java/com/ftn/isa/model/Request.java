package com.ftn.isa.model;


import javax.persistence.*;

@Entity
public class Request extends Notification {


    @Column(name = "request_type", nullable = false)
    private RequestType requestType;

    @OneToOne
    @JoinColumn(name = "sender_id", referencedColumnName = "id")
    private User sender;

    public Request(String message, boolean isAnswered, String localDateTime, String requestType, User sender) {
        super(message, isAnswered, localDateTime);
        this.requestType = (requestType.equals("ACCOUNT DELETION")) ? RequestType.ACCOUNT_DELETION : RequestType.ACCOUNT_REGISTRATION;
        this.sender = sender;
    }

    public Request() {

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
