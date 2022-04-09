package com.ftn.isa.model;

import javax.persistence.*;

@Entity
public class LoyaltyProgram {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "loyalty_type", nullable = false, unique = true)
    private LoyaltyType loyaltyType;

    @Column(name = "discount", nullable = false)
    private Double discount;

    @Column(name = "increase", nullable = false)
    private Double increase;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }


}
