package com.ftn.isa.services;

import com.ftn.isa.DTO.ClientProfileDTO;
import com.ftn.isa.model.Client;
import com.ftn.isa.repository.ClientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class ClientService {
    @Autowired
    private ClientRepository clientRepo;

    public Client findByEmail(String email) {
        return clientRepo.findByEmail(email);
    }

    public void updatePersonalInfo(ClientProfileDTO clientData, Client client) {
        client.updatePersonalInfo(clientData);
        saveOrUpdateClient(client);
    }

    public List<Client> getAllClients(){
        return clientRepo.findAll();
    }

    public void saveOrUpdateClient(Client client) {
        clientRepo.save(client);
    }


}
