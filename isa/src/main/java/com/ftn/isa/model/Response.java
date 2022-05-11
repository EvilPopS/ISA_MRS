package com.ftn.isa.model;


import org.aspectj.weaver.ast.Not;

import javax.persistence.*;

@Entity
public class Response extends Notification{


    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "notification_id", referencedColumnName = "id")
    private Notification notification;

    public Response(String message, boolean isAnswered, String localDateTime) {
        super(message, isAnswered, localDateTime);
    }

    public Response() {

    }

    public Notification getNotification(){
        return notification;
    }
    public void setNotification(Notification notif){
        notification = notif;
    }
}
