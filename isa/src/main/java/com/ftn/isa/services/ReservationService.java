package com.ftn.isa.services;

import com.ftn.isa.DTO.ActionResDTO;
import com.ftn.isa.DTO.RegularResDTO;
import com.ftn.isa.DTO.ReservationDTO;
import com.ftn.isa.model.*;
import com.ftn.isa.repository.ReservationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
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


    public void cancelReservation(Long resId) {
        Reservation res = findById(resId);
        res.setCanceled(true);
        reservationRepository.save(res);
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

    public Set<ReservationDTO> createResDTO(FishingInstructor fishingInstructor, List<Client> allClients) {
        Set<ReservationDTO> reservations = new HashSet<>();
        for (Adventure a : fishingInstructor.getAdventures()){
            for (Reservation reservation : a.getReservations()){
                if (!reservation.isUnavailable() && reservation.isReserved()) {
                    ReservationDTO reservationDTO = new ReservationDTO(reservation.getId(), a.getId(),
                            a.getName(), reservation.getStartTime(), reservation.getEndTime(),
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



    public boolean checkIfIsInUnvailable(LocalDateTime startTime, LocalDateTime endTime, Long rentalId){
        List<Reservation> reservations = reservationRepository.getAllReservations();
        for (Reservation res : reservations){
            if (res.isUnavailable() && res.getRental().getId().equals(rentalId)) {
                //kada je unvailable period
                if (startTime.isAfter(res.getStartTime()) &&
                    startTime.isBefore(res.getEndTime()) &&
                    endTime.isBefore(res.getEndTime()))
                    return true;
                else if (startTime.isBefore(res.getStartTime()) &&
                        startTime.isBefore(res.getEndTime()) &&
                        endTime.isAfter(res.getEndTime()))
                    return true;
                else if (startTime.isAfter(res.getStartTime()) &&
                        startTime.isBefore(res.getEndTime()) &&
                        endTime.isAfter(res.getEndTime()))
                    return true;
            }
        }

        return false;
    }

    public boolean checkOverlapingWithOtherRes(List<Reservation> reservations, LocalDateTime startTime, LocalDateTime endTime) {
                for (Reservation reservation : reservations) {
                    if (startTime.isAfter(reservation.getStartTime()) &&
                            startTime.isBefore(reservation.getEndTime()) &&
                            endTime.isBefore(reservation.getEndTime()))
                        return true;
                    else if (startTime.isBefore(reservation.getStartTime()) &&
                            startTime.isBefore(reservation.getEndTime()) &&
                            endTime.isAfter(reservation.getEndTime()))
                        return true;
                    else if (startTime.isAfter(reservation.getStartTime()) &&
                            startTime.isBefore(reservation.getEndTime()) &&
                            endTime.isAfter(reservation.getEndTime()))
                        return true;
                }

        return false;
    }

    public Reservation addNewActionRes(ActionResDTO actionResDTO, CottageOwner cottageOwner) {

        for (Cottage c : cottageOwner.getCottages()){
            if (c.getId().equals(actionResDTO.getCottageId())){
                if (checkOverlapingWithOtherRes(c.getReservations(), actionResDTO.getStartTime(), actionResDTO.getEndTime()))
                    return null;
            }
        }
      
        if (checkIfIsInUnvailable(actionResDTO.getStartTime(), actionResDTO.getEndTime(), actionResDTO.getCottageId()))
            return null;

        Reservation res = new Reservation(actionResDTO.getStartTime(), actionResDTO.getEndTime(),
                true, actionResDTO.getPrice(), false, false, actionResDTO.getActionServices());

        res = reservationRepository.save(res);
        return res;
    }

    public Reservation addNewRegularRes(RegularResDTO regularResDTO, CottageOwner cottageOwner, Client client, boolean isUnvailable) {
        for (Cottage c : cottageOwner.getCottages()){
            if (c.getId().equals(regularResDTO.getCottageId())){
                if (checkOverlapingWithOtherRes(c.getReservations(), regularResDTO.getStartTime(), regularResDTO.getEndTime()))
                    return null;
            }
        }

        if (checkIfIsInUnvailable(regularResDTO.getStartTime(), regularResDTO.getEndTime(), regularResDTO.getCottageId()))
            return null;

        Reservation res = null;
        if (!isUnvailable)
            res = new Reservation(regularResDTO.getStartTime(), regularResDTO.getEndTime(),
                    false, regularResDTO.getPrice(), true, false, null);
        else
            res = new Reservation(regularResDTO.getStartTime(), regularResDTO.getEndTime(), false,
            0.0, true, true, null);

        res.setClient(client); //client ili null za slucaj da je unvailable period
        res = reservationRepository.save(res);
        return res;

    }
}
