package com.ftn.isa.services;

import com.ftn.isa.DTO.ActionResDTO;
import com.ftn.isa.DTO.ReservationDTO;
import com.ftn.isa.model.*;
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

    public void saveReservation(Reservation r) {
        reservationRepository.save(r);
    }

    public Reservation findById(Long id) {
        return reservationRepository.findById(id).orElse(null);
    }

    public void makeActionReservation(Long resId, Client client) throws Exception {
        Reservation res = findById(resId);
        if (res.isReserved())
            throw new Exception("Already reserved!");
        res.setReserved(true);
        res.setClient(client);
        saveReservation(res);
    }

    public Set<ReservationDTO> createResDTO(CottageOwner cottageOwner, List<Client> allClients) {
        Set<ReservationDTO> reservations = new HashSet<>();
        for (Cottage c : cottageOwner.getCottages()){
            for (Reservation reservation : c.getReservations()){
                if (!reservation.isUnavailable() && reservation.isReserved()) {
                    ReservationDTO reservationDTO = new ReservationDTO(reservation.getId(), c.getId(),
                            c.getName(), reservation.getStartTime(), reservation.getEndTime(),
                            reservation.getPrice(), reservation.isAction(), reservation.isReserved());
                    reservations.add(reservationDTO);
                }
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

    public boolean checkIfIsInUnavailable(ActionResDTO actionResDTO){
        List<Reservation> reservations = reservationRepository.getAllReservations();
        for (Reservation res : reservations){
            if (res.isUnavailable()) {
                //kada je unavailable period
                if (actionResDTO.getStartTime().isAfter(res.getStartTime()) &&
                    actionResDTO.getStartTime().isBefore(res.getEndTime()) &&
                    actionResDTO.getEndTime().isBefore(res.getEndTime()))
                    return true;
                else if (actionResDTO.getStartTime().isBefore(res.getStartTime()) &&
                        actionResDTO.getStartTime().isBefore(res.getEndTime()) &&
                        actionResDTO.getEndTime().isAfter(res.getEndTime()))
                    return true;
                else if (actionResDTO.getStartTime().isAfter(res.getStartTime()) &&
                        actionResDTO.getStartTime().isBefore(res.getEndTime()) &&
                        actionResDTO.getEndTime().isAfter(res.getEndTime()))
                    return true;
            }
        }

        return false;
    }

    public Reservation addNewActionRes(ActionResDTO actionResDTO, CottageOwner cottageOwner) {

        for (Cottage c : cottageOwner.getCottages()) {
            if (c.getId().equals(actionResDTO.getCottageId())) {
                for (Reservation reservation : c.getReservations()) {
                    if (actionResDTO.getStartTime().isAfter(reservation.getStartTime()) &&
                            actionResDTO.getStartTime().isBefore(reservation.getEndTime()) &&
                            actionResDTO.getEndTime().isBefore(reservation.getEndTime()))
                        return null;
                    else if (actionResDTO.getStartTime().isBefore(reservation.getStartTime()) &&
                            actionResDTO.getStartTime().isBefore(reservation.getEndTime()) &&
                            actionResDTO.getEndTime().isAfter(reservation.getEndTime()))
                        return null;
                    else if (actionResDTO.getStartTime().isAfter(reservation.getStartTime()) &&
                            actionResDTO.getStartTime().isBefore(reservation.getEndTime()) &&
                            actionResDTO.getEndTime().isAfter(reservation.getEndTime()))
                        return null;
                }
            }
        }
        if (checkIfIsInUnavailable(actionResDTO))
            return null;
        Reservation res = new Reservation(actionResDTO.getStartTime(), actionResDTO.getEndTime(),
                true, actionResDTO.getPrice(), false, false, actionResDTO.getActionServices());

        res = reservationRepository.save(res);
        return res;
    }
}
