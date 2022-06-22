package com.ftn.isa.model;


import javax.persistence.*;

@Entity
public class Report extends Notification{
    @Column(name = "is_negative", nullable = false)
    private boolean isNegative;

    @Column(name = "has_showedUp")
    private boolean hasShowedUp;

    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "owner_id", referencedColumnName = "id")
    private User owner;

    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "client_id", referencedColumnName = "id")
    private Client client;

    @Column(name="client_is_sender")
    private boolean clientIsSender;

    public Report(String message, boolean isAnswered, String localDateTime) {
        super(message, isAnswered, localDateTime);
    }

    public Report(String message, boolean isAnswered, String localDateTime, Client client, User owner,
                  boolean hasShowedUp, boolean isNegative) {
        super(message, isAnswered, localDateTime);
        this.client = client;
        this.owner = owner;
        this.isNegative = isNegative;
        this.hasShowedUp = hasShowedUp;
    }

    public Report(String message, Client client, User owner, Boolean clientIsSender) {
        super(message);
        this.client = client;
        this.owner = owner;
        this.clientIsSender = clientIsSender;
        this.isNegative = true;
    }

    public Report() {}

    public boolean isNegative() {
        return isNegative;
    }

    public void setNegative(boolean negative) {
        isNegative = negative;
    }

    public boolean isHasShowedUp() {
        return hasShowedUp;
    }

    public void setHasShowedUp(boolean hasShowedUp) {
        this.hasShowedUp = hasShowedUp;
    }

    public User getOwner() {
        return owner;
    }

    public void setOwner(User owner) {
        this.owner = owner;
    }

    public Client getClient() {
        return client;
    }

    public void setClient(Client client) {
        this.client = client;
    }

    public boolean isClientIsSender() {
        return clientIsSender;
    }

    public void setClientIsSender(boolean clientIsSender) {
        this.clientIsSender = clientIsSender;
    }
}
