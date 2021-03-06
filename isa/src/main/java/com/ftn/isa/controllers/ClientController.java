package com.ftn.isa.controllers;

import com.ftn.isa.DTO.*;
import com.ftn.isa.configs.ServerConfig;
import com.ftn.isa.helpers.Validate;
import com.ftn.isa.model.Client;
import com.ftn.isa.model.RentalService;
import com.ftn.isa.model.Reservation;
import com.ftn.isa.model.Subscription;
import com.ftn.isa.security.auth.TokenUtils;
import com.ftn.isa.services.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.orm.ObjectOptimisticLockingFailureException;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping(value="api/client")
public class ClientController {
    @Autowired
    private ClientService clientService;

    @Autowired
    private TokenUtils tokenUtils;
    @Autowired
    private RentalServService rentalServService;
    @Autowired
    private ReservationService reservationService;

    @Autowired
    private LoyaltyProgramService loyaltyProgramService;

    @GetMapping
    @PreAuthorize("hasRole('CLIENT')")
    @CrossOrigin(origins = ServerConfig.FRONTEND_ORIGIN)
    public ResponseEntity<ClientProfileDTO> getProfileData(HttpServletRequest request) {
        String email = tokenUtils.getEmailDirectlyFromHeader(request);
        if (email == null)
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);

