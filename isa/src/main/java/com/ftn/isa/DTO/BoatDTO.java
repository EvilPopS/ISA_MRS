package com.ftn.isa.DTO;

import com.ftn.isa.helpers.Validate;
import com.ftn.isa.model.Boat;
import com.ftn.isa.model.Photo;

import java.util.HashSet;
import java.util.Set;

public class BoatDTO {

    private Long id;
    private String name;
    private String description;
    private Set<String> photos = new HashSet<>();
    private int capacity;
    private String rules;
    private String country;
    private String city;
    private String street;
    private String lon;
    private String lat;
    private Double averageRating;
    private int noRatings;
    private double price;
    private String type;
    private String boatLength;
    private String engineNumber;
    private String enginePower;
    private String maxSpeed;
    private String navigationEquipment;
    private String fishingEquipment;

    public BoatDTO () {

    }

    public BoatDTO(Boat boat) {
        this.name = boat.getName();
        this.description = boat.getDescription();
        this.capacity = boat.getCapacity();
        this.rules = boat.getRules();
        this.country = boat.getAddress().getCountry();
        this.city = boat.getAddress().getPlaceName();
        this.street = boat.getAddress().getStreet();
        this.lon = boat.getAddress().getLon();
        this.lat = boat.getAddress().getLat();
        this.averageRating = boat.getAverageRate();
        this.noRatings = boat.getNoRatings();
        this.price = boat.getPrice();
        for (Photo p : boat.getPhotos())
            this.photos.add(p.getPhotoPath());
        this.id = boat.getId();
        this.type = boat.getType();
        this.boatLength = boat.getBoatLength();
        this.engineNumber = boat.getEngineNumber();
        this.enginePower = boat.getEnginePower();
        this.maxSpeed = boat.getMaxSpeed();
        this.fishingEquipment = boat.getFishingEquipment();
        this.navigationEquipment = boat.getNavigationEquipment();
    }

    public BoatDTO(String name, String description, int capacity, String rules, String country,
                      String city, String street, String lon, String lat,Double averageRating,
                      double price, int noRatings, Long id,
                   String type, String boatLength, String engineNumber, String enginePower, String maxSpeed,
                   String fishingEquipment, String navigationEquipment) {
        this.name = name;
        this.description = description;
        this.capacity = capacity;
        this.rules = rules;
        this.country = country;
        this.city = city;
        this.street = street;
        this.lon = lon;
        this.lat = lat;
        this.averageRating = averageRating;
        this.noRatings = noRatings;
        this.price = price;
        this.id = id;
        this.type = type;
        this.boatLength = boatLength;
        this.engineNumber = engineNumber;
        this.enginePower = enginePower;
        this.maxSpeed = maxSpeed;
        this.fishingEquipment = fishingEquipment;
        this.navigationEquipment = navigationEquipment;
    }

    public boolean arePropsValidAdding() {
        try {
            double checkLength, checkEngineNum, checkEnginePow, checkMaxSpeed;
            checkLength = Double.parseDouble(this.boatLength);
            checkEngineNum = Double.parseDouble(this.engineNumber);
            checkEnginePow = Double.parseDouble(this.enginePower);
            checkMaxSpeed = Double.parseDouble(this.maxSpeed);
            if (checkLength <= 0 || checkEngineNum <= 0 || checkEnginePow <= 0 || checkMaxSpeed <= 0) return false;
        } catch (Exception e){
            return false;
        }
        return  Validate.validateSurName(this.name) &&
                Validate.validateWords(this.city) &&
                Validate.validateWords(this.country) &&
                Validate.validateStreet(this.street) &&
                Validate.validateStreet(this.type) &&
                this.price > 0 && this.capacity > 0 &&
                this.photos.size() > 0;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public Set<String> getPhotos() {
        return photos;
    }

    public void setPhotos(Set<String> photos) {
        this.photos = photos;
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

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
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

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getBoatLength() {
        return boatLength;
    }

    public void setBoatLength(String boatLength) {
        this.boatLength = boatLength;
    }

    public String getEngineNumber() {
        return engineNumber;
    }

    public void setEngineNumber(String engineNumber) {
        this.engineNumber = engineNumber;
    }

    public String getEnginePower() {
        return enginePower;
    }

    public void setEnginePower(String enginePower) {
        this.enginePower = enginePower;
    }

    public String getMaxSpeed() {
        return maxSpeed;
    }

    public void setMaxSpeed(String maxSpeed) {
        this.maxSpeed = maxSpeed;
    }

    public String getNavigationEquipment() {
        return navigationEquipment;
    }

    public void setNavigationEquipment(String navigationEquipment) {
        this.navigationEquipment = navigationEquipment;
    }

    public String getFishingEquipment() {
        return fishingEquipment;
    }

    public void setFishingEquipment(String fishingEquipment) {
        this.fishingEquipment = fishingEquipment;
    }

    public int getNoRatings() {
        return noRatings;
    }

    public void setNoRatings(int noRatings) {
        this.noRatings = noRatings;
    }
}
