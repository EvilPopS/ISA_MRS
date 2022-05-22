package com.ftn.isa.services;

import com.ftn.isa.DTO.ClientProfileDTO;
import com.ftn.isa.DTO.ReservationDisplayDTO;
import com.ftn.isa.model.*;
import com.ftn.isa.repository.AdventureRepository;
import com.ftn.isa.repository.BoatRepository;
import com.ftn.isa.repository.ClientRepository;
import com.ftn.isa.repository.CottageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Service
public class ClientService {
    @Autowired
    private ClientRepository clientRepo;
    @Autowired
    private AdventureRepository adventureRepo;
    @Autowired
    private CottageRepository cottageRepo;
    @Autowired
    private BoatRepository boatRepo;

    public Client findByEmail(String email) {
        return clientRepo.findByEmail(email);
    }

    public void updatePersonalInfo(ClientProfileDTO clientData, Client client) {
        clientData.hashPassword();
        client.updatePersonalInfo(clientData);
        saveOrUpdateClient(client);
    }

    public List<Client> getAllClients(){
        return clientRepo.findAll();
    }

    public void saveOrUpdateClient(Client client) {
        clientRepo.save(client);
    }

    public List<ReservationDisplayDTO> getReservationHistory(String email) {
        return getReservationsByFilter(email, true);
    }

    public List<ReservationDisplayDTO> getUpcomingReservations(String email) {
        return getReservationsByFilter(email, false);
    }

    private List<ReservationDisplayDTO> getReservationsByFilter(String email, boolean isHistory) {
        Set<Reservation> reservations = clientRepo.findByEmail(email).getReservations();
        List<Adventure> adventures = adventureRepo.findAll();
        List<Cottage> cottages = cottageRepo.findAll();
        List<Boat> boats = boatRepo.findAll();

        List<ReservationDisplayDTO> filteredReservs = new ArrayList<>();

        for (Reservation res : reservations) {
            for(Adventure adv : adventures)
                if (matchReservation(res, adv, filteredReservs, isHistory))
                    break;
            for(Cottage cot : cottages)
                if (matchReservation(res, cot, filteredReservs, isHistory))
                    break;
            for(Boat boat : boats)
                if (matchReservation(res, boat, filteredReservs, isHistory))
                    break;
        }

        return filteredReservs;
    }

    private boolean matchReservation(Reservation res, RentalService rental, List<ReservationDisplayDTO> reservationHistory, boolean areDone) {
        for (Reservation r: rental.getReservations())
            if (res.getId().equals(r.getId())) {
                if (areDone) {
                    if (res.getEndTime().isBefore(LocalDateTime.now()))
                        reservationHistory.add(new ReservationDisplayDTO(res, rental));
                }
                else {
                    if (res.getEndTime().isAfter(LocalDateTime.now()))
                        reservationHistory.add(new ReservationDisplayDTO(res, rental));
                }
                return true;
            }
        return false;
    }

}
