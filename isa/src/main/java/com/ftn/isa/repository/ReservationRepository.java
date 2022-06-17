package com.ftn.isa.repository;

import com.ftn.isa.model.Reservation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.QueryHints;
import org.springframework.data.repository.query.Param;

import javax.persistence.LockModeType;
import javax.persistence.QueryHint;
import java.util.List;
import java.util.Optional;

public interface ReservationRepository extends JpaRepository<Reservation, Long> {
    @Query(nativeQuery = true, value = "select * from reservation")
    List<Reservation> getAllReservations();

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @QueryHints({@QueryHint(name = "javax.persistence.lock.timeout", value ="0")})
    @Query(value = "select r from Reservation r where r.rental.id =?1")
    List<Reservation> getAllResForNewRes(Long id);

}
