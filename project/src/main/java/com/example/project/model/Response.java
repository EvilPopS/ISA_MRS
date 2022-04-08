package com.example.project.model;


import org.aspectj.weaver.ast.Not;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;

@Entity
public class Response extends Notification{


    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "notification_id", referencedColumnName = "id")
    private Notification notification;

    public Notification getNotification(){
        return notification;
    }
    public void setNotification(Notification notif){
        notification = notif;
    }
}
