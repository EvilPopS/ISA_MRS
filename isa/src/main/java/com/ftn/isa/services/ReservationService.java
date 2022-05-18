package com.ftn.isa.services;

import com.ftn.isa.model.Reservation;
import com.ftn.isa.repository.ReservationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ReservationService {
    @Autowired
    ReservationRepository reservationRepo;

    public void saveReservation(Reservation r) {
        reservationRepo.save(r);
    }

}
