package com.example.project.model;

import javax.persistence.*;

@Entity
public class LoyaltyProgram {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private LoyaltyType loyaltyType;

    private Double discount;

    private Double increase;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }


}
