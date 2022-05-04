package com.ftn.isa.model;


import javax.persistence.*;

@Entity
public class Address {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name="country", nullable = false)
    private String country;

    @Column(name = "place_name", nullable = false)
    private String placeName;

    @Column(name = "street", nullable = false)
    private String street;

    @Column(name = "lon")
    private String lon;

    @Column(name = "lat")
    private String lat;


    public Address(){}

    public Address(String country, String placeName, String street) {
        this.country = country;
        this.placeName = placeName;
        this.street = street;
    }

    public Address(String country, String placeName, String street, String lon, String lat) {
        this.country = country;
        this.placeName = placeName;
        this.street = street;
        this.lon = lon;
        this.lat = lat;
    }

    public String getLat() {
        return lat;
    }

    public void setLat(String lat) {
        this.lat = lat;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getPlaceName() {
        return placeName;
    }

    public void setPlaceName(String placeName) {
        this.placeName = placeName;
    }

    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getLon() {
        return lon;
    }

    public void setLon(String lon) {
        this.lon = lon;
    }
}
