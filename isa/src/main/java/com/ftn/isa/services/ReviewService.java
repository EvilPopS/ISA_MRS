package com.ftn.isa.services;

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
}
