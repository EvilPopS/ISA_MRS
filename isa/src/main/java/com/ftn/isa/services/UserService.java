package com.ftn.isa.services;

import com.ftn.isa.model.User;
import com.ftn.isa.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService {
    @Autowired
    private AdminRepository adminRepository;
    @Autowired
    private ClientRepository clientRepository;
    @Autowired
    private CottageOwnerRepository cottageOwnerRepository;
    @Autowired
    private BoatOwnerRepository boatOwnerRepository;
    @Autowired
    private FishingInstructorRepository instructorRepository;

    public User getUserByEmailAndRole(String email, String type) {
        switch (type) {
            case "CLIENT":
                return clientRepository.findByEmail(email);
            case "ADMIN":
                return adminRepository.findByEmail(email);
            case "COTTAGE_OWNER":
                return cottageOwnerRepository.findByEmail(email);
            case "BOAT_OWNER":
                return boatOwnerRepository.findByEmail(email);
            case "INSTRUCTOR":
                return instructorRepository.findByEmail(email);
        }
        return null;
    }
}
