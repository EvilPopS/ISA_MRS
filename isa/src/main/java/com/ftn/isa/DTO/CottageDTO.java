package com.ftn.isa.DTO;

import com.ftn.isa.helpers.Validate;
import com.ftn.isa.model.Cottage;
import com.ftn.isa.model.Photo;

import java.util.HashSet;
import java.util.Set;

public class CottageDTO {

    private Long id;
    private String name;
    private String description;
    private Set<String> photos = new HashSet<String>();
    private int capacity;
    private String rules;
    private String city;
    private String zipCode;
    private String street;
    private String lon;
    private String lat;
    private Double averageRating;
    private double price;
    private String additionalServices;
    private int noRatings;
    private int noRooms;

    public CottageDTO(Cottage cottage) {
        this.name = cottage.getName();
        this.description = cottage.getDescription();
        this.capacity = cottage.getCapacity();
        this.rules = cottage.getRules();
        this.city = cottage.getAddress().getPlaceName();
        this.zipCode = cottage.getAddress().getZipCode();
        this.street = cottage.getAddress().getStreet();
        this.lon = cottage.getAddress().getLon();
        this.lat = cottage.getAddress().getLat();
        this.averageRating = cottage.getAverageRate();
        this.price = cottage.getPrice();
        this.additionalServices = cottage.getAdditionalServices();
        for (Photo p : cottage.getPhotos()){this.photos.add(p.getPhotoPath());}
        this.noRatings = cottage.getNoRatings();
        this.noRooms = cottage.getNoRooms();
        this.id = cottage.getId();
    }

    public CottageDTO(String name, String description, int capacity, String rules,
                      String city, String zipCode, String street, String lon, String lat,Double averageRating,
                      double price, String additionalServices, int noRatings, int noRooms, Long id) {
        this.name = name;
        this.description = description;
        this.capacity = capacity;
        this.rules = rules;
        this.city = city;
        this.zipCode = zipCode;
        this.street = street;
        this.lon = lon;
        this.lat = lat;
        this.averageRating = averageRating;
        this.price = price;
        this.additionalServices = additionalServices;
        this.noRatings = noRatings;
        this.noRooms = noRooms;
        this.id = id;
    }

    public String getLon() {
        return lon;
    }

    public void setLon(String lon) {
        this.lon = lon;
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

    public int getNoRooms() {
        return noRooms;
    }

    public void setNoRooms(int noRooms) {
        this.noRooms = noRooms;
    }

    public int getNoRatings() {
        return noRatings;
    }

    public void setNoRatings(int noRatings) {
        this.noRatings = noRatings;
    }

    public String getAdditionalServices() {
        return additionalServices;
    }

    public void setAdditionalServices(String additionalServices) {
        this.additionalServices = additionalServices;
    }

    public Set<String> getPhotos() {
        return photos;
    }

    public void setPhotos(Set<String> photos) {
        this.photos = photos;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getCapacity() {
        return capacity;
    }

    public void setCapacity(int capacity) {
        this.capacity = capacity;
    }

    public String getRules() {
        return rules;
    }

    public void setRules(String rules) {
        this.rules = rules;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getZipCode() {
        return zipCode;
    }

    public void setZipCode(String zipCode) {
        this.zipCode = zipCode;
    }

    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public Double getAverageRating() {
        return averageRating;
    }

    public void setAverageRating(Double averageRating) {
        this.averageRating = averageRating;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public boolean arePropsValidAdding() {
        return  Validate.validateSurName(this.name) &&
                Validate.validateWords(this.city) &&
                Validate.validateNumber(this.zipCode) &&
                Validate.validateStreet(this.street) &&
                this.price > 0 && this.noRooms > 0 && this.capacity > 0 &&
                this.photos.size() > 0 && this.noRatings == 0 && this.averageRating == 0
                ;

    }
}
