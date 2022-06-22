package com.ftn.isa.repository;


import com.ftn.isa.model.Review;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReviewRepository extends JpaRepository<Review, Long> {

    @Query(nativeQuery = true, value = "select * from review")
    List<Review> getAllRequests();

    @Query(nativeQuery = true,
            value = "SELECT * FROM review " +
                        "WHERE review.rental_id = :rental_id AND is_answered"
    )
    List<Review> getReviewsForRental(@Param("rental_id") Long rentalId);

    Review getReviewById(Long id);


}
