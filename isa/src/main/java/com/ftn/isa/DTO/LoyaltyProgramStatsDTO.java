package com.ftn.isa.DTO;

import com.ftn.isa.model.LoyaltyProgram;
import com.ftn.isa.model.LoyaltyType;

import java.util.List;

public class LoyaltyProgramStatsDTO {
    private double bronzeDisc;
    private int bronzePrice;
    private double bronzeBonus;
    private double silverDisc;
    private int silverPrice;
    private double silverBonus;
    private double goldDisc;
    private int goldPrice;
    private double goldBonus;

    public LoyaltyProgramStatsDTO(List<LoyaltyProgram> lp) {
        for (LoyaltyProgram loy : lp) {
            switch (loy.getLoyaltyType()) {
                case BRONZE:
                    this.bronzeDisc = loy.getDiscount();
                    this.bronzePrice = loy.getPrice();
                    this.bronzeBonus = loy.getIncrease();
                    break;
                case SILVER:
                    this.silverDisc = loy.getDiscount();
                    this.silverPrice = loy.getPrice();
                    this.silverBonus = loy.getIncrease();
                    break;
                case GOLD:
                    this.goldDisc = loy.getDiscount();
                    this.goldPrice = loy.getPrice();
                    this.goldBonus = loy.getIncrease();
            }
        }
    }

    public double getBronzeDisc() {
        return bronzeDisc;
    }

    public void setBronzeDisc(double bronzeDisc) {
        this.bronzeDisc = bronzeDisc;
    }

    public int getBronzePrice() {
        return bronzePrice;
    }

    public void setBronzePrice(int bronzePrice) {
        this.bronzePrice = bronzePrice;
    }

    public double getBronzeBonus() {
        return bronzeBonus;
    }

    public void setBronzeBonus(double bronzeBonus) {
        this.bronzeBonus = bronzeBonus;
    }

    public double getSilverDisc() {
        return silverDisc;
    }

    public void setSilverDisc(double silverDisc) {
        this.silverDisc = silverDisc;
    }

    public int getSilverPrice() {
        return silverPrice;
    }

    public void setSilverPrice(int silverPrice) {
        this.silverPrice = silverPrice;
    }

    public double getSilverBonus() {
        return silverBonus;
    }

    public void setSilverBonus(double silverBonus) {
        this.silverBonus = silverBonus;
    }

    public double getGoldDisc() {
        return goldDisc;
    }

    public void setGoldDisc(double goldDisc) {
        this.goldDisc = goldDisc;
    }

    public int getGoldPrice() {
        return goldPrice;
    }

    public void setGoldPrice(int goldPrice) {
        this.goldPrice = goldPrice;
    }

    public double getGoldBonus() {
        return goldBonus;
    }

    public void setGoldBonus(double goldBonus) {
        this.goldBonus = goldBonus;
    }
}
