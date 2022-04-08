package com.example.project.model;

import javax.persistence.CascadeType;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import java.util.HashSet;
import java.util.Set;

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
