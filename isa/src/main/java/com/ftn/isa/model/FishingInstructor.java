package com.ftn.isa.model;


import javax.persistence.*;
import java.util.Set;

@Entity
public class FishingInstructor extends User{


    @OneToMany
    @JoinColumn(name = "adventure_id", referencedColumnName = "id")
    private Set<Adventure> adventures;

    public Set<Adventure> getAdventures() {
        return adventures;
    }

    public void setAdventures(Set<Adventure> adventures) {
        this.adventures = adventures;
    }


}
