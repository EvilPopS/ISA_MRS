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

    @Column(name = "price", nullable = false)
    private int price;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LoyaltyType getLoyaltyType() {
        return loyaltyType;
    }

    public void setLoyaltyType(LoyaltyType loyaltyType) {
        this.loyaltyType = loyaltyType;
    }

    public Double getDiscount() {
        return discount;
    }

    public void setDiscount(Double discount) {
        this.discount = discount;
    }

    public Double getIncrease() {
        return increase;
    }

    public void setIncrease(Double increase) {
        this.increase = increase;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }
}
