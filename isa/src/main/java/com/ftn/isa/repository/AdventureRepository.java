package com.ftn.isa.repository;

import com.ftn.isa.model.Adventure;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Date;
import java.util.List;

public interface AdventureRepository extends JpaRepository<Adventure, Long> {
    @Query(nativeQuery = true,
            value = "SELECT adv.* " +
                        "FROM adventure as adv " +
                        "JOIN reservation AS res ON adv.id = res.rental_id " +
                        "JOIN address AS addr ON addr.id = adv.address_id " +
                            "WHERE adv.is_deleted = false " +
                            "AND res.start_time NOT BETWEEN :startDate AND :endDate " +
                            "AND res.end_time NOT BETWEEN :startDate AND :endDate " +
                            "AND (:minPrice = -1 OR adv.price >= :minPrice) " +
                            "AND (:maxPrice = -1 OR adv.price <= :maxPrice) " +
                            "AND (:location = '' OR addr.place_name = :location) " +
                            "AND (:minRate = -1 OR adv.average_rate >= :minRate) " +
                            "AND (:maxRate = -1 OR adv.average_rate <= :maxRate) " +
                                "GROUP BY adv.id")
    List<Adventure> searchAdventures(@Param("startDate") Date startDate, @Param("endDate") Date endDate,
                                     @Param("minPrice") double minPrice, @Param("maxPrice") double maxPrice,
                                     @Param("location") String location, @Param("minRate") double minRate,
                                     @Param("maxRate") double maxRate);
}
