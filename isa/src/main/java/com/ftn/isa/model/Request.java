package com.ftn.isa.model;


import javax.persistence.*;

@Entity
public class Request extends Notification {


    @Column(name = "request_type", nullable = false)
    private RequestType requestType;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "sender_id", referencedColumnName = "id")
    private User sender;

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
