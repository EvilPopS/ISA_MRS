package com.ftn.isa.repository;

import com.ftn.isa.model.BoatOwner;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface BoatOwnerRepository extends JpaRepository<BoatOwner, Long> {
    BoatOwner findByEmail(String email);

    BoatOwner getOne(Long id);

    @Query(nativeQuery = true,
            value="SELECT own.* " +
                    "FROM boat_owner as own " +
                    "JOIN boat as bt ON bt.cottage_owner_id = own.id " +
                    "WHERE bt.id = :boat_id"
    )
    BoatOwner getOwnerByCBoatId(@Param("boat_id") double boatId);

}
