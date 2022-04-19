package com.ftn.isa.repository;

import com.ftn.isa.model.Cottage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.util.Date;
import java.util.List;

public interface CottageRepository extends JpaRepository<Cottage, Long> {
    @Query(nativeQuery = true,
            value = "SELECT cot.* " +
                        "FROM cottage as cot " +
                        "JOIN reservation AS res ON cot.id = res.rental_id " +
                        "JOIN address AS addr ON addr.id = cot.address_id " +
                            "WHERE cot.is_deleted = false " +
                            "AND res.start_time NOT BETWEEN :startDate AND :endDate " +
                            "AND res.end_time NOT BETWEEN :startDate AND :endDate " +
                            "AND (:minPrice = -1 OR cot.price >= :minPrice) " +
                            "AND (:maxPrice = -1 OR cot.price <= :maxPrice) " +
                            "AND (:location = '' OR addr.place_name = :location) " +
                            "AND (:minRate = -1 OR cot.average_rate >= :minRate) " +
                            "AND (:maxRate = -1 OR cot.average_rate <= :maxRate) " +
                                "GROUP BY cot.id")
    List<Cottage> searchCottages(@Param("startDate") Date startDate, @Param("endDate") Date endDate,
                                     @Param("minPrice") double minPrice, @Param("maxPrice") double maxPrice,
                                     @Param("location") String location, @Param("minRate") double minRate,
                                     @Param("maxRate") double maxRate);
}
