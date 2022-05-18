package com.ftn.isa.model;

import javax.persistence.*;

@Entity
public class Subscription {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "owner_id", referencedColumnName = "id")
    private User owner;


    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "client_id", referencedColumnName = "id")
    private Client client;

    @Column(name = "is_active_subscription", nullable = false)
    private boolean isActiveSubscription;

    public Subscription() {
    }

    public Subscription(User owner, Client client, boolean isActiveSubscription) {
        this.owner = owner;
        this.client = client;
        this.isActiveSubscription = isActiveSubscription;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public boolean isActiveSubscription() {
        return isActiveSubscription;
    }

    public void setActiveSubscription(boolean activeSubscription) {
        isActiveSubscription = activeSubscription;
    }
}
