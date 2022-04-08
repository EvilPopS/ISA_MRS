package com.example.project.model;


import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
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
