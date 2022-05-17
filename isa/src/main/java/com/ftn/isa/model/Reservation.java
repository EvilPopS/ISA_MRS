package com.ftn.isa.model;


import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
public class Reservation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "start_time", nullable = false)
    private LocalDateTime startTime;

    @Column(name = "end_time", nullable = false)
    private LocalDateTime endTime;

    @Column(name = "is_action", nullable = false)
    private boolean isAction;

    @Column(name = "price", nullable = false)
    private Double price;

    @Column(name = "is_reserved", nullable = false)
    private boolean isReserved;

    @Column(name = "is_unvailable", nullable = false)
    private boolean isUnvailable;

    @Column(name = "action_services", nullable = true)
    private String actionServices;

    public Reservation() {

    }

    public Reservation(LocalDateTime startTime, LocalDateTime endTime, boolean isAction,
                       Double price, boolean isReserved, boolean isUnvailable, String actionServices) {
        this.startTime = startTime;
        this.endTime = endTime;
        this.isAction = isAction;
        this.price = price;
        this.isReserved = isReserved;
        this.isUnvailable = isUnvailable;
        this.actionServices = actionServices;
    }

    public LocalDateTime getStartTime() {
        return startTime;
    }

    public void setStartTime(LocalDateTime startTime) {
        this.startTime = startTime;
    }

    public LocalDateTime getEndTime() {
        return endTime;
    }

    public void setEndTime(LocalDateTime endTime) {
        this.endTime = endTime;
    }

    public boolean isAction() {
        return isAction;
    }

    public void setAction(boolean action) {
        isAction = action;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public boolean isReserved() {
        return isReserved;
    }

    public void setReserved(boolean reserved) {
        isReserved = reserved;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public boolean isUnvailable() {
        return isUnvailable;
    }

    public void setUnvailable(boolean unvailable) {
        isUnvailable = unvailable;
    }

    public String getActionServices() {
        return actionServices;
    }

    public void setActionServices(String actionServices) {
        this.actionServices = actionServices;
    }
}
