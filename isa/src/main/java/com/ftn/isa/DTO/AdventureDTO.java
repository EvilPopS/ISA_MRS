package com.ftn.isa.DTO;

import com.ftn.isa.helpers.Validate;
import com.ftn.isa.model.Adventure;
import com.ftn.isa.model.Photo;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class AdventureDTO {

    private Long id;
    private String name;
    private String description;
    private String rules;
    private Double price;
    private Double cancellationConditions;
    private String biography;
    private String fishingEquipment;
    private String city;
    private String street;
    private String zipcode;
    private int capacity;
    private Double rating;
    private int noRatings;
    private Set<String> photos = new HashSet<>();

    public AdventureDTO(Adventure adventure){
        this.id = adventure.getId();
        this.name = adventure.getName();
        this.description = adventure.getDescription();
        this.rules = adventure.getRules();
        this.price = adventure.getPrice();
        this.cancellationConditions = adventure.getCancellationConditions();
        this.biography = adventure.getBiography();
        this.fishingEquipment = adventure.getFishingEquipment();
        this.city = adventure.getAddress().getPlaceName();
        this.street = adventure.getAddress().getStreet();
        this.zipcode = adventure.getAddress().getZipCode();
        this.rating = adventure.getAverageRate();
        this.capacity = adventure.getCapacity();
        this.noRatings = adventure.getNoRatings();
        for (Photo p : adventure.getPhotos()){this.photos.add(p.getPhotoPath());};
    }

    public AdventureDTO(){}

    public AdventureDTO(Long id,String name, String description, String rules, Double price, Double cancellationConditions, String biography, String fishingEquipment, String city, String street, String zipcode, int capacity, Double rating, int noRatings, Set<String> photos) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.rules = rules;
        this.price = price;
        this.cancellationConditions = cancellationConditions;
        this.biography = biography;
        this.fishingEquipment = fishingEquipment;
        this.city = city;
        this.street = street;
        this.zipcode = zipcode;
        this.capacity = capacity;
        this.rating = rating;
        this.noRatings = noRatings;
        this.photos = photos;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setRating(Double rating) {
        this.rating = rating;
    }

    public int getCapacity() {
        return capacity;
    }

    public void setCapacity(int capacity) {
        this.capacity = capacity;
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

    public String getRules() {
        return rules;
    }

    public void setRules(String rules) {
        this.rules = rules;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public Double getCancellationConditions() {
        return cancellationConditions;
    }

    public void setCancellationConditions(Double cancellationConditions) {
        this.cancellationConditions = cancellationConditions;
    }

    public String getBiography() {
        return biography;
    }

    public void setBiography(String biography) {
        this.biography = biography;
    }

    public String getFishingEquipment() {
        return fishingEquipment;
    }

    public void setFishingEquipment(String fishingEquipment) {
        this.fishingEquipment = fishingEquipment;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public String getZipcode() {
        return zipcode;
    }

    public void setZipcode(String zipcode) {
        this.zipcode = zipcode;
    }

    public double getRating() {
        return rating;
    }

    public void setRating(double rating) {
        this.rating = rating;
    }

    public int getNoRatings() {
        return noRatings;
    }

    public void setNoRatings(int noRatings) {
        this.noRatings = noRatings;
    }

    public Set<String> getPhotos() {
        return photos;
    }

    public void setPhotos(Set<String> photos) {
        this.photos = photos;
    }

    public boolean propsValid() {
        return  Validate.validateSurName(this.name) &&
                Validate.validateWords(this.city) &&
                Validate.validateNumber(this.zipcode) &&
                Validate.validateStreet(this.street) &&
                this.price > 0 && !this.biography.equals("") && this.capacity > 0 &&
                this.photos.size() > 0 && this.noRatings == 0 && this.rating == 0
                ;
    }
}
