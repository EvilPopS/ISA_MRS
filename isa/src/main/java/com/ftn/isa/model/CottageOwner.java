package com.ftn.isa.model;


import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
public class CottageOwner extends User{


    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name = "cottage_id", referencedColumnName = "id")
    private Set<Cottage> cottages = new HashSet<>();

    public Set<Cottage> getCottages() {
        return cottages;
    }

    public void setCottages(Set<Cottage> cottages) {
        this.cottages = cottages;
    }
}
