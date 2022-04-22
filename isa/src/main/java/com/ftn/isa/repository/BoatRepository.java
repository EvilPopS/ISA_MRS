package com.ftn.isa.repository;

import com.ftn.isa.model.Boat;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.util.Date;
import java.util.List;

public interface BoatRepository extends JpaRepository<Boat, Long> {
    @Query(nativeQuery = true,
            value = "SELECT bot.* " +
                        "FROM boat as bot " +
                        "JOIN reservation AS res ON bot.id = res.rental_id " +
                        "JOIN address AS addr ON addr.id = bot.address_id " +
                            "WHERE bot.is_deleted = false " +
                            "AND res.start_time NOT BETWEEN :startDate AND :endDate " +
                            "AND res.end_time NOT BETWEEN :startDate AND :endDate " +
                            "AND (:minPrice = -1 OR bot.price >= :minPrice) " +
                            "AND (:maxPrice = -1 OR bot.price <= :maxPrice) " +
                            "AND (:location = '' OR addr.place_name = :location) " +
                            "AND (:minRate = -1 OR bot.average_rate >= :minRate) " +
                            "AND (:maxRate = -1 OR bot.average_rate <= :maxRate) " +
                                "GROUP BY bot.id")
    List<Boat> searchBoats(@Param("startDate") Date startDate, @Param("endDate") Date endDate,
                           @Param("minPrice") double minPrice, @Param("maxPrice") double maxPrice,
                           @Param("location") String location, @Param("minRate") double minRate,
                           @Param("maxRate") double maxRate);
}
