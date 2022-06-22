package com.ftn.isa.model;


import javax.persistence.*;

@Entity
public class Review extends Notification{

    @Column(name = "grade", nullable = false)
    private Double grade;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "receiver_id", referencedColumnName = "id")
    private User receiver;

    @OneToOne()
    @JoinColumn(name = "rental_id", referencedColumnName = "id")
    private RentalService rentalService;

    @OneToOne()
    @JoinColumn(name = "sender_id", referencedColumnName = "id")
    private Client sender;


    public Review(String message, boolean isAnswered, String localDateTime) {
        super(message, isAnswered, localDateTime);
    }

    public Review(String message, double rating, Client sender, RentalService rental) {
        super(message);
        this.grade = rating;
        this.sender = sender;
        this.rentalService = rental;
    }
    public Review() {

    }

    public Double getGrade() {
        return grade;
    }

    public void setGrade(Double grade) {
        this.grade = grade;
    }

    public User getReceiver() {
        return receiver;
    }

    public void setReceiver(User receiver) {
        this.receiver = receiver;
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
