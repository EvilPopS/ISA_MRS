package com.ftn.isa.services;

import com.ftn.isa.DTO.ClientProfileDTO;
import com.ftn.isa.DTO.ReservationDisplayDTO;
import com.ftn.isa.helpers.Validate;
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
    @Autowired
    private UserService userService;


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
            if (res.getId().equals(r.getId()) && !res.isCanceled()) {
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



    public boolean checkIfSubscribed(Client client, Long ownerId) {
        for (Subscription sub : client.getSubscriptions())
            if (sub.isActiveSubscription() && sub.getOwner().getId().equals(ownerId))
                return true;
        return false;
    }

    public void subscribeToOwner(Client client, Long ownerId, String usrType) {
        client.getSubscriptions().add(
                new Subscription(userService.getUserByIdAndRole(ownerId, usrType), client, true)
        );
        clientRepo.save(client);
    }

    public void unsubscribeFromOwner(Client client, Long ownerId) {
        List<Subscription> subs = client.getSubscriptions();
        for (Subscription sub : subs)
            if (sub.isActiveSubscription() && sub.getOwner().getId().equals(ownerId)) {
                sub.setActiveSubscription(false);
                clientRepo.save(client);
                break;
            }
    }

    public void increasePoints(Client client) {
        client.incLoyaltyPoints(5);
        clientRepo.save(client);
    }

    public void decreasePoints(Client client) {
        client.incLoyaltyPoints(-5);
        clientRepo.save(client);
    }

    public boolean updateClientLoyaltyProgram(Client client, List<LoyaltyProgram> programs, String newProgram) {
        LoyaltyType type = LoyaltyType.REGULAR;
        switch (newProgram) {
            case "BRONZE":
                type = LoyaltyType.BRONZE;
                break;
            case "SILVER":
                type = LoyaltyType.SILVER;
                break;
            case "GOLD":
                type = LoyaltyType.GOLD;
                break;
        }

        for (LoyaltyProgram lp : programs)
            if (lp.getLoyaltyType() == type) {
                int leftPoints = client.getLoyaltyPoints() - lp.getPrice();
                if (leftPoints >= 0)
                    client.setLoyaltyPoints(leftPoints);
                else
                    return false;

                client.setLoyaltyType(type);
                clientRepo.save(client);
                return true;
            }
        return false;
    }
}
