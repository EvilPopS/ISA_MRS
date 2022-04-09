package com.ftn.isa.model;

import javax.persistence.*;
import java.util.Set;

@Entity
public class Admin extends User{


    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name = "response_id", referencedColumnName = "id")
    private Set<Response> responses;

    public Admin() {
    }

    public Admin(Set<Response> responses) {
        this.responses = responses;
    }

    public Set<Response> getResponses() {
        return responses;
    }

    public void setResponses(Set<Response> responses) {
        this.responses = responses;
    }
}
