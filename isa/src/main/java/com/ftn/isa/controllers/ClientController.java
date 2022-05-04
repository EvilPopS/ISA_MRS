package com.ftn.isa.controllers;

import com.ftn.isa.DTO.BasicClientDTO;
import com.ftn.isa.DTO.ClientProfileDTO;
import com.ftn.isa.DTO.UserRegDTO;
import com.ftn.isa.model.Client;
import com.ftn.isa.services.ClientService;
import com.ftn.isa.services.EmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value="api/client")
public class ClientController {
    @Autowired
    private ClientService clientService;
    @Autowired
    private EmailService emailService;


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

        if (!clientData.arePropsValid())
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

        Client client = new Client(clientData);
        try {
            clientService.saveOrUpdateClient(client);
        } catch (Exception ignored) {
            return new ResponseEntity<>(HttpStatus.UNPROCESSABLE_ENTITY);
        }

        try {
            emailService.sendMail(client, "Confirmation mail",
                "Click here to activate your account: http://localhost:8080/api/client/confirm-mail/" + client.getEmail());
        } catch(Exception ignored){
            return new ResponseEntity<>(HttpStatus.EXPECTATION_FAILED);
        }
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping(value="/confirm-mail/{email}")
    public ResponseEntity<HttpStatus> activateAccount(@PathVariable String email){
        Client client = clientService.findByEmail(email);
        if (client == null)
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);

        client.setActive(true);
        clientService.saveOrUpdateClient(client);

        return new ResponseEntity<>(HttpStatus.OK);
    }
}
