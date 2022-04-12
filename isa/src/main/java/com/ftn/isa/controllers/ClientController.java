package com.ftn.isa.controllers;

import com.ftn.isa.DTO.ClientProfileDTO;
import com.ftn.isa.model.Client;
import com.ftn.isa.services.ClientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value="api/client")
@CrossOrigin(origins = "http://localhost:8081")
public class ClientController {
    @Autowired
    private ClientService clientService;

    @GetMapping(value="/{email}")
    public ResponseEntity<ClientProfileDTO> getByEmail(@PathVariable String email) {
        Client client = clientService.findByEmail(email);
        if (client == null)
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        return new ResponseEntity<>(new ClientProfileDTO(client), HttpStatus.OK);
    }

    @PutMapping(consumes="application/json", value="/data-update")
    public ResponseEntity<ClientProfileDTO> updatePersonalData(@RequestBody ClientProfileDTO clientData) {
        Client client = clientService.findByEmail(clientData.getEmail());

        if (client == null)
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);

        if (clientData.arePropsValid())
            return new ResponseEntity<>(HttpStatus.UNPROCESSABLE_ENTITY);

        clientService.updatePersonalInfo(clientData, client);
        return new ResponseEntity<>(clientData, HttpStatus.OK);
    }
}
