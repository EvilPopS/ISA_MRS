package com.ftn.isa.repository;

import com.ftn.isa.model.Reservation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import java.util.List;


public interface ReservationRepository extends JpaRepository<Reservation, Long> {
    @Query(nativeQuery = true, value = "select * from reservation")
    public List<Reservation> getAllReservations();
  
}
