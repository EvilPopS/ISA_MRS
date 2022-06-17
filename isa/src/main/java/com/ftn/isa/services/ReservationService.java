package com.ftn.isa.services;

import com.ftn.isa.DTO.ActionResDTO;
import com.ftn.isa.DTO.RegularResDTO;
import com.ftn.isa.DTO.ReservationDTO;
import com.ftn.isa.model.*;
import com.ftn.isa.repository.ReservationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.PessimisticLockingFailureException;
import org.springframework.stereotype.Service;
import java.time.LocalDate;
import java.util.ArrayList;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
public class ReservationService {
    @Autowired
    private ReservationRepository reservationRepository;

    public List<Reservation> getAllReservations(){return reservationRepository.getAllReservations();}

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
                if (!reservation.isUnavailable() && reservation.isReserved() && !reservation.isCanceled()) {
                    ReservationDTO reservationDTO = new ReservationDTO(reservation.getId(), c.getId(),
                            c.getName(), reservation.getStartTime(), reservation.getEndTime(),
                            reservation.getPrice(), reservation.isAction(), reservation.isReserved(), reservation.getActionServices());
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
                            reservation.getPrice(), reservation.isAction(), reservation.isReserved(), reservation.getActionServices());
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

    public Set<ReservationDTO> createResDTO(BoatOwner boatOwner, List<Client> allClients) {
        Set<ReservationDTO> reservations = new HashSet<>();
        for (Boat c : boatOwner.getBoats()){
            for (Reservation reservation : c.getReservations()){
                if (!reservation.isUnavailable() && reservation.isReserved() && !reservation.isCanceled()) {
                    ReservationDTO reservationDTO = new ReservationDTO(reservation.getId(), c.getId(),
                            c.getName(), reservation.getStartTime(), reservation.getEndTime(),
                            reservation.getPrice(), reservation.isAction(), reservation.isReserved(), reservation.getActionServices());
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

    public boolean checkIfIsInUnvailable(LocalDateTime startTime, LocalDateTime endTime, Long rentalId, Boolean isBoatOwner){
        List<Reservation> reservations = reservationRepository.getAllReservations();
        //ako je boat owner ide na nivou svih
        for (Reservation res : reservations){
            boolean stepIn = false;
            if (isBoatOwner) stepIn = true;
            else if (res.getRental().getId().equals(rentalId)) stepIn = true;
            if (res.isUnavailable() && stepIn) {
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
                    if (!reservation.isCanceled()) {
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
                }

        return false;
    }

    public Reservation addNewActionRes(ActionResDTO actionResDTO, CottageOwner cottageOwner) {
        for (Cottage c : cottageOwner.getCottages()){
            if (c.getId().equals(actionResDTO.getRentalId())){
                if (checkOverlapingWithOtherRes(c.getReservations(), actionResDTO.getStartTime(), actionResDTO.getEndTime()))
                    return null;
            }
        }

        if (checkIfIsInUnvailable(actionResDTO.getStartTime(), actionResDTO.getEndTime(), actionResDTO.getRentalId(), false))
            return null;

        Reservation res = new Reservation(actionResDTO.getStartTime(), actionResDTO.getEndTime(),
                true, actionResDTO.getPrice(), false, false, actionResDTO.getActionServices());

        res = reservationRepository.save(res);
        return res;
    }

    //za boat za razliku od cottage ownera se gleda za sve boatove u isto vreme
    public Reservation addNewActionRes(ActionResDTO actionResDTO, BoatOwner boatOwner) {

        for (Boat c : boatOwner.getBoats()){
            if (checkOverlapingWithOtherRes(c.getReservations(), actionResDTO.getStartTime(), actionResDTO.getEndTime()))
                return null;
        }

        if (checkIfIsInUnvailable(actionResDTO.getStartTime(), actionResDTO.getEndTime(), actionResDTO.getRentalId(), true))
            return null;

        Reservation res = new Reservation(actionResDTO.getStartTime(), actionResDTO.getEndTime(),
                true, actionResDTO.getPrice(), false, false, actionResDTO.getActionServices());

        res = reservationRepository.save(res);
        return res;
    }

    public Reservation addNewActionResAdventure(ActionResDTO actionResDTO, FishingInstructor fishingInstructor) {

        for (Adventure a : fishingInstructor.getAdventures()) {
            if (a.getId().equals(actionResDTO.getRentalId())) {
                for (Reservation reservation : a.getReservations()) {
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

    public List<Reservation> findUpcomingReservationsByRentalId(Long id, List<Reservation> reservations) {
        List<Reservation> retVal = new ArrayList<>();
        for (Reservation res : reservations){
            if (res.getRental().getId().equals(id) && !res.isCanceled()
                && res.getEndTime().isAfter(LocalDateTime.now())
            ){
                retVal.add(res);
            }
        }

        return retVal;
    }

    public boolean checkOverlapingWithOtherRes(List<Reservation> reservations, LocalDateTime startTime, LocalDateTime endTime) {
        for (Reservation reservation : reservations) {
            if (!reservation.isCanceled()) {
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
        }

        return false;
    }

    public boolean checkIfIsInUnvailable(LocalDateTime startTime, LocalDateTime endTime, Long rentalId, Boolean isBoatOwner){
        List<Reservation> reservations = reservationRepository.getAllReservations();
        //ako je boat owner ide na nivou svih
        for (Reservation res : reservations){
            boolean stepIn = false;
            if (isBoatOwner) stepIn = true;
            else if (res.getRental().getId().equals(rentalId)) stepIn = true;
            if (res.isUnavailable() && stepIn) {
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

    //@Transactional(readOnly = true, propagation = Propagation.REQUIRED)
    public Reservation addNewRegularRes(RegularResDTO regularResDTO, CottageOwner cottageOwner, Client client, boolean isUnvailable) {
        for (Cottage c : cottageOwner.getCottages()){
            if (c.getId().equals(regularResDTO.getRentalId())){
                if (checkOverlapingWithOtherRes(c.getReservations(), regularResDTO.getStartTime(), regularResDTO.getEndTime()))
                    return null;
            }
        }

        if (checkIfIsInUnvailable(regularResDTO.getStartTime(), regularResDTO.getEndTime(), regularResDTO.getRentalId(), false))
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

    //za boat za razliku od cottage ownera se gleda za sve boatove u isto vreme
    public Reservation addNewRegularRes(RegularResDTO regularResDTO, BoatOwner boatOwner, Client client, boolean isUnvailable) {
        for (Boat c : boatOwner.getBoats()){
            if (checkOverlapingWithOtherRes(c.getReservations(), regularResDTO.getStartTime(), regularResDTO.getEndTime()))
                return null;
        }

        if (checkIfIsInUnvailable(regularResDTO.getStartTime(), regularResDTO.getEndTime(), regularResDTO.getRentalId(), true))
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
