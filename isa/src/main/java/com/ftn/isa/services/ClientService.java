package com.ftn.isa.services;

import com.ftn.isa.DTO.ClientProfileDTO;
import com.ftn.isa.model.Client;
import com.ftn.isa.repository.ClientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ClientService {
    @Autowired
    private ClientRepository clientRepo;

    public Client findByEmail(String email) {
        return clientRepo.findByEmail(email);
    }

    public boolean save(ClientProfileDTO clientData, Client client) {
        // Napraviti izmenu podataka klijenta i provera
        client.setPhoneNumber(clientData.getPhoneNumber());
        clientRepo.save(client);
        return true;
    }


}
