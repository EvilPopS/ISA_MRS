package com.example.project.model;

import javax.persistence.*;

@Entity
public class Report extends Notification{


    @Column(name = "isNegative", nullable = false)
    private boolean isNegative;

    @Column(name = "hasShowedUp", nullable = false)
    private boolean hasShowedUp;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "owner_id", referencedColumnName = "id")
    private User owner;


    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "client_id", referencedColumnName = "id")
    private Client client;

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
}
