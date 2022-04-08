package com.example.project.model;


import javax.persistence.*;

@Entity
public class Room {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;


    @Column(name = "noBeds", nullable = false)
    private int noBeds;


    public int getNoBeds() {
        return noBeds;
    }

    public void setNoBeds(int noBeds) {
        this.noBeds = noBeds;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
}
