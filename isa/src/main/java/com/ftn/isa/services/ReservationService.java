package com.ftn.isa.services;

import com.ftn.isa.DTO.ActionResDTO;
import com.ftn.isa.DTO.RegularResDTO;
import com.ftn.isa.DTO.ReservationDTO;
import com.ftn.isa.model.*;
import com.ftn.isa.repository.ReservationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
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

    public List<Reservation> getAllReservations(){return reservationRepository.getAllReservations();}

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

    @Transactional(propagation = Propagation.REQUIRED)
    public Reservation addNewActionRes(ActionResDTO actionResDTO) {

        for (Reservation res : reservationRepository.getAllResForNewRes(actionResDTO.getRentalId())){
            if (res.periodsAreOverlapping(actionResDTO.getStartTime(), actionResDTO.getEndTime()))
                return null;
        }

        Reservation res = new Reservation(actionResDTO.getStartTime(), actionResDTO.getEndTime(),
                true, actionResDTO.getPrice(), false, false, actionResDTO.getActionServices());

        res = reservationRepository.save(res);
        return res;
    }

    @Transactional(propagation = Propagation.REQUIRED)
    public void test() {
        List<Reservation> reservations = reservationRepository.getAllResForNewRes(5l);
        /*try {
            Thread.sleep(200);
        } catch (InterruptedException e) {
           System.out.println("Glupost");
        }
        */
    }

    @Transactional(propagation = Propagation.REQUIRED)
    public Reservation addNewRegularRes(RegularResDTO regularResDTO, Client client, boolean isUnvailable) {

        for (Reservation res : reservationRepository.getAllResForNewRes(regularResDTO.getRentalId())){
            if (res.periodsAreOverlapping(regularResDTO.getStartTime(), regularResDTO.getEndTime()))
                return null;
        }

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
