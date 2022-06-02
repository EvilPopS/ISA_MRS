package com.ftn.isa.DTO;


import com.ftn.isa.model.Review;

public class RentalReviewDTO {
    private double rating;
    private String review;


    public RentalReviewDTO(Review review) {
        this.rating = review.getGrade();
        this.review = review.getMessage();
    }

    public double getRating() {
        return rating;
    }

    public void setRating(double rating) {
        this.rating = rating;
    }

    public String getReview() {
        return review;
    }

    public void setReview(String review) {
        this.review = review;
    }
}
