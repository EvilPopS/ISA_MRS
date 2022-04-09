package com.ftn.isa.model;


import javax.persistence.*;

@Entity
public class Review extends Notification{

    @Column(name = "grade", nullable = false)
    private Double grade;


    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "reciever_id", referencedColumnName = "id")
    private User reciever;


    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "rentalService_id", referencedColumnName = "id")
    private RentalService rentalService;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "sender_id", referencedColumnName = "id")
    private Client sender;

    public Double getGrade() {
        return grade;
    }

    public void setGrade(Double grade) {
        this.grade = grade;
    }

    public User getReciever() {
        return reciever;
    }

    public void setReciever(User reciever) {
        this.reciever = reciever;
    }

    public RentalService getRentalService() {
        return rentalService;
    }

    public void setRentalService(RentalService rentalService) {
        this.rentalService = rentalService;
    }

    public Client getSender() {
        return sender;
    }

    public void setSender(Client sender) {
        this.sender = sender;
    }
}
