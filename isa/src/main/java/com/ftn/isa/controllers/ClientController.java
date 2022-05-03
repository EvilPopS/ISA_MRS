package com.ftn.isa.controllers;

import com.ftn.isa.DTO.BasicClientDTO;
import com.ftn.isa.DTO.ClientProfileDTO;
import com.ftn.isa.DTO.UserRegDTO;
import com.ftn.isa.model.Client;
import com.ftn.isa.services.ClientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value="api/client")
public class ClientController {
    @Autowired
    private ClientService clientService;

    @GetMapping(value="/{email}")
    @CrossOrigin(origins = "http://localhost:8081")
    public ResponseEntity<ClientProfileDTO> getByEmail(@PathVariable String email) {
        Client client = clientService.findByEmail(email);
        if (client == null)
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        return new ResponseEntity<>(new ClientProfileDTO(client), HttpStatus.OK);
    }

    @GetMapping(value="/basic-profile/{email}")
    @CrossOrigin(origins = "http://localhost:8081")
    public ResponseEntity<BasicClientDTO> getBasicProfileByEmail(@PathVariable String email) {
        Client client = clientService.findByEmail(email);
        if (client == null)
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        return new ResponseEntity<>(new BasicClientDTO(client), HttpStatus.OK);
    }

    @PutMapping(consumes="application/json", value="/data-update")
    @CrossOrigin(origins = "http://localhost:8081")
    public ResponseEntity<ClientProfileDTO> updatePersonalData(@RequestBody ClientProfileDTO clientData) {
        Client client = clientService.findByEmail(clientData.getEmail());

        if (client == null)
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);

        if (clientData.arePropsValid())
            return new ResponseEntity<>(HttpStatus.UNPROCESSABLE_ENTITY);

        clientService.updatePersonalInfo(clientData, client);
        return new ResponseEntity<>(clientData, HttpStatus.OK);
    }

    @PostMapping(consumes="application/json", value="/register")
    @CrossOrigin(origins = "http://localhost:8081")
    public ResponseEntity<HttpStatus> registerUser(@RequestBody UserRegDTO clientData) {
        if (!clientData.arePropsValid())
            return new ResponseEntity<>(HttpStatus.UNPROCESSABLE_ENTITY);

        if (clientService.findByEmail(clientData.getEmail()) != null)
            return new ResponseEntity<>(HttpStatus.CONFLICT);

        clientService.registerClient(new Client(clientData));
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