        Client client = clientService.findByEmail(email);
        if (client == null)
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        return new ResponseEntity<>(new ClientProfileDTO(client), HttpStatus.OK);
    }

    @GetMapping(value="/basic-profile/{email}")
    @CrossOrigin(origins = ServerConfig.FRONTEND_ORIGIN)
    @PreAuthorize("hasRole('COTTAGE_OWNER') || hasRole('INSTRUCTOR') || hasRole('BOAT_OWNER')")
    public ResponseEntity<BasicClientDTO> getBasicProfileByEmail(HttpServletRequest request, @PathVariable String email) {
        String ownerEmail = tokenUtils.getEmailDirectlyFromHeader(request);
        if (ownerEmail == null)
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);

        Client client = clientService.findByEmail(email);
        if (client == null)
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        return new ResponseEntity<>(new BasicClientDTO(client), HttpStatus.OK);
    }

    @PutMapping(consumes="application/json", value="/data-update")
    @PreAuthorize("hasRole('CLIENT')")
    @CrossOrigin(origins = ServerConfig.FRONTEND_ORIGIN)
    public ResponseEntity<ClientProfileDTO> updatePersonalData(@RequestBody ClientProfileDTO clientData, HttpServletRequest request) {
        String email = tokenUtils.getEmailDirectlyFromHeader(request);
        if (email == null)
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);

        if (!clientData.arePropsValid())
            return new ResponseEntity<>(HttpStatus.UNPROCESSABLE_ENTITY);

        Client client = clientService.findByEmail(email);

        clientService.updatePersonalInfo(clientData, client);
        return new ResponseEntity<>(clientData, HttpStatus.OK);
    }

    @GetMapping(value="/reservation-history")
    @PreAuthorize("hasRole('CLIENT')")
    @CrossOrigin(origins = ServerConfig.FRONTEND_ORIGIN)
    public ResponseEntity<List<ReservationDisplayDTO>> getReservationHistory(HttpServletRequest request) {
        String email = tokenUtils.getEmailDirectlyFromHeader(request);
        if (email == null)
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);

        return new ResponseEntity<>(clientService.getReservationHistory(email), HttpStatus.OK);
    }

    @GetMapping(value="/upcoming-reservations")
    @PreAuthorize("hasRole('CLIENT')")
    @CrossOrigin(origins = ServerConfig.FRONTEND_ORIGIN)
    public ResponseEntity<List<ReservationDisplayDTO>> getUpcomingReservations(HttpServletRequest request) {
        String email = tokenUtils.getEmailDirectlyFromHeader(request);
        if (email == null)
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);

        return new ResponseEntity<>(clientService.getUpcomingReservations(email), HttpStatus.OK);
    }

    @PostMapping(value="/make-reservation")
    @PreAuthorize("hasRole('CLIENT')")
    @CrossOrigin(origins = ServerConfig.FRONTEND_ORIGIN)
    public ResponseEntity<HttpStatus> makeReservation (@RequestBody ReservingInfoDTO reservationData, HttpServletRequest request) {
        String email = tokenUtils.getEmailDirectlyFromHeader(request);
        if (email == null)
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);

        Client client = clientService.findByEmail(email);
        if (client.getNumOfPenalties() > 2)
            return new ResponseEntity<>(HttpStatus.NOT_ACCEPTABLE);

        if (!Validate.validateDatePeriod(reservationData.getStartDate(), reservationData.getEndDate())
                || !Validate.validateIfResPeriodWasCanceled(client.getReservations(), reservationData)
        )
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);

        RentalService rental;
        try {
            rental = rentalServService.getEntityByTypeAndId(reservationData.getRentalType(), reservationData.getRentalId());
            if (rental == null || !Validate.validateIfReservationPeriodIsAvailable(rental.getReservations(), reservationData))
                return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        } catch (Exception ignored) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        try {
            reservationService.saveReservation(
                    new Reservation(
                            reservationData,
                            rental.getPrice(),
                            rental,
                            client
                    ),
                    rental
            );
        } catch(ObjectOptimisticLockingFailureException e) {
            return new ResponseEntity<>(HttpStatus.CONFLICT);
        }

        clientService.increasePoints(client);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PutMapping(value="/make-action-reservation/{resId}")
    @PreAuthorize("hasRole('CLIENT')")
    @CrossOrigin(origins = ServerConfig.FRONTEND_ORIGIN)
    public ResponseEntity<HttpStatus> makeActionReservation (@PathVariable Long resId, HttpServletRequest request) {
        String email = tokenUtils.getEmailDirectlyFromHeader(request);
        if (email == null)
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);

        Client client = clientService.findByEmail(email);
        if (client.getNumOfPenalties() > 2)
            return new ResponseEntity<>(HttpStatus.NOT_ACCEPTABLE);

        try {
            reservationService.makeActionReservation(resId, client);
        } catch(ObjectOptimisticLockingFailureException e) {
            return new ResponseEntity<>(HttpStatus.CONFLICT);
        }
        catch(Exception ignored) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        clientService.increasePoints(client);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PutMapping(value="/cancel-reservation/{resId}")
    @PreAuthorize("hasRole('CLIENT')")
    @CrossOrigin(origins = ServerConfig.FRONTEND_ORIGIN)
    public ResponseEntity<HttpStatus> cancelReservation (@PathVariable Long resId, HttpServletRequest request) {
        String email = tokenUtils.getEmailDirectlyFromHeader(request);
        if (email == null)
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);

        reservationService.cancelReservation(resId);

        clientService.decreasePoints(clientService.findByEmail(email));
        return new ResponseEntity<>(HttpStatus.OK);
    }


    @GetMapping(value="/subscriptions")
    @PreAuthorize("hasRole('CLIENT')")
    @CrossOrigin(origins = ServerConfig.FRONTEND_ORIGIN)
    public ResponseEntity<List<SubClientPreview>> fetchSubscriptions (HttpServletRequest request) {
        String email = tokenUtils.getEmailDirectlyFromHeader(request);
        if (email == null)
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);

        List<SubClientPreview> subs = new ArrayList<>();
        for (Subscription sub : clientService.findByEmail(email).getSubscriptions())
            if (sub.isActiveSubscription())
                subs.add(new SubClientPreview(sub));

        return new ResponseEntity<>(subs, HttpStatus.OK);
    }


    @GetMapping(value="/check-if-subscribed/{ownerId}")
    @PreAuthorize("hasRole('CLIENT')")
    @CrossOrigin(origins = ServerConfig.FRONTEND_ORIGIN)
    public ResponseEntity<HttpStatus> checkIfSubscribed(@PathVariable Long ownerId, HttpServletRequest request) {
        String email = tokenUtils.getEmailDirectlyFromHeader(request);
        if (email == null)
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);

        if (!clientService.checkIfSubscribed(clientService.findByEmail(email), ownerId))
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);

        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PostMapping(value="/subscribe/{ownerId}")
    @PreAuthorize("hasRole('CLIENT')")
    @CrossOrigin(origins = ServerConfig.FRONTEND_ORIGIN)
    public ResponseEntity<HttpStatus> subscribe(@PathVariable Long ownerId, @RequestParam("userType") String usrType, HttpServletRequest request) {
        String email = tokenUtils.getEmailDirectlyFromHeader(request);
        if (email == null)
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);

        Client client = clientService.findByEmail(email);
        if (clientService.checkIfSubscribed(client, ownerId))
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);

        clientService.subscribeToOwner(client, ownerId, usrType);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PutMapping(value="/unsubscribe/{ownerId}")
    @PreAuthorize("hasRole('CLIENT')")
    @CrossOrigin(origins = ServerConfig.FRONTEND_ORIGIN)
    public ResponseEntity<HttpStatus> unsubscribe(@PathVariable Long ownerId, HttpServletRequest request) {
        String email = tokenUtils.getEmailDirectlyFromHeader(request);
        if (email == null)
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);

        clientService.unsubscribeFromOwner(clientService.findByEmail(email), ownerId);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PutMapping(value="/buy-loyalty-program/{program}")
    @PreAuthorize("hasRole('CLIENT')")
    @CrossOrigin(origins = ServerConfig.FRONTEND_ORIGIN)
    public ResponseEntity<HttpStatus> buyLoyaltyProgram(@PathVariable String program, HttpServletRequest request) {
        String email = tokenUtils.getEmailDirectlyFromHeader(request);
        if (email == null)
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);

        if (
            !clientService.updateClientLoyaltyProgram(clientService.findByEmail(email), loyaltyProgramService.getAllLoyaltyPrograms(), program)
        )
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);

        return new ResponseEntity<>(HttpStatus.OK);
    }
}
