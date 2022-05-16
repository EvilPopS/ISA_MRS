package com.ftn.isa.services;

import com.ftn.isa.DTO.ReservationDTO;
import com.ftn.isa.model.Client;
import com.ftn.isa.model.Cottage;
import com.ftn.isa.model.CottageOwner;
import com.ftn.isa.model.Reservation;
import com.ftn.isa.repository.ReservationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
public class ReservationService {

    @Autowired
    private ReservationRepository reservationRepository;


    public Set<ReservationDTO> createResDTO(CottageOwner cottageOwner, List<Client> allClients) {
        Set<ReservationDTO> reservations = new HashSet<>();
        for (Cottage c : cottageOwner.getCottages()){
            for (Reservation reservation : c.getReservations()){
                ReservationDTO reservationDTO = new ReservationDTO(reservation.getId(), c.getId(),
                        c.getName(), reservation.getStartTime(), reservation.getEndTime(),
                        reservation.getPrice(), reservation.isAction(), reservation.isReserved());
                reservations.add(reservationDTO);
            }
        }

        for (ReservationDTO resDTO : reservations) {
            for (Client client : allClients) {
                for (Reservation r : client.getReservations()) {
                    if (resDTO.getReservationId() == r.getId()) {
                        resDTO.setClientEmail(client.getEmail());
                        resDTO.setClientProfilePhoto(client.getProfilePicture().getPhotoPath());
                        resDTO.setClientFullName(client.getName() + " " + client.getSurname());
                    }
                }
            }
        }

        return reservations;
    }
}
