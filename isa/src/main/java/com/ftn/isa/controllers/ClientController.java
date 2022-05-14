package com.ftn.isa.controllers;

import com.ftn.isa.DTO.BasicClientDTO;
import com.ftn.isa.DTO.ClientProfileDTO;
import com.ftn.isa.DTO.ReservationHistoryDTO;
import com.ftn.isa.DTO.UserRegDTO;
import com.ftn.isa.configs.ServerConfig;
import com.ftn.isa.model.Client;
import com.ftn.isa.services.ClientService;
import com.ftn.isa.services.EmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value="api/client")
public class ClientController {
    @Autowired
    private ClientService clientService;
    @Autowired
    private EmailService emailService;


    @GetMapping(value="/{email}")
    @CrossOrigin(origins = ServerConfig.FRONTEND_ORIGIN)
    public ResponseEntity<ClientProfileDTO> getByEmail(@PathVariable String email) {
        Client client = clientService.findByEmail(email);
        if (client == null)
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        return new ResponseEntity<>(new ClientProfileDTO(client), HttpStatus.OK);
    }

    @GetMapping(value="/basic-profile/{email}")
    @CrossOrigin(origins = ServerConfig.FRONTEND_ORIGIN)
    public ResponseEntity<BasicClientDTO> getBasicProfileByEmail(@PathVariable String email) {
        Client client = clientService.findByEmail(email);
        if (client == null)
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        return new ResponseEntity<>(new BasicClientDTO(client), HttpStatus.OK);
    }

    @PutMapping(consumes="application/json", value="/data-update")
    @CrossOrigin(origins = ServerConfig.FRONTEND_ORIGIN)
    public ResponseEntity<ClientProfileDTO> updatePersonalData(@RequestBody ClientProfileDTO clientData) {
        Client client = clientService.findByEmail(clientData.getEmail());

        if (client == null)
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);

        if (!clientData.arePropsValid())
            return new ResponseEntity<>(HttpStatus.UNPROCESSABLE_ENTITY);

        clientService.updatePersonalInfo(clientData, client);
        return new ResponseEntity<>(clientData, HttpStatus.OK);
    }

    @GetMapping(value="/reservation-history/{email}")
    @PreAuthorize("hasRole('CLIENT')")
    @CrossOrigin(origins = ServerConfig.FRONTEND_ORIGIN)
    public ResponseEntity<List<ReservationHistoryDTO>> getReservationHistory(@PathVariable String email) {
        return new ResponseEntity<>(clientService.getReservationHistory(email), HttpStatus.OK);
    }

}
