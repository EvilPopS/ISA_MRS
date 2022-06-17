package com.ftn.isa.services;

import com.ftn.isa.DTO.RentalReviewDTO;
import com.ftn.isa.model.Client;
import com.ftn.isa.model.RentalService;
import com.ftn.isa.model.Review;
import com.ftn.isa.repository.ReviewRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ReviewService {

    @Autowired
    ReviewRepository reviewRepository;

    public List<Review> getAllReviews(){return reviewRepository.getAllRequests();}

    public List<Review> getReviewsForRental(Long rentalId) {
        return reviewRepository.getReviewsForRental(rentalId);
    }

    public void makeNewReview(RentalReviewDTO reviewData, Client client, RentalService rental) {
        reviewRepository.save(
                new Review(reviewData.getReview(), reviewData.getRating(), client, rental)
        );
    }

}
