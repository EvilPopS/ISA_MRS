package com.ftn.isa.model;


import javax.persistence.*;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
public abstract class RentalService {
    @Id
    @SequenceGenerator(name = "my_seq_gen_rental", sequenceName = "my_seq_gen_rental", initialValue = 1, allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "my_seq_gen_rental")
    private Long id;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "description", nullable = false)
    private String description;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinColumn(name = "rental_id", referencedColumnName = "id")
    private Set<Photo> photos;

    @Column(name = "capacity", nullable = false)
    private int capacity;

    @Column(name = "rules", nullable = false)
    private String rules;

    @Column(name = "is_deleted", nullable = false)
    private boolean isDeleted;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "address_id", referencedColumnName = "id")
    private Address address;

    @Column(name = "average_rate", nullable = false)
    private Double averageRate;

    @Column(name = "no_ratings", nullable = false)
    private int noRatings;

    @Column(name = "rental_type", nullable = false)
    private RentalType rentalType;

    @Column(name = "price", nullable = false)
    private Double price;
  
    @OneToMany(cascade = CascadeType.ALL, fetch=FetchType.EAGER)
    @JoinColumn(name = "rental_id", referencedColumnName = "id")
    private List<Reservation> reservations;
  
    public RentalService() {

    }

    public RentalService(String name, String description, int capacity, String rules,
                         boolean isDeleted, Address address, Double averageRate,
                         int noRatings, RentalType rentalType, Double price) {
        this.name = name;
        this.description = description;
        this.capacity = capacity;
        this.rules = rules;
        this.isDeleted = isDeleted;
        this.address = address;
        this.averageRate = averageRate;
        this.noRatings = noRatings;
        this.rentalType = rentalType;
        this.price = price;
        this.photos = new HashSet<Photo>();
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

    public boolean isDeleted() {
        return isDeleted;
    }

    public void setDeleted(boolean deleted) {
        isDeleted = deleted;
    }

    public Address getAddress() {
        return address;
    }

    public void setAddress(Address address) {
        this.address = address;
    }

    public Double getAverageRate() {
        return averageRate;
    }

    public void setAverageRate(Double averageRate) {
        this.averageRate = averageRate;
    }

    public int getNoRatings() {
        return noRatings;
    }

    public void setNoRatings(int noRatings) {
        this.noRatings = noRatings;
    }

    public RentalType getRentalType() {
        return rentalType;
    }

    public void setRentalType(RentalType rentalType) {
        this.rentalType = rentalType;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Set<Photo> getPhotos() {
        return photos;
    }

    public void setPhotos(Set<Photo> photos) {
        this.photos = photos;
    }

    public List<Reservation> getReservations() {
        return reservations;
    }

    public void setReservations(List<Reservation> reservations) {
        this.reservations = reservations;
    }

    
}
